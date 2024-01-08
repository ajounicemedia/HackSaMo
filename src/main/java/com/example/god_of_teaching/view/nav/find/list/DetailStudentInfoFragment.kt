package com.example.god_of_teaching.view.nav.find.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.god_of_teaching.R
import com.example.god_of_teaching.databinding.FragmentDetailStudentInfoBinding
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.dataclass.StudentInfo
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
class DetailStudentInfoFragment : Fragment() {
    private var _binding: FragmentDetailStudentInfoBinding?=null
    private val binding get() = _binding!!
    private val findView: FindViewModel by activityViewModels()
    private var studentKey: StudentInfo? = null
    private val wishListViewModel : WishListViewModel by activityViewModels()
    private var myWishList : List<StudentInfo>?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailStudentInfoBinding.inflate(inflater,container,false)
        studentKey = findView.studentKey?.value
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
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarFixStudentInfo,"선택된 학생 정보",binding.toolbarTitleFixStudentInfo)
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //학생 상세 정보 보이게하기
    private fun uiSetting()
    {
        binding.tvMyNicknameFixStudentInfo.text = studentKey?.nickname
        binding.tvMyDesFixStudentInfo.text = studentKey?.des
        binding.tvMyBornYearFixStudentInfo.text = studentKey?.bornYear
        binding.tvMyGenderFixStudentInfo.text = studentKey?.gender
        binding.tvMyWayFixStudentInfo.text = studentKey?.way
        binding.tvMyLocalFixStudentInfo.text =studentKey?.availableLocal.toString()
        binding.tvMySubjectFixStudentInfo.text = (studentKey?.subject).toString()
        binding.tvMyIntroduceFixStudentInfo.text =studentKey?.introduce

    }
    //프사 불러오기
    private fun loadProfileImage()
    {
        val imageUrl = arguments?.getString("studentImageUrl") ?: return
        Glide.with(this)
            .load(imageUrl)
            .transform(CircleCrop()) // CircleCrop 변환 적용
            .into(binding.ivProfileImgFixStudentInfo)
    }

    //기존에 찜목록에 있었는지 확인
    private fun checkWishList()
    {
        myWishList = wishListViewModel.myStudentWishList.value
        if(myWishList?.any { it.uid == studentKey?.uid } == true)
        {
            binding.ivWishlistDetailStudent.setImageResource(R.drawable.redheart)
        }
    }
    //채팅방가기
    private fun goChat()
    {

        binding.ivWishlistDetailStudent.setOnClickListener{
            val userDataStoreHelper = UserDataStoreHelper.getInstance(requireActivity())
            viewLifecycleOwner.lifecycleScope.launch {
                val myUid = userDataStoreHelper.getUid.first()
                if(studentKey?.uid !=myUid)//자기 자신에게 채팅 못 보내게
                {
                    val navController = findNavController()
                    val bundle = Bundle()
                    bundle.putString("other_name",studentKey?.nickname)
                    bundle.putString("other_uid",studentKey?.uid)
                    navController.navigate(R.id.studentChatRoomFragment,bundle)
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

        binding.ivWishlistDetailStudent.setOnClickListener {
            val currentImage = binding.ivWishlistDetailStudent.drawable.constantState?.newDrawable()
            //위시리스트에 안 담겼을 때
            if(currentImage?.constantState?.equals(binding.root.context.getDrawable(R.drawable.blackheart)?.constantState) == true)
            {
                val userDataStoreHelper = UserDataStoreHelper.getInstance(requireActivity())
                CoroutineScope(Dispatchers.IO).launch {
                    val myUid = userDataStoreHelper.getUid.first()
                    if(studentKey?.uid !=myUid)//자기 자신 위시리스트에 추가 못하게
                    {
                            binding.ivWishlistDetailStudent.setImageResource(R.drawable.redheart)
                            studentKey?.let { it1 -> wishListViewModel.uplodeStudentWishList(it1) }
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
                binding.ivWishlistDetailStudent.setImageResource(R.drawable.blackheart)
                studentKey?.let { it1 -> wishListViewModel.deleteStudentWishList(it1) }
            }
        }
    }

}