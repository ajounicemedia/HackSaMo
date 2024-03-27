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
import god.example.god_of_teaching.view.nav.mywishlist.adapter.MyTeacherWishListAdapter
import god.example.god_of_teaching.viewmodel.FindViewModel
import god.example.god_of_teaching.viewmodel.WishListViewModel

@AndroidEntryPoint
class TeacherWishListFragment : Fragment() {
    private var _binding: FragmentMyWishListBinding?=null
    private val binding get() = _binding!!
    private val wishListViewModel : WishListViewModel by activityViewModels()
    private val findView : FindViewModel by activityViewModels()
    private lateinit var teacherListAdapter : MyTeacherWishListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wishListViewModel.loadTeacherWishList()

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
        createTeacherAdapter()
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
    private fun createTeacherAdapter()
    {

        binding.rvWishlistMyWishlist.layoutManager = LinearLayoutManager(context)

         teacherListAdapter = MyTeacherWishListAdapter(
            { teacher ->
                //사진 빨리 업로드하기 위해서
                findView.teacherKey?.value = teacher
                val storageReference = Firebase.storage.reference.child("teachers/${teacher.uid}")
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val bundle = Bundle().apply {
                        putString("teacherImageUrl", uri.toString())
                    }
                    val navController = findNavController()
                    navController.navigate(R.id.detailTeacherInfoFragment,bundle)
                }.addOnFailureListener {
                    findView.teacherKey?.value = teacher
                    val navController = findNavController()
                    navController.navigate(R.id.detailTeacherInfoFragment)
                }
            },
            {
                wishListViewModel.deleteTeacherWishList(it)
            }
        )


        binding.rvWishlistMyWishlist.adapter = teacherListAdapter

    }
    private fun observeStudentWishList()
    {

        wishListViewModel.myTeacherWishList.observe(viewLifecycleOwner) { teacherList ->
            teacherListAdapter.submitList(teacherList)
        }
    }

}