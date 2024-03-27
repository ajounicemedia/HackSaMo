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
import god.example.god_of_teaching.databinding.FragmentDetailStudentInfoBinding
import god.example.god_of_teaching.model.dataclass.StudentInfo
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.FindViewModel
import god.example.god_of_teaching.viewmodel.MyInfoViewModel
import god.example.god_of_teaching.viewmodel.WishListViewModel

@AndroidEntryPoint
class DetailStudentInfoFragment : Fragment() {
    private var _binding: FragmentDetailStudentInfoBinding?=null
    private val binding get() = _binding!!
    private var studentKey: StudentInfo? = null
    private var myWishList : List<StudentInfo>?=null

    private val findView: FindViewModel by activityViewModels()
    private val wishListViewModel : WishListViewModel by activityViewModels()
    private val myInfoView: MyInfoViewModel by viewModels()


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

        binding.btnChatDetailStudent.setOnClickListener{
            myInfoView.uid.observe(viewLifecycleOwner) { myUid ->
                if (studentKey?.uid != myUid)//자기 자신에게 채팅 못 보내게
                {
                    goChatRoom()
                } else {
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

                myInfoView.uid.observe(viewLifecycleOwner) {myUid->

                    if(studentKey?.uid !=myUid)//자기 자신 위시리스트에 추가 못하게
                    {
                            binding.ivWishlistDetailStudent.setImageResource(R.drawable.redheart)
                            studentKey?.let { it1 -> wishListViewModel.uploadStudentWishList(it1) }
                    }
                    else {
                        Toast.makeText(context, "자기 자신을 위시리스트에 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
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

    //채팅방 들어가기
    private fun goChatRoom()
    {
        myInfoView.status.observe(viewLifecycleOwner) {myStatus->
            if(myStatus=="teacher") {
                val navController = findNavController()
                val bundle = Bundle()
                bundle.putString("other_name",studentKey?.nickname)
                bundle.putString("other_uid",studentKey?.uid)
                bundle.putString("my_chat_type", "teacher")
                bundle.putString("other_chat_type","student")
                bundle.putBoolean("first_chat",true)
                navController.navigate(R.id.teacherToStudentChatRoomFragment,bundle)
            }
            else{
                Toast.makeText(context, "선생님 상태에서만 학생에게 채팅을 보낼 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}