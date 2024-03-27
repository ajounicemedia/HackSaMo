package god.example.god_of_teaching.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import god.example.god_of_teaching.model.dataclass.AcademyInfo
import god.example.god_of_teaching.model.dataclass.StudentInfo
import god.example.god_of_teaching.model.dataclass.TeacherInfo
import god.example.god_of_teaching.model.repository.WishListRepository
import javax.inject.Inject
//위시리스트 뷰모델
@HiltViewModel
class WishListViewModel @Inject constructor
    (private val wishListRepository: WishListRepository,
     application: Application): AndroidViewModel(application)
{
        //wishListRepository - uploadAcademyWishList 호출
        fun uploadAcademyWishList(academy : AcademyInfo)
        {
            academy.uid?.let { wishListRepository.uploadAcademyWishList(it)}
        }
        //wishListRepository - deleteAcademyWishList 호출
        fun deleteAcademyWishList(academy : AcademyInfo)
        {
            academy.uid?.let { it1 -> wishListRepository.deleteAcademyWishList(it1) }
        }
         //wishListRepository - uploadStudentWishList 호출
        fun uploadStudentWishList(student : StudentInfo) {
            student.uid?.let { wishListRepository.uploadStudentWishList(it) }
        }
        //wishListRepository - deleteStudentWishList 호출
        fun deleteStudentWishList(student : StudentInfo)
        {
            student.uid?.let { it1 -> wishListRepository.deleteStudentWishList(it1)}
        }
        //wishListRepository - uploadTeacherWishList 호출
        fun uploadTeacherWishList(teacher : TeacherInfo)
        {
            teacher.uid?.let { wishListRepository.uploadTeacherWishList(it)}
        }
        //wishListRepository - deleteTeacherWishList 호출
        fun deleteTeacherWishList(teacher : TeacherInfo)
        {
            teacher.uid?.let { it1 -> wishListRepository.deleteTeacherWishList(it1) }
        }
        //wishListRepository - loadAcademyWishList 호출
        var myAcademyWishList = MutableLiveData<List<AcademyInfo>>()
        fun loadAcademyWishList()
        {
            myAcademyWishList=wishListRepository.loadAcademyWishList()
        }
        //wishListRepository - loadTeacherWishList 호출
        var myTeacherWishList = MutableLiveData<List<TeacherInfo>>()
        fun loadTeacherWishList()
        {
            myTeacherWishList =wishListRepository.loadTeacherWishList()
        }
        //wishListRepository - loadStudentWishList 호출
        var myStudentWishList = MutableLiveData<List<StudentInfo>>()
        fun loadStudentWishList()
        {
            myStudentWishList=wishListRepository.loadStudentWishList()
        }
}