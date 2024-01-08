package com.example.god_of_teaching.view.nav.find.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.god_of_teaching.R
import com.example.god_of_teaching.databinding.FragmentFindListBinding
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.dataclass.StudentInfo
import com.example.god_of_teaching.model.`object`.util.MenuUtil
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.view.nav.find.adapter.StudentPagingListAdapter
import com.example.god_of_teaching.viewmodel.FindViewModel
import com.example.god_of_teaching.viewmodel.WishListViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ShowStudentFragment: Fragment() {
    private var _binding: FragmentFindListBinding?=null
    private val binding get() = _binding!!
    private val findView : FindViewModel by activityViewModels()
    private val wishListViewModel : WishListViewModel by activityViewModels()
    //코루틴 정지 시키기 위해서 선언함
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    //다시 화면 돌아와도 볼 수 있게
    override fun onResume() {
        super.onResume()
        createAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFindListBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
            createAdapter()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본세팅
    private fun basicSetting()
    {
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarShowSearchedList,"검색된 학생",binding.toolbarTitleShowSearchedList)
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //학생 리스트 생성
    private fun createAdapter()
    {
        var myWishList : List<StudentInfo>?=null
        wishListViewModel.loadStudentWishList()
        wishListViewModel.myStudentWishList.observe(viewLifecycleOwner) { studentList ->
            myWishList = studentList
        }
            binding.rvListAdapter.layoutManager = LinearLayoutManager(context)

            val studentPagingListAdapter = StudentPagingListAdapter(
                {student ->
                    findView.studentKey?.value = student
                    val storageReference = Firebase.storage.reference.child("students/${student.uid}")
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val bundle = Bundle().apply {
                            putString("studentImageUrl", uri.toString())
                        }
                        val navController = findNavController()
                        navController.navigate(R.id.detailStudentInfoFragment,bundle)
                    }.addOnFailureListener {
                        findView.studentKey?.value = student
                        val navController = findNavController()
                        navController.navigate(R.id.detailStudentInfoFragment)
                    }},
                {
                    val userDataStoreHelper = UserDataStoreHelper.getInstance(requireActivity())
                    CoroutineScope(Dispatchers.IO).launch {
                        val myUid = userDataStoreHelper.getUid.first()
                        if(it.uid!=myUid)//자기 자신 위시리스트에 추가 못하게
                        {
                            wishListViewModel.uplodeStudentWishList(it)
                        }
                        else{
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "자기 자신을 위시리스트에 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                {
                    wishListViewModel.deleteStudentWishList(it)
                },
                myWishList
            )

        binding.rvListAdapter.adapter = studentPagingListAdapter
        coroutineScope.launch {
            findView.pagedStudents.collect(){
                studentPagingListAdapter.submitData(it)
            }
        }
    }
}