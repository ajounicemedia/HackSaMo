package com.example.god_of_teaching.model.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.god_of_teaching.model.dataclass.AcademyInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

private const val STARTING_KEY = 1
//검색된 학원 목록
class AcademyPagingSource(
    private val db: FirebaseFirestore,
    private val selectedSubject: List<String>,
    private val selectedLocation: List<String>
) : PagingSource<Int, AcademyInfo>() {

    private var lastDocumentSnapshot: QuerySnapshot? = null



    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AcademyInfo> {
        return try {
            val pageSize = 30

            val baseQuery = db.collection("academys")
                .whereArrayContainsAny("searchedLocal", selectedLocation)
                .limit(pageSize.toLong())

            val pageQuery = if (params.key != null) {
                baseQuery.startAfter(lastDocumentSnapshot?.documents?.last())
            } else {
                baseQuery
            }

            val querySnapshot = pageQuery.get().await()

            lastDocumentSnapshot = querySnapshot // 마지막 문서 스냅샷 업데이트
            val matchedAcademies = querySnapshot.documents.mapNotNull { document ->
                document.toObject(AcademyInfo::class.java)?.takeIf { academy ->
                    academy.subject?.any { subject -> selectedSubject.contains(subject) } == true
                }
            }


            LoadResult.Page(
                data = matchedAcademies,
                prevKey = null, // Firestore는 이전 페이지로 돌아가는 것을 지원x
                nextKey = if (matchedAcademies.size < pageSize) null else params.key?.plus(1) ?: STARTING_KEY + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AcademyInfo>): Int? {
        return null
    }
}