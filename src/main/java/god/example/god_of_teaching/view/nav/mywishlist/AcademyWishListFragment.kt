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
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentMyWishListBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.view.nav.mywishlist.adapter.MyAcademyWishListAdapter
import god.example.god_of_teaching.viewmodel.FindViewModel
import god.example.god_of_teaching.viewmodel.WishListViewModel

class AcademyWishListFragment: Fragment() {
    private var _binding: FragmentMyWishListBinding?=null
    private val binding get() = _binding!!
    private val wishListViewModel : WishListViewModel by activityViewModels()
    private val findView : FindViewModel by activityViewModels()
    private lateinit var academyListAdapter : MyAcademyWishListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wishListViewModel.loadAcademyWishList()
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
        createAcademyAdapter()
        observeAcademyWishList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
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
    //학원 어댑터 생성
    private fun createAcademyAdapter()
    {

        binding.rvWishlistMyWishlist.layoutManager = LinearLayoutManager(context)
        //이미지 딜레이 없애기 위해
        academyListAdapter = MyAcademyWishListAdapter({ academy->
            findView.academyKey?.value = academy
            val storageReference = Firebase.storage.reference.child("academies/${academy.uid}")
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val bundle = Bundle().apply {
                    putString("academyImageUrl", uri.toString())
                }
                val navController = findNavController()
                navController.navigate(R.id.detailAcademyInfoFragment,bundle)
            }.addOnFailureListener {
                findView.academyKey?.value = academy
                val navController = findNavController()
                navController.navigate(R.id.detailAcademyInfoFragment)
            }
        },
            {
                wishListViewModel.deleteAcademyWishList(it)
            }
        )


        binding.rvWishlistMyWishlist.adapter = academyListAdapter

    }
    private fun observeAcademyWishList()
    {
        wishListViewModel.myAcademyWishList.observe(viewLifecycleOwner) { academyList ->
            academyListAdapter.submitList(academyList)
        }
    }
}