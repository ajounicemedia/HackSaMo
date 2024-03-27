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
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentFindListBinding
import god.example.god_of_teaching.model.dataclass.TeacherInfo
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.view.nav.find.adapter.TeacherPagingListAdapter
import god.example.god_of_teaching.viewmodel.ChatViewModel
import god.example.god_of_teaching.viewmodel.FindViewModel
import god.example.god_of_teaching.viewmodel.MyInfoViewModel
import god.example.god_of_teaching.viewmodel.WishListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//선생님 필터링해서 보여주기
@AndroidEntryPoint
class ShowTeacherFragment : Fragment() {
   private var _binding: FragmentFindListBinding?=null
    private val binding get() = _binding!!
    private val findView : FindViewModel by activityViewModels()
    private val wishListViewModel : WishListViewModel by activityViewModels()
    private val myInfoView: MyInfoViewModel by viewModels()
    private val chatView : ChatViewModel by viewModels()

    private var myWishList : List<TeacherInfo>?=null

    //다시 화면 돌아와도 볼 수 있게
//    override fun onResume() {
//        super.onResume()
//        createAdapter()
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wishListViewModel.loadTeacherWishList()

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
        observeWishlist()
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
        binding.rvListAdapter.layoutManager = LinearLayoutManager(context)
        val teacherPagingListAdapter = TeacherPagingListAdapter(
            {teacher ->
                //사진 빨리 업로드하기 위해서
                findView.teacherKey?.value = teacher
                chatView.getOtherProfileImage("teachers",teacher.uid!!)
                chatView.uri?.observe(viewLifecycleOwner) { uri ->
                    if(uri!="null")
                    {
                        val bundle = Bundle().apply {
                            putString("teacherImageUrl", uri.toString())
                        }
                        val navController = findNavController()
                        navController.navigate(R.id.detailTeacherInfoFragment, bundle)
                    }
                else//상대방 프사 없을시
                {
                        findView.teacherKey?.value = teacher
                        val navController = findNavController()
                        navController.navigate(R.id.detailTeacherInfoFragment)
                }
            }},
            {
                myInfoView.uid.observe(viewLifecycleOwner) {myUid->
                    if(it.uid!=myUid)//자기 자신 위시리스트에 추가 못하게
                    {
                        wishListViewModel.uploadTeacherWishList(it)
                    }
                    else{
                            Toast.makeText(context, "자기 자신을 위시리스트에 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            {
                wishListViewModel.deleteTeacherWishList(it)
            },
            myWishList
        )
        binding.rvListAdapter.adapter = teacherPagingListAdapter
        CoroutineScope(Dispatchers.IO).launch {
            findView.pagedTeachers.collect(){
                teacherPagingListAdapter.submitData(it)
            }
        }
    }
    //위시리스트 관찰
    private fun observeWishlist()
    {
        wishListViewModel.myTeacherWishList.observe(viewLifecycleOwner) { teacherList ->

            myWishList = teacherList
            createAdapter()
        }
    }



}