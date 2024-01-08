package com.example.god_of_teaching.view.nav.find.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.god_of_teaching.R
import com.example.god_of_teaching.databinding.FragmentFindListBinding
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.dataclass.TeacherInfo
import com.example.god_of_teaching.model.`object`.util.MenuUtil
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.view.nav.find.adapter.TeacherPagingListAdapter
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

//선생님 필터링해서 보여주기
@AndroidEntryPoint
class ShowTeacherFragment : Fragment() {
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
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarShowSearchedList,"검색된 선생님",binding.toolbarTitleShowSearchedList)
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //선생님 어댑터 생성
    private fun createAdapter()
    {
        var myWishList : List<TeacherInfo>?=null
        wishListViewModel.loadTeacherWishList()
        wishListViewModel.myTeacherWishList.observe(viewLifecycleOwner) { teacherList ->
            myWishList = teacherList
        }
        binding.rvListAdapter.layoutManager = LinearLayoutManager(context)

        val teacherPagingListAdapter = TeacherPagingListAdapter(
            {teacher ->
                //사진 빨리 업로드하기 위해서
                findView.teacherKey?.value = teacher
                val storageReference = Firebase.storage.reference.child("teachers/${teacher.uid}")
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val bundle = Bundle().apply {
                        putString("teacherImageUrl", uri.toString())
                    }
                    val navController = findNavController()
                    navController.navigate(R.id.detailTeacherInfoFragment, bundle)
                }.addOnFailureListener {//상대방 프사 없을시
                        findView.teacherKey?.value = teacher
                        val navController = findNavController()
                        navController.navigate(R.id.detailTeacherInfoFragment)
                    }
            },
            {
                val userDataStoreHelper = UserDataStoreHelper.getInstance(requireActivity())
                CoroutineScope(Dispatchers.IO).launch {
                    val myUid = userDataStoreHelper.getUid.first()
                    if(it.uid!=myUid)//자기 자신 위시리스트에 추가 못하게
                    {
                        wishListViewModel.uplodeTeacherWishList(it)
                    }
                    else{
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "자기 자신을 위시리스트에 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            {
                wishListViewModel.deleteTeacherWishList(it)
            },
            myWishList
        )
        binding.rvListAdapter.adapter = teacherPagingListAdapter
        coroutineScope.launch {
            findView.pagedTeachers.collect(){
                teacherPagingListAdapter.submitData(it)
            }
        }
    }



}