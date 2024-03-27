package god.example.god_of_teaching.view.nav.mywishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentMyWishListBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.view.nav.mywishlist.adapter.MyStudentWishListAdapter
import god.example.god_of_teaching.viewmodel.FindViewModel
import god.example.god_of_teaching.viewmodel.WishListViewModel

@AndroidEntryPoint
class StudentWishListFragment : Fragment() {
    private var _binding: FragmentMyWishListBinding?=null
    private val binding get() = _binding!!
    private val wishListViewModel : WishListViewModel by activityViewModels()
    private val findView : FindViewModel by activityViewModels()
    private lateinit var studentListAdapter : MyStudentWishListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wishListViewModel.loadStudentWishList()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyWishListBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        createStudentAdapter()
        observeStudentWishList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰페이저 생성

    //뷰기본세팅
    private fun basicSetting()
    {

        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            if (navigationView.visibility == View.GONE) {
                navigationView.visibility = View.VISIBLE
            }
        }
        val navController = findNavController()
        NavigationUtil.setupBackNavigationHome(this, navController)
    }
    //학생 어댑터 생성
    private fun createStudentAdapter()
    {

        binding.rvWishlistMyWishlist.layoutManager = LinearLayoutManager(context)
        //사진 빨리 업로드하기 위해서
        studentListAdapter = MyStudentWishListAdapter({student ->
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
                wishListViewModel.deleteStudentWishList(it)

            }
        )
        // LiveData를 관찰하고 리스트 어댑터에 데이터를 제공

        binding.rvWishlistMyWishlist.adapter = studentListAdapter
    }
    private fun observeStudentWishList()
    {
        // LiveData를 관찰하고 리스트 어댑터에 데이터를 제공
        wishListViewModel.myStudentWishList.observe(viewLifecycleOwner){studentList ->
            studentListAdapter.submitList(studentList)
        }
    }
}