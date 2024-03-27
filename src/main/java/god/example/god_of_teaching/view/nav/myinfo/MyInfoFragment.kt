package god.example.god_of_teaching.view.nav.myinfo


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentMyInfoBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.view.auth.LoginActivity
import god.example.god_of_teaching.viewmodel.AuthViewModel
import god.example.god_of_teaching.viewmodel.MyInfoViewModel


//내 정보 프래그먼트
@AndroidEntryPoint
class MyInfoFragment : Fragment() {
    private var _binding: FragmentMyInfoBinding? =null
    private val binding get() = _binding!!
    private val authView: AuthViewModel by viewModels()
    private val myInfoView: MyInfoViewModel by viewModels()
    private var uidValue: String? = null


    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_info, container, false)
        binding.vm = authView
        binding.lifecycleOwner=this


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController() // NavController 초기화

        basicSetting()
        goOtherFragment()
        observe()


    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    //뷰 기본 세팅
    private fun basicSetting() {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            if (navigationView.visibility == View.GONE) {
                navigationView.visibility = View.VISIBLE
            }
        }
        NavigationUtil.setupBackNavigationHome(this, navController)
    }
    //다른 프래그먼트로 이동
    private fun goOtherFragment()
    {


        //유저정보관리로이동
        binding.btnManageUserinfoMyinfo.setOnClickListener {
            navController.navigate(R.id.manageUserInfoFragment)
        }
        //선생님등록하기로이동
        binding.btnRegisterTeacherMyinfo.setOnClickListener {
            myInfoView.teacherCheck.observe(viewLifecycleOwner) { teacherCheck->
                if (teacherCheck!="true")
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
            myInfoView.studentCheck.observe(viewLifecycleOwner){studentCheck->
                    if (studentCheck!="true")
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
            myInfoView.academyCheck.observe(viewLifecycleOwner){academyCheck->
                if (academyCheck!="true")
                {
                        navController.navigate(R.id.registerAcademyFragment)
                } else {
                        Snackbar.make(binding.root, "이미 등록하셨습니다", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        //선생님정보관리로이동
        binding.btnManageTeacherMyinfo.setOnClickListener {
            myInfoView.teacherCheck.observe(viewLifecycleOwner) { teacherCheck->
                if (teacherCheck=="true") {
                    val bundle = Bundle()
                    bundle.putString("uid", uidValue)
                    navController.navigate(R.id.manageTeacherProfileFragment, bundle)
                } else {
                    Snackbar.make(binding.root, "선생님 등록이 되지 않으셨습니다", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        //학생정보관리로이동
        binding.btnManageStudentMyinfo.setOnClickListener {
            myInfoView.studentCheck.observe(viewLifecycleOwner) { studentCheck ->
                if (studentCheck == "true") {
                    val bundle = Bundle()
                    bundle.putString("uid", uidValue)
                    navController.navigate(R.id.modifyStudentProfileFragment, bundle)
                } else {
                    Snackbar.make(binding.root, "학생 등록이 되지 않으셨습니다", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        //학원정보관리로이동
        binding.btnManageAcademyMyinfo.setOnClickListener {
            myInfoView.academyCheck.observe(viewLifecycleOwner){academyCheck->
                if (academyCheck=="true")
                    {
                            val bundle = Bundle()
                            bundle.putString("uid", uidValue)
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
            navController.navigate(R.id.manageUserStatusFragment)
        }
        //로그아웃
        binding.btnLogoutMyinfo.setOnClickListener {
            authView.logout()
        }
        //차단 계정 관리로 이동
        binding.btnManageBlackAccountMyinfo.setOnClickListener {
            navController.navigate(R.id.manageBlackListFragment)
        }
        //알림 설정으로 이동
        binding.btnNotifySetMyinfo.setOnClickListener {
            val intent = Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            }
            startActivity(intent)
        }
        //계정 삭제로 이동
        binding.btnDeleteAccountMyinfo.setOnClickListener {
           navController.navigate(R.id.inputPasswordForDeleteAccount)
        }
    }


    //로그아웃 관찰
    private fun observe() {
        authView.logoutCheck.observe(viewLifecycleOwner){isLoggedOut ->
            if (isLoggedOut) {
                val currentActivity = activity
                if (currentActivity != null) {
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }
        myInfoView.uid.observe(viewLifecycleOwner){uid->
            uidValue=uid
        }
    }

//    //캐시 삭제 함수
//    private suspend fun deleteCache()
//    {

//    }




}