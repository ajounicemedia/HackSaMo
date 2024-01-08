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
import com.example.god_of_teaching.databinding.FragmentDetailAcademyInfoBinding
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.dataclass.AcademyInfo
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
class DetailAcademyInfoFragment : Fragment() {
    private var _binding: FragmentDetailAcademyInfoBinding?=null
    private val binding get() = _binding!!
    private val findView: FindViewModel by activityViewModels()
    private var academyKey: AcademyInfo? = null
    private val wishListViewModel : WishListViewModel by activityViewModels()
    private var myWishList : List<AcademyInfo>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailAcademyInfoBinding.inflate(inflater,container,false)
        //findView = activityViewModels<FindViewModel>().value
        academyKey = findView.academyKey?.value
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
    //뷰 기본 세팅
    private fun basicSetting()
    {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            navigationView.visibility = View.GONE
        }
        loadProfileImage()
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarFixAcademyInfo,"선택된 학원 정보",binding.toolbarTitleFixAcademyInfo)
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //학원 상세 정보 표시
    private fun uiSetting()
    {
        binding.tvMyNameAcademyInfo.text = academyKey?.name
        binding.tvMyDesAcademyInfo.text = academyKey?.des
        binding.tvMyClassAgeAcademyInfo.text = academyKey?.des
        binding.tvMyIntroduceAcademyInfo.text = academyKey?.introduce
        binding.tvMyLocalAcademyInfo.text = academyKey?.local
        binding.tvMyDetailAddressFixAcademyInfo.text =academyKey?.detailAddress
        binding.tvMySubjectFixAcademyInfo.text = (academyKey?.subject).toString()

    }
    private fun loadProfileImage()
    {
//        val uid = academyKey?.uid
//        val storageReference = Firebase.storage.reference.child("academys").child("ZUBSVRXVjiWIA2QeVHYOLP8FXcG2")
//
//
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
        val imageUrl = arguments?.getString("academyImageUrl") ?: return
        Glide.with(this)
            .load(imageUrl)
            .transform(CircleCrop()) // CircleCrop 변환 적용
            .into(binding.ivProfileImgAcademyInfo)
    }

    //기존에 찜목록에 있었는지 확인
    private fun checkWishList()
    {
        myWishList = wishListViewModel.myAcademyWishList.value
        if(myWishList?.any { it.uid == academyKey?.uid } == true)
        {
            binding.ivWishlistDetailAcademy.setImageResource(R.drawable.redheart)
        }
    }
    //채팅방가기
    private fun goChat()
    {
        binding.ivWishlistDetailAcademy.setOnClickListener{
            val userDataStoreHelper = UserDataStoreHelper.getInstance(requireActivity())
            viewLifecycleOwner.lifecycleScope.launch {
                val myUid = userDataStoreHelper.getUid.first()
                if(academyKey?.uid !=myUid)//자기 자신에게 채팅 못 보내게
                {
                    val navController = findNavController()
                    val bundle = Bundle()
                    bundle.putString("other_name",academyKey?.name)
                    bundle.putString("other_uid",academyKey?.uid)
                    navController.navigate(R.id.academyChatRoomFragment,bundle)
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

        binding.ivWishlistDetailAcademy.setOnClickListener {
            val currentImage = binding.ivWishlistDetailAcademy.drawable.constantState?.newDrawable()
            //위시리스트에 안 담겼을 때
            if(currentImage?.constantState?.equals(binding.root.context.getDrawable(R.drawable.blackheart)?.constantState) == true)
            {
                val userDataStoreHelper = UserDataStoreHelper.getInstance(requireActivity())
                CoroutineScope(Dispatchers.IO).launch {
                    val myUid = userDataStoreHelper.getUid.first()
                    if(academyKey?.uid !=myUid)//자기 자신 위시리스트에 추가 못하게
                    {
                        binding.ivWishlistDetailAcademy.setImageResource(R.drawable.redheart)
                        academyKey?.let { it1 -> wishListViewModel.uplodeAcademyWishList(it1) }
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
                binding.ivWishlistDetailAcademy.setImageResource(R.drawable.blackheart)
                academyKey?.let { it1 -> wishListViewModel.uplodeAcademyWishList(it1) }
            }
        }
    }



}