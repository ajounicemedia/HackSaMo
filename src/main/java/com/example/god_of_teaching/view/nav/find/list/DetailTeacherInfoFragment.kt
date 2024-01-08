package com.example.god_of_teaching.view.nav.find.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.god_of_teaching.R
import com.example.god_of_teaching.databinding.FragmentDetailTeacherInfoBinding
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.dataclass.TeacherInfo
import com.example.god_of_teaching.model.`object`.util.MenuUtil
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.viewmodel.FindViewModel
import com.example.god_of_teaching.viewmodel.WishListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailTeacherInfoFragment : Fragment() {

    private var _binding: FragmentDetailTeacherInfoBinding?=null
    private val binding get() = _binding!!
    private val findView: FindViewModel by activityViewModels()
    private val wishListViewModel : WishListViewModel by activityViewModels()
    private var teacherKey: TeacherInfo?=null
    private var myWishList : List<TeacherInfo>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailTeacherInfoBinding.inflate(inflater,container,false)
        teacherKey = findView.teacherKey?.value

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        uiSetting()
        goChat()
        setWishList()
        checkWishList()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본세팅
    private fun basicSetting()
    {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            navigationView.visibility = View.GONE
        }
        loadProfileImage()
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarFixTeacherInfo,"선택된 선생님 정보",binding.toolbarTitleFixTeacherInfo)
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //선생님 상세 정보 보이게하기
    private fun uiSetting()
    {
        binding.tvMyNicknameFixTeacherInfo.text  = teacherKey?.nickname
        binding.tvMyDesFixTeacherInfo.text = teacherKey?.des
        binding.tvMyBornYearFixTeacherInfo.text = teacherKey?.bornYear
        binding.tvMyGenderFixTeacherInfo.text = teacherKey?.gender
        binding.tvMyAbililtyFixTeacherInfo.text = teacherKey?.status
        binding.tvMyWayFixTeacherInfo.text = teacherKey?.way
        binding.tvMyLocalFixTeacherInfo.text = teacherKey?.availableLocal.toString()
        binding.tvMySubjectFixTeacherInfo.text = teacherKey?.subject.toString()
        binding.tvMyIntroduceFixTeacherInfo.text = teacherKey?.introduce
        binding.tvMyStatusFixTeacherInfo.text = teacherKey?.status

    }
    //프사 불러오기
    private fun loadProfileImage()
    {
//        val uid = academyKey?.uid
//        val storageReference = Firebase.storage.reference.child("academys").child("ZUBSVRXVjiWIA2QeVHYOLP8FXcG2")
//        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
//            if(task.isSuccessful) {
//
//                Glide.with(this)
//                    .load(task.result)
//                    .transform(CircleCrop()) // CircleCrop 변환 적용
//                    .into(binding.ivProfileImgAcademyInfo)
//
//
//            } else {
//
//            }
//        })
        val imageUrl = arguments?.getString("teacherImageUrl") ?: return
        Glide.with(this)
            .load(imageUrl)
            .transform(CircleCrop()) // CircleCrop 변환 적용
            .into(binding.ivProfileImgFixTeacherInfo)
    }
    //기존에 찜목록에 있었는지 확인
    private fun checkWishList()
    {
        myWishList = wishListViewModel.myTeacherWishList.value
        if(myWishList?.any { it.uid == teacherKey?.uid } == true)
        {
            binding.ivWishlistDetailTeacher.setImageResource(R.drawable.redheart)
        }
    }
    //채팅방가기
    private fun goChat()
    {
        binding.btnChatDetailTeacher.setOnClickListener{
            val userDataStoreHelper = UserDataStoreHelper.getInstance(requireActivity())
            viewLifecycleOwner.lifecycleScope.launch {
                val myUid = userDataStoreHelper.getUid.first()
                if(teacherKey?.uid !=myUid)//자기 자신에게 채팅 못 보내게
                {
                    val navController = findNavController()
                    val bundle = Bundle()
                    bundle.putString("other_name",teacherKey?.nickname)
                    bundle.putString("other_uid",teacherKey?.uid)
                        navController.navigate(R.id.teacherChatRoomFragment, bundle)

                }
                else{
                        Toast.makeText(context, "자기 자신에게 채팅을 할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //위시리스트 설정
    private fun setWishList()
    {

        binding.ivWishlistDetailTeacher.setOnClickListener {
            val currentImage = binding.ivWishlistDetailTeacher.drawable.constantState?.newDrawable()
            //위시리스트에 안 담겼을 때
            if(currentImage?.constantState?.equals(binding.root.context.getDrawable(R.drawable.blackheart)?.constantState) == true)
            {
                val userDataStoreHelper = UserDataStoreHelper.getInstance(requireActivity())
                CoroutineScope(Dispatchers.IO).launch {
                    val myUid = userDataStoreHelper.getUid.first()
                    if(teacherKey?.uid !=myUid)//자기 자신 위시리스트에 추가 못하게
                    {
                        binding.ivWishlistDetailTeacher.setImageResource(R.drawable.redheart)
                        teacherKey?.let { it1 -> wishListViewModel.uplodeTeacherWishList(it1) }
                    }
                    else{
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "자기 자신을 위시리스트에 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            else
            {
                binding.ivWishlistDetailTeacher.setImageResource(R.drawable.blackheart)
                teacherKey?.let { it1 -> wishListViewModel.deleteTeacherWishList(it1) }
            }
        }
    }

}