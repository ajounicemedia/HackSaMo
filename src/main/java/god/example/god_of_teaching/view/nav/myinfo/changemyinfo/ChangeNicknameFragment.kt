package god.example.god_of_teaching.view.nav.myinfo.changemyinfo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.databinding.FragmentChangeNicknameBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.AuthViewModel
import god.example.god_of_teaching.viewmodel.MyInfoViewModel


//닉네임 변경 프래그먼트
@AndroidEntryPoint
class ChangeNicknameFragment : Fragment() {
    private lateinit var nickname:String
    private var _binding: FragmentChangeNicknameBinding?=null
    private val binding get() = _binding!!
    private val myInfoView: MyInfoViewModel by viewModels()
    private val authView: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChangeNicknameBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()

        changeNickname()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarChangeNickname,"닉네임 변경",binding.toolbarTitleChangeNickname)
        NavigationUtil.handleBackPress(this,navController)
    }
    //닉네임 변경
    private fun changeNickname() {
        binding.btnChangeNicknameChangeNickname.setOnClickListener {
            nickname = binding.etInputNicknameChangeNickname.text.toString()
            if (nickname.isEmpty()) {
                Toast.makeText(context, "닉네임이 입력되지 않았습니다", Toast.LENGTH_SHORT).show()
            }
            else if(nickname.length>11)
            {
                Toast.makeText(context,"닉네임은 10자 이하로 설정 가능합니다.",Toast.LENGTH_SHORT).show()
            }
            else {
                authView.checkSameNickname(nickname)
                observeNicknameSame()

            }
        }
    }
    //닉네임 중복, 변경 체크
    private fun observeNicknameSame() {
        authView.nicknameSameCheck.observe(viewLifecycleOwner) { nicknameSameCheck ->
            when (nicknameSameCheck)
            {
                true->
                {
                    myInfoView.changeNickname(nickname)
                    observeNicknameChange()
                }
                false->
                {
                    Toast.makeText(context, "중복되어 사용이 불가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    authView.resetCheckSameNickname()
                }
                null->
                {

                }
            }
        }
    }
    private fun observeNicknameChange() {
        myInfoView.nicknameChangeCheck.observe(viewLifecycleOwner) { check ->
            when(check)
            {
                true->
                {
                    myInfoView.changeNicknameLocal(nickname)
                    Toast.makeText(context, "닉네임 변경에 성공했습니다", Toast.LENGTH_SHORT).show()
                    val navController = findNavController()
                    navController.popBackStack()
                }
                false->
                {
                    Toast.makeText(context, "모종의 이유로 닉네임 변경에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
                null->
                {

                }
            }

        }
    }

}