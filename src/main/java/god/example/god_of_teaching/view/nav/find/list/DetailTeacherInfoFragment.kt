package god.example.god_of_teaching.view.nav.find.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentDetailTeacherInfoBinding
import god.example.god_of_teaching.model.dataclass.TeacherInfo
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.FindViewModel
import god.example.god_of_teaching.viewmodel.MyInfoViewModel
import god.example.god_of_teaching.viewmodel.WishListViewModel


@AndroidEntryPoint
class DetailTeacherInfoFragment : Fragment() {

    private var _binding: FragmentDetailTeacherInfoBinding?=null
    private val binding get() = _binding!!
    private var teacherKey: TeacherInfo?=null
    private var myWishList : List<TeacherInfo>?=null

    private val findView: FindViewModel by activityViewModels()
    private val wishListViewModel : WishListViewModel by activityViewModels()
    private val myInfoView: MyInfoViewModel by viewModels()


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

            myInfoView.uid.observe(viewLifecycleOwner) {myUid->

                if(teacherKey?.uid !=myUid)//자기 자신에게 채팅 못 보내게
                {
                    goChatRoom()
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

                myInfoView.uid.observe(viewLifecycleOwner) {myUid->

                    if(teacherKey?.uid !=myUid)//자기 자신 위시리스트에 추가 못하게
                    {
                        binding.ivWishlistDetailTeacher.setImageResource(R.drawable.redheart)
                        teacherKey?.let { it1 -> wishListViewModel.uploadTeacherWishList(it1) }
                    }
                    else{
                            Toast.makeText(context, "자기 자신을 위시리스트에 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
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
    //채팅방 들어가기
    private fun goChatRoom()
    {
        myInfoView.status.observe(viewLifecycleOwner) {myStatus->
            if(myStatus=="student") {
                val navController = findNavController()
                val bundle = Bundle()
                bundle.putString("other_name",teacherKey?.nickname)
                bundle.putString("other_uid",teacherKey?.uid)
                bundle.putString("my_chat_type","student")
                bundle.putString("other_chat_type","teacher")
                bundle.putBoolean("first_chat",true)
                navController.navigate(R.id.studentToTeacherChatRoomFragment, bundle)
            }
            else {
                Toast.makeText(context, "학생 상태에서만 선생님에게 채팅을 보낼 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }

}