package god.example.god_of_teaching.view.nav.myinfo.registermodify.teacher.register


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.univcert.api.UnivCert
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentAuthCampusBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//대학 인증 프래그먼트(api 이슈로 사용 중지)
class AuthCampusFragment : Fragment() {


    private val univCertKey: String by lazy {
        resources.getString(R.string.univ_cert_key)
    }
    private var _binding : FragmentAuthCampusBinding?=null
    private val binding get() = _binding!!
    private var email: String? = null
    private var campus:String?=null
    private var success: Boolean?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAuthCampusBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
            sendCode()
            checkCampus()
        certifyAndNext()
        //테스트용버튼 모든 정보 초기화
        binding.button4.setOnClickListener {
            clear()
        }



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        val navController = findNavController()
        NavigationUtil.handleBackPressRegister(this,navController)
        binding.toolbarTitleAuthCampus.text = "선생님 등록하기"
    }
    //모든 인증 정보 초기화(테스트 버튼)
    private fun clear()
    {
        GlobalScope.launch(Dispatchers.IO) {
            UnivCert.clear(univCertKey)
        }
    }
    //인증 가능한 대학인지 확인
    private fun checkCampus()
    {
        binding.btnCheckUniv.setOnClickListener {
            campus = binding.etInputUnivAuthCampus.text.toString()
            if (campus!!.isEmpty()) {
                Toast.makeText(context, "대학교를 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show()
            } else {

                GlobalScope.launch(Dispatchers.IO) {
                    val response = UnivCert.check(campus)
                    success = response["success"] as? Boolean
                    Log.d("인증 확인", success.toString())
                    withContext(Dispatchers.Main) {
                        binding.tvVisibleCampus.isVisible = true
                        if (success == true) {
                            binding.tvVisibleCampus.text = "                          인증 가능한 대학입니다"
                            binding.tvVisibleCampus.setTextColor(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.theme_color
                                )
                            )
                        } else {
                            binding.tvVisibleCampus.text = "                          인증 불가능한 대학입니다"
                            binding.tvVisibleCampus.setTextColor(Color.RED)
                        }
                    }

                }


            }
        }
        }
//이메일 보내기
    private fun sendCode()
    {
        binding.btnSendEmailAuthCampus.setOnClickListener {
            campus = binding.etInputUnivAuthCampus.text.toString()
            email = binding.etInputEmailAuthCampus.text.toString()
            if (email!!.isEmpty()) {
                Toast.makeText(context, "이메일을 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show()
            } else if (campus!!.isEmpty()) {
                Toast.makeText(context, "대학명이 비워져있습니다.", Toast.LENGTH_SHORT).show()
            } else {
                //이메일 대학교 조건
                GlobalScope.launch(Dispatchers.IO) {
                    UnivCert.certify(univCertKey, email, campus, false)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "인증번호가 발송되었습니다. 발송되지 않는다면 스팸 메일함을 확인하거나 메일을 제대로 입력했는지 확인해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }
    //인증번호확인 및 다음프래그먼트로 이동
    private fun certifyAndNext()
    {
        binding.btnNextAuthCampus.setOnClickListener {
//       val code = binding.etInputCodeAuthCampus.text.toString()
//        campus = binding.etInputUnivAuthCampus.text.toString()
//        email = binding.etInputEmailAuthCampus.text.toString()
//        if(campus!!.isEmpty())
//        {
//            Toast.makeText(context,"대학명이 비워져있습니다",Toast.LENGTH_SHORT).show()
//        }
//        else if(email!!.isEmpty())
//        {
//            Toast.makeText(context,"이메일이 비워져있습니다",Toast.LENGTH_SHORT).show()
//        }
//        else
//        {
//            if(code.isNotEmpty()) {
//                if(code.all { it.isDigit() }==false)
//                {
//                    Toast.makeText(context,"인증번호란에는 숫자만 입력해주세요",Toast.LENGTH_SHORT).show()
//                }
//                else{
//                    GlobalScope.launch(Dispatchers.IO) {
//                        UnivCert.certifyCode(univCertKey,email,campus,code.toInt())
//                        val response = UnivCert.check(campus)
//                        success = response["success"] as? Boolean
//                        withContext(Dispatchers.Main) {
//                            if(success==true)
//                            {
//                                Toast.makeText(context,"인증이 완료 되었습니다!",Toast.LENGTH_SHORT).show()
//                                val navController = findNavController()
//                                navController.popBackStack()
//                                navController.navigate(R.id.choiceLocalFragment)
//                            }
//                            else
//                            {
//                                Toast.makeText(context,"인증번호가 일치하지 않습니다. 다시 한 번 확인해주세요",Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                }
//
//            }
//            else
//            {
//                Toast.makeText(context,"인증번호를 입력하지 않았습니다",Toast.LENGTH_SHORT).show()
//            }
//        }
            val navController = findNavController()
            navController.popBackStack()
            navController.navigate(R.id.choiceLocalFragment)
        }
    }



}