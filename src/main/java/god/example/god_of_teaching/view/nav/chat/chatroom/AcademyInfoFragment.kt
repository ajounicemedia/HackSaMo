package god.example.god_of_teaching.view.nav.chat.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.databinding.FragmentAcademyInfoBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.StudentToAcademyChatViewModel

//채팅창에서 학원 보기
@AndroidEntryPoint
class AcademyInfoFragment: Fragment() {
    private var _binding: FragmentAcademyInfoBinding?=null
    private val academyChatView : StudentToAcademyChatViewModel by viewModels()
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
        _binding = FragmentAcademyInfoBinding.inflate(inflater,container,false)



        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        loadProfileImage()
        otherUid?.let { academyChatView.getAcademyInfo(it) }
        uiSetting()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarFixAcademyInfo,"선택된 선생님 정보",binding.toolbarTitleFixAcademyInfo)
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //프사 불러오기
    private fun loadProfileImage()
    {
        val imageUrl = arguments?.getString("academyImageUrl") ?: return
        Glide.with(this)
            .load(imageUrl)
            .transform(CircleCrop()) // CircleCrop 변환 적용
            .into(binding.ivProfileImgAcademyInfo)
    }
    //선생님 정보 ui에 할당
    private fun uiSetting()
    {
        academyChatView.academyInfo?.observe(viewLifecycleOwner, Observer { academyInfo ->
            binding.tvMyNameAcademyInfo.text = academyInfo.nickname
            binding.tvMyDesAcademyInfo.text = academyInfo.des
            binding.tvMyClassAgeAcademyInfo.text = academyInfo.des
            binding.tvMyIntroduceAcademyInfo.text = academyInfo.introduce
            binding.tvMyLocalAcademyInfo.text = academyInfo.local
            binding.tvMyDetailAddressFixAcademyInfo.text =academyInfo.detailAddress
            binding.tvMySubjectFixAcademyInfo.text = (academyInfo.subject).toString()
        })
    }
}