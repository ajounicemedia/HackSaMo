package com.example.god_of_teaching.view.nav.myinfo.academy.register


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.god_of_teaching.R
import com.example.god_of_teaching.model.`object`.util.MenuUtil
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.`object`.util.ToolbarUtil
import com.example.god_of_teaching.databinding.FragmentFindListBinding
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.model.dataclass.AcademyInfo
import com.example.god_of_teaching.view.nav.find.adapter.AcademyPagingListAdapter
import com.example.god_of_teaching.viewmodel.FindViewModel
import com.example.god_of_teaching.viewmodel.WishListViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//학원 정보 업로드하는 프래그먼트
@AndroidEntryPoint
class ShowAcademyListFragment : Fragment() {
    private var _binding: FragmentFindListBinding?=null
    private val binding get() = _binding!!
    private val findView : FindViewModel by activityViewModels()
    private val wishListViewModel : WishListViewModel by activityViewModels()

    //코루틴 정지 시키기 위해서 선언함
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    //다시 화면 돌아와도 볼 수 있게
    override fun onResume() {
        super.onResume()
            createAdapter()
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
        createAdapter()

    }
    override fun onDestroyView() {
        super.onDestroyView()
      //  coroutineScope.cancel()
        _binding = null
    }
    //뷰 기본세팅
    private fun basicSetting()
    {
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarShowSearchedList,"검색된 학원",binding.toolbarTitleShowSearchedList)
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        NavigationUtil.handleBackPress(this,navController)
    }
    //학원 어댑터 사용
    private fun createAdapter()
    {
        var myWishList : List<AcademyInfo>?=null
        wishListViewModel.loadAcademyWishList()
        wishListViewModel.myAcademyWishList.observe(viewLifecycleOwner) { academyList ->
            myWishList = academyList
        }
        binding.rvListAdapter.layoutManager = LinearLayoutManager(context)
        //이미지 딜레이 없애기 위해
        val academyListAdapter = AcademyPagingListAdapter({ academy->
            findView.academyKey?.value = academy
            val storageReference = Firebase.storage.reference.child("academys/${academy.uid}")
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
                    val userDataStoreHelper = UserDataStoreHelper.getInstance(requireActivity())
                    CoroutineScope(Dispatchers.IO).launch {
                        val myUid = userDataStoreHelper.getUid.first()
                        if(it.uid!=myUid)//자기 자신 위시리스트에 추가 못하게
                        {
                            wishListViewModel.uplodeAcademyWishList(it)
                        }
                        else{
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "자기 자신을 위시리스트에 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }


                },
                {
                    wishListViewModel.deleteAcademyWishList(it)
                },
                     myWishList
                )

        binding.rvListAdapter.adapter = academyListAdapter
        coroutineScope.launch {
            findView.pagedAcademies.collect {
                academyListAdapter?.submitData(it)
            }

        }
    }
}

