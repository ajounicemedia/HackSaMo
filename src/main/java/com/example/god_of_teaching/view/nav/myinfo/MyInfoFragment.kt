package com.example.god_of_teaching.view.nav.myinfo


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.god_of_teaching.R
import com.example.god_of_teaching.databinding.FragmentMyInfoBinding
import com.example.god_of_teaching.model.`object`.util.NavigationUtil
import com.example.god_of_teaching.model.datastore.AcademyDatastoreHelper
import com.example.god_of_teaching.model.datastore.StudentDatastoreHelper
import com.example.god_of_teaching.model.datastore.TeacherDataStoreHelper
import com.example.god_of_teaching.model.datastore.UserDataStoreHelper
import com.example.god_of_teaching.view.auth.LoginActivity
import com.example.god_of_teaching.viewmodel.AuthViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//내 정보 프래그먼트
@AndroidEntryPoint
class MyInfoFragment : Fragment() {
    private var _binding: FragmentMyInfoBinding? =null
    private val binding get() = _binding!!
    private val authView: AuthViewModel by viewModels()

    private var uidValue: String? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onResume() {
        super.onResume()
        checkUid()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_info, container, false)
        binding.vm = authView
        binding.lifecycleOwner=this
        checkUid()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basicSetting()
        goOtherFragment()
        observeLogoutEvent()


    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    //선생님, 학생, 학원 등록됐는지 체크
    private fun checkUid() {
        //uid가져오기

        viewLifecycleOwner.lifecycleScope.launch {
            // UID 가져오기
                val userDataStoreHelper = UserDataStoreHelper.getInstance(requireActivity())
                uidValue= userDataStoreHelper.getUid.first()
        }
    }
    //뷰 기본 세팅
    private fun basicSetting() {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            if (navigationView.visibility == View.GONE) {
                navigationView.visibility = View.VISIBLE
            }
        }
        val navController = findNavController()
        NavigationUtil.setupBackNavigationHome(this, navController)
    }
    //다른 프래그먼트로 이동
    private fun goOtherFragment()
    {
        val navController = findNavController()

        //유저정보관리로이동
        binding.btnManageUserinfoMyinfo.setOnClickListener {
            navController.navigate(R.id.manageUserInfoFragment)
        }
        //선생님등록하기로이동
        binding.btnRegisterTeacherMyinfo.setOnClickListener {
            //선생님 체크
            viewLifecycleOwner.lifecycleScope.launch {
                val teacherDataStoreHelper = TeacherDataStoreHelper.getInstance(requireActivity())
                val value = teacherDataStoreHelper.getIntroduce.first()
                    if (value=="default")
                    {
                            navController.navigate(R.id.registerTeacherFragment)
                    }
                    else
                    {
                            Snackbar.make(binding.root, "이미 등록하셨습니다", Snackbar.LENGTH_SHORT).show()
                    }
            }
        }
        //학생등록하기로이동
        binding.btnCertifyStudentMyinfo.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val studentDataStoreHelper = StudentDatastoreHelper.getInstance(requireActivity())
                val value = studentDataStoreHelper.getIntroduce.first()
                    if(value=="default")
                    {
                            navController.navigate(R.id.registerStudentFragment)
                    }
                    else{
                            Snackbar.make(binding.root, "이미 등록하셨습니다", Snackbar.LENGTH_SHORT).show()
                    }
            }
        }
        //학원등록하기로이동
        binding.btnRegisterAcademyMyinfo.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val academyDataStoreHelper = AcademyDatastoreHelper.getInstance(requireActivity())
                val value = academyDataStoreHelper.getIntroduce.first()
                if (value=="default")
                {
                        navController.navigate(R.id.registerAcademyFragment)
                } else {
                        Snackbar.make(binding.root, "이미 등록하셨습니다", Snackbar.LENGTH_SHORT).show()
                }
            }

        }
        //선생님정보관리로이동
        binding.btnManageTeacherMyinfo.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val teacherDataStoreHelper = TeacherDataStoreHelper.getInstance(requireActivity())
                val value = teacherDataStoreHelper.getIntroduce.first()
                if (value!="default")
                    {
                            val bundle = Bundle()
                            bundle.putString("uid", uidValue)
                            val navController = findNavController()
                            navController.navigate(R.id.manageTeacherProfileFragment, bundle)
                    }
                    else
                    {
                            Snackbar.make(binding.root, "선생님 등록이 되지 않으셨습니다", Snackbar.LENGTH_SHORT).show()
                    }
                }

        }
        //학생정보관리로이동
        binding.btnManageStudentMyinfo.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val studentDataStoreHelper = StudentDatastoreHelper.getInstance(requireActivity())
                val value = studentDataStoreHelper.getIntroduce.first()
                if (value!="default")
                    {
                            val bundle = Bundle()
                            bundle.putString("uid", uidValue)
                            val navController = findNavController()
                            navController.navigate(R.id.modifyStudentProfileFragment, bundle)
                    }
                    else
                    {
                            Snackbar.make(binding.root, "학생 등록이 되지 않으셨습니다", Snackbar.LENGTH_SHORT).show()
                    }
                }

        }
        //학원정보관리로이동
        binding.btnManageAcademyMyinfo.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                    val academyDataStoreHelper = AcademyDatastoreHelper.getInstance(requireActivity())
                    val value = academyDataStoreHelper.getIntroduce.first()
                    if (value!="default")
                    {
                            val bundle = Bundle()
                            bundle.putString("uid", uidValue)
                            val navController = findNavController()
                            navController.navigate(R.id.modifyAcademyProfileFragment, bundle)
                    }
                    else
                    {
                            Snackbar.make(binding.root, "학원 등록이 되지 않으셨습니다", Snackbar.LENGTH_SHORT).show()
                    }
            }
        }
        //유저 상태 관리로 이동
        binding.btnManageStatusMyinfo.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.manageUserStatusFragment)
        }
        //로그아웃
        binding.btnLogoutMyinfo.setOnClickListener {
            authView.logout()
        }
    }


    //로그아웃 관찰
    private fun observeLogoutEvent() {
        authView.logoutCheck.observe(requireActivity(), Observer { isLoggedOut ->
            if (isLoggedOut) {

                lifecycleScope.launch {
                    //캐시삭제
                    deleteCache()
                    val navController = findNavController()
                    navController.popBackStack()
                    // 모든 프래그먼트를 종료하고 LoginActivity로 이동
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        })
    }

    //캐시 삭제 함수
    private suspend fun deleteCache()
    {
        coroutineScope.launch {
                AcademyDatastoreHelper.getInstance(requireContext()).clearDataStore()
                StudentDatastoreHelper.getInstance(requireContext()).clearDataStore()
                TeacherDataStoreHelper.getInstance(requireContext()).clearDataStore()
                UserDataStoreHelper.getInstance(requireContext()).clearDataStore()
                coroutineScope.cancel()
        }.join()

    }
    //내 상태 선택




}