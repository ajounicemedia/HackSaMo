package god.example.god_of_teaching.model.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import god.example.god_of_teaching.model.dataclass.StudentInfo
import kotlinx.coroutines.tasks.await

private const val STARTING_KEY = 1
//검색된 학생 목록
class StudentPagingSource(
    private val db: FirebaseFirestore,
    private val selectedSubject: List<String>,
    private val selectedLocation: List<String>,
    private val gender: String
) : PagingSource<Int, StudentInfo>() {

    private var lastDocumentSnapshot: DocumentSnapshot? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StudentInfo> {
        try {
            val pageSize = 5
            val currentPage = params.key ?: STARTING_KEY
            val baseQuery = db.collection("students")
                .whereArrayContainsAny("availableLocal", selectedLocation)
                .limit(pageSize.toLong())

            val pageQuery = lastDocumentSnapshot?.let {
                baseQuery.startAfter(it)
            } ?: baseQuery

            val querySnapshot = pageQuery.get().await()
            if (!querySnapshot.isEmpty) {
                lastDocumentSnapshot = querySnapshot.documents.lastOrNull()
            }



            val matchedStudents = querySnapshot.documents.mapNotNull { document ->
                document.toObject(StudentInfo::class.java)?.takeIf { student ->
                    student.subject?.any { subject -> selectedSubject.contains(subject) } == true &&
                            (student.gender == gender || gender == "상관 없음")
                }
            }
            Log.d("PagingSource", "Page: $currentPage, Loaded: ${matchedStudents.size}")


            val nextKey = if (matchedStudents.size < pageSize) null else currentPage + 1
            return LoadResult.Page(
                data = matchedStudents,
                prevKey = if (currentPage == STARTING_KEY) null else currentPage - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Log.e("버그", "StudentPagingSource - Error loading data", e)
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StudentInfo>): Int? {
        return null
    }
}