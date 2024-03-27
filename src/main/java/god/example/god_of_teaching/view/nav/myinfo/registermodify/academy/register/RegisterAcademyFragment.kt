package god.example.god_of_teaching.view.nav.myinfo.registermodify.academy.register


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentRegisterAcademyBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.viewmodel.AcademyViewModel
import java.io.File

//학원 정보 업로드하는 프래그먼트
@AndroidEntryPoint
class RegisterAcademyFragment : Fragment() {
    private val REQUEST_GALLERY = 1001
    private var resultUri: Uri?=null
    private var _binding: FragmentRegisterAcademyBinding?=null
    private val binding get() = _binding!!
    private var classAge: MutableList<String>? = null
    private val academyView : AcademyViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterAcademyBinding.inflate(inflater,container,false)
        classAge = mutableListOf()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basicSetting()
            profilePicture()
            selectClassAge()
            uploadAcademyInfo()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun basicSetting()
    {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            navigationView.visibility = View.GONE
        }
        val navController = findNavController()
        binding.toolbarTitleRegisterAcademy.text = "학원 등록하기"
        NavigationUtil.handleBackPressRegister(this,navController)
    }
    //프로필 사진 설정하는 코드
    private fun profilePicture()
    {
        binding.ivChoiceProfilePictureRegisterAcademy.setOnClickListener {
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
    //사진 업로드
    private fun uploadPhoto()
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY)
    }
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
            binding.ivChoiceProfilePictureRegisterAcademy.scaleType = ImageView.ScaleType.FIT_CENTER
            binding.ivChoiceProfilePictureRegisterAcademy.setImageURI(resultUri)
            binding.ivChoiceProfilePictureRegisterAcademy.adjustViewBounds = true
        }
    }
    //기본이미지인지 선택한 이미지인지
    private fun showImageOptionPopup(view: View) {
        val popupMenu = context?.let { PopupMenu(it, view) }
        popupMenu?.menuInflater?.inflate(R.menu.profile_image_options_menu, popupMenu.menu)

        popupMenu?.setOnMenuItemClickListener { menuItem ->
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
        popupMenu?.show()
    }
    //기본이미지로 되돌리기
    private fun resetToDefaultImage() {
        binding.ivChoiceProfilePictureRegisterAcademy.setImageResource(R.drawable.teacher_profileimage)

        resultUri = null
    }
    //수업 연령 선택
    private fun selectClassAge()
    {
        binding.tvClassAgeRegisterAcademy.setOnClickListener {
            val items = listOf("사회인", "대학생", "n수생", "고등학생", "중학생", "초등학생", "미취학 아동")
            val checkedItems = BooleanArray(items.size) // 선택된 항목들을 저장할 배열

            // 이전에 선택한 항목들을 checkedItems 배열에 반영합니다.
            for (i in items.indices) {
                if (classAge?.contains(items[i]) == true) {
                    checkedItems[i] = true
                }
            }

            val builder = context?.let { AlertDialog.Builder(it, R.style.CustomAlertDialog) }

            builder?.setTitle("수업 대상")
            builder?.setMultiChoiceItems(
                items.toTypedArray(),
                checkedItems
            ) { _, which, isChecked ->
                checkedItems[which] = isChecked
                if (isChecked) {

                    classAge?.add(items[which]) // 선택된 항목을 classAge에 추가
                } else {
                    classAge?.remove(items[which]) // 선택 해제된 항목을 classAge에서 제거
                }
            }

            if (builder != null) {
                builder.setPositiveButton("확인") { dialog, _ ->
                    binding.tvClassAgeRegisterAcademy.text = "  " + classAge.toString()
                    dialog.dismiss()
                }
            }

            if (builder != null) {
                builder.setNegativeButton("취소") { dialog, _ ->
                    classAge?.clear()
                    dialog.dismiss()
                }
            }

            if (builder != null) {
                builder.show()
            }
        }
    }
    //다음 프래그먼트로 이동 및 학원정보 업로드
    private fun uploadAcademyInfo()
    {
        binding.btnNextRegisterAcademy.setOnClickListener {
            val des = binding.etDesRegisterAcademy.text.toString()

            if (des.isEmpty()||classAge == null) {
                Toast.makeText(context, "입력이 안 된 정보가 있습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val navController = findNavController()

                academyView.uploadAcademyInfo(des, classAge!!)
                if (resultUri != null) {
                    context?.let { it1 -> academyView.uploadAcademyProfileImage(resultUri!!, it1)}
                }
                navController.popBackStack()
                navController.navigate(R.id.authAcademyFragment)
            }
        }


    }

}