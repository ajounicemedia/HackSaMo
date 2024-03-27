package god.example.god_of_teaching.view.nav.myinfo.registermodify.teacher.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yalantis.ucrop.UCrop
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentUploadTeacherCertificateBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.viewmodel.TeacherViewModel
import java.io.File

class UploadTeacherCertificateFragment : Fragment() {
    private val REQUEST_GALLERY = 1001
    private var resultUri: Uri?=null
    private var _binding: FragmentUploadTeacherCertificateBinding?=null
    private val teacherView : TeacherViewModel by viewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUploadTeacherCertificateBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        uploadAcademyAuth()
        next()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun basicSetting()
    {
        val navController = findNavController()
        binding.toolbarTitleAuthAcademy.text = "선생님 인증하기"
        NavigationUtil.handleBackPressRegister(this,navController)
    }
    //갤러리 보여주기
    private fun uploadAcademyAuth()
    {
        binding.btnImgUplodeAuthAcademy.setOnClickListener {
            if(resultUri!=null)
            {
                showImageOptionPopup(it)
            }
            else
            {
                uploadPhoto()
            }
        }
    }
    //갤러리에서 선택한 사진 뷰에 반영
    private fun uploadPhoto()
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY)
    }
    //기본 이미지인지 아닌지 선택하는 메서드
    private fun showImageOptionPopup(view: View) {
        val popupMenu = context?.let { PopupMenu(it, view) }
        if (popupMenu != null) {
            popupMenu.menuInflater.inflate(R.menu.profile_image_options_menu, popupMenu.menu)
        }

        if (popupMenu != null) {
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.option_default_image -> {
                        resetToDefaultImage()
                        true
                    }
                    R.id.option_change_image -> {
                        uploadPhoto() // 기존의 이미지 업로드 메서드 호출
                        true
                    }
                    else -> false
                }
            }
        }
        if (popupMenu != null) {
            popupMenu.show()
        }
    }
    //갤러리 관련 코드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GALLERY && resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImageUri = data?.data
            //이미지 명 다르게해서 사진 재선택 가능하게하기
            val destinationUri = Uri.fromFile(File(context?.cacheDir, "cropped_${System.currentTimeMillis()}"))
            UCrop.of(selectedImageUri!!, destinationUri)
                .withAspectRatio(1f, 1f)  // 1:1 비율로 크롭
                .start(requireContext(), this)
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == AppCompatActivity.RESULT_OK) {
            resultUri = UCrop.getOutput(data!!)!!
            binding.ivAuthAcademy.scaleType = ImageView.ScaleType.FIT_CENTER
            binding.ivAuthAcademy.setImageURI(resultUri)
            binding.ivAuthAcademy.adjustViewBounds = true
        }
    }

    //이미지 뷰에 사진 반영
    private fun resetToDefaultImage() {
        binding.ivAuthAcademy.setImageDrawable(null)

        resultUri = null
    }
    //학원 등록증 사진 업로드 및 다음 프래그먼트로 이동
    private fun next() {
        binding.btnNextAuthAcademy.setOnClickListener {
            if (resultUri == null) {
                Toast.makeText(context, "이미지를 선택하지 않으셨습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val navController = findNavController()
                teacherView.uploadTeacherAuthImage(resultUri!!)
                navController.popBackStack()
                navController.navigate(R.id.choiceLocalFragment)
            }
//            val navController = findNavController()
//            navController.popBackStack()
//            navController.navigate(R.id.choiceLocalFragment)


        }
    }
}