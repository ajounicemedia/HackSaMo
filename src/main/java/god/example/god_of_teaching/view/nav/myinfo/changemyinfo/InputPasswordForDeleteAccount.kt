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
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentInputPasswordForFixMyinfoBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.MyInfoViewModel

//계정삭제를 위한 비밀번호 입력 프래그먼트
@AndroidEntryPoint
class InputPasswordForDeleteAccount : Fragment(){
    private var _binding:  FragmentInputPasswordForFixMyinfoBinding?=null
    private val binding get() = _binding!!
    private val myInfoView: MyInfoViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding= FragmentInputPasswordForFixMyinfoBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        checkPassword()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰기본세팅
    private fun basicSetting()
    {
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarInputPasswordForFixMyinfo,"비밀번호 입력",binding.toolbarTitleInputPasswordForFixMyinfo)
        NavigationUtil.handleBackPress(this,navController)
    }
    //비밀번호 확인
    private fun checkPassword()
    {
        binding.btnPasswordCheckForFixMyinfo.setOnClickListener {
            val password = binding.etInputPasswordForFixMyinfo.text.toString()
            if (password.isEmpty()) {
                Toast.makeText(context, "비밀번호를 입력하지 않으셨습니다", Toast.LENGTH_SHORT).show()
            } else {
                myInfoView.passwordCheck(password)
                observePasswordCheck()
            }

        }

    }
    //비밀번호 맞는지 확인
    private fun observePasswordCheck()
    {
        val navController = findNavController()
        myInfoView.passwordCheck.observe(viewLifecycleOwner){check->
            when(check)
            {
                true ->
                {
                    navController.popBackStack()
                    navController.navigate(R.id.deleteAccountFragment)
                }
                false ->
                {
                    Toast.makeText(context, "잘못된 비밀번호를 입력하셨습니다", Toast.LENGTH_SHORT).show()
                    myInfoView.resetPasswordCheck()
                }
                null ->
                {

                }
            }
        }
    }
}