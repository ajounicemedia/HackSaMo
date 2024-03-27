package god.example.god_of_teaching.view.nav.chat.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.databinding.FragmentTeacherInfoBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.StudentToTeacherChatViewModel

//채팅창에서 선생님 정보 보기
@AndroidEntryPoint
class TeacherInfoFragment: Fragment() {
    private var _binding: FragmentTeacherInfoBinding?=null
    private val teacherChatView : StudentToTeacherChatViewModel by viewModels()
    private val binding get() = _binding!!
    private var otherUid : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otherUid = arguments?.getString("other_uid")

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeacherInfoBinding.inflate(inflater,container,false)



        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        loadProfileImage()
        otherUid?.let { teacherChatView.getTeacherInfo(it) }
        uiSetting()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarFixTeacherInfo,"선택된 선생님 정보",binding.toolbarTitleFixTeacherInfo)
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
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
    //선생님 정보 ui에 할당
    private fun uiSetting()
    {
        teacherChatView.teacherInfo?.observe(viewLifecycleOwner) { teacherInfo ->

            binding.tvMyNicknameFixTeacherInfo.text = teacherInfo?.nickname
            binding.tvMyDesFixTeacherInfo.text = teacherInfo?.des
            binding.tvMyBornYearFixTeacherInfo.text=teacherInfo?.bornYear
            binding.tvMyGenderFixTeacherInfo.text = teacherInfo?.gender
            binding.tvMyAbililtyFixTeacherInfo.text = teacherInfo?.status
            binding.tvMyWayFixTeacherInfo.text = teacherInfo?.way
            binding.tvMyLocalFixTeacherInfo.text = teacherInfo?.availableLocal.toString()
            binding.tvMySubjectFixTeacherInfo.text = teacherInfo?.subject.toString()
            binding.tvMyIntroduceFixTeacherInfo.text = teacherInfo?.introduce
            binding.tvMyStatusFixTeacherInfo.text = teacherInfo?.status
        }
    }

}