package god.example.god_of_teaching.model.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import god.example.god_of_teaching.model.dataclass.AcademyInfo
import kotlinx.coroutines.tasks.await

private const val STARTING_KEY = 1
//검색된 학원 목록
class AcademyPagingSource(
    private val db: FirebaseFirestore,
    private val selectedSubject: List<String>,
    private val selectedLocation: List<String>
) : PagingSource<Int, AcademyInfo>() {

    private var lastDocumentSnapshot: DocumentSnapshot? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AcademyInfo> {
        try {
            val pageSize = 5
            val currentPage = params.key ?: STARTING_KEY
            val baseQuery = db.collection("academies")
                .whereArrayContainsAny("searchedLocal", selectedLocation)
                .limit(pageSize.toLong())

            val pageQuery = lastDocumentSnapshot?.let {
                baseQuery.startAfter(it)
            } ?: baseQuery

            val querySnapshot = pageQuery.get().await()
            if (!querySnapshot.isEmpty) {
                lastDocumentSnapshot = querySnapshot.documents.lastOrNull()
            }



            val matchedAcademies = querySnapshot.documents.mapNotNull { document ->
                document.toObject(AcademyInfo::class.java)?.takeIf { academy ->
                    academy.subject?.any { subject -> selectedSubject.contains(subject) } == true
                }
            }

            val nextKey = if (matchedAcademies.size < pageSize) null else currentPage + 1
            return LoadResult.Page(
                data = matchedAcademies,
                prevKey = if (currentPage == STARTING_KEY) null else currentPage - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Log.e("버그", "AcademyPagingSource - Error loading data", e)
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AcademyInfo>): Int? {
        return null
    }
}