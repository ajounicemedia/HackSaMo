package com.example.god_of_teaching.model.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.god_of_teaching.model.dataclass.StudentInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

private const val STARTING_KEY = 1
class StudentPagingSource(
    private val db: FirebaseFirestore,
    private val selectedSubject: List<String>,
    private val selectedLocation: List<String>,
    private val gender: String
) : PagingSource<Int, StudentInfo>() {
    private var lastDocumentSnapshot: QuerySnapshot? = null



    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StudentInfo> {
        return try {
            val pageSize = 30

            val baseQuery = db.collection("students")
                .whereArrayContainsAny("availableLocal", selectedLocation)
                .limit(pageSize.toLong())

            val pageQuery = if (params.key != null) {
                baseQuery.startAfter(lastDocumentSnapshot?.documents?.last())
            } else {
                baseQuery
            }

            val querySnapshot = pageQuery.get().await()

            lastDocumentSnapshot = querySnapshot // 마지막 문서 스냅샷 업데이트

            val matchedStudents = querySnapshot.documents.mapNotNull { document ->
                Log.d("123123123", document.toString())
                document.toObject(StudentInfo::class.java)?.takeIf { student ->
                    student.subject?.any { subject -> selectedSubject.contains(subject) } == true&&
                            (student.gender ==gender||gender=="상관 없음")
                }
            }


            Log.d("123123123","123")
            Log.d("123123123",matchedStudents.toString())
            LoadResult.Page(
                data = matchedStudents,
                prevKey = null, // Firestore는 이전 페이지로 돌아가는 것을 지원x
                nextKey = if (matchedStudents.size < pageSize) null else params.key?.plus(1) ?: STARTING_KEY + 1
            )
        } catch (e: Exception) {
            Log.e("StudentPagingSource", "Error loading data", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StudentInfo>): Int? {
        return null
    }
}