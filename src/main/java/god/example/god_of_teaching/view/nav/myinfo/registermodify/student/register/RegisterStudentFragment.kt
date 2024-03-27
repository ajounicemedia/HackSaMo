package god.example.god_of_teaching.view.nav.myinfo.registermodify.student.register


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentRegisterStudentBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.view.nav.myinfo.registermodify.adapter.CustomArrayAdapter
import god.example.god_of_teaching.viewmodel.StudentViewModel
import java.io.File

//학생정보 입력하는 프래그먼트
@AndroidEntryPoint
class RegisterStudentFragment : Fragment() {

    private var _binding: FragmentRegisterStudentBinding?=null
    private val binding get() = _binding!!
    private val REQUEST_GALLERY = 1001
    private var gender: String? = null
    private var way: String? = null
    private var bornYear: String?= null
    private var resultUri:Uri?=null
    private val studentView : StudentViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterStudentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        waySpinner()
        genderSpinner()
        bornYearSpinner()
        bindProfilePicture()
        next()
    }
    //뷰 기본 설정
    private fun basicSetting()
    {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            navigationView.visibility = View.GONE
        }
        val navController = findNavController()
        binding.toolbarTitleRegisterStudent.text = "학생 등록하기"
        NavigationUtil.handleBackPressRegister(this,navController)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //프로필 사진 이미지뷰에 반영
    private fun bindProfilePicture()
    {
        binding.ivChoiceProfilePictureRegisterStudent.setOnClickListener {
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

    //사진 앱에 업로드
    private fun uploadPhoto()
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY)
    }
    //기본이미지인지 내가 고른 사진인지 선택
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
//기본 이미지로 다시 변경
    private fun resetToDefaultImage() {
        binding.ivChoiceProfilePictureRegisterStudent.setImageResource(R.drawable.teacher_profileimage)

        resultUri = null
    }


    //사진 편집하는 코드
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
            binding.ivChoiceProfilePictureRegisterStudent.scaleType = ImageView.ScaleType.FIT_CENTER
            binding.ivChoiceProfilePictureRegisterStudent.setImageURI(resultUri)
            binding.ivChoiceProfilePictureRegisterStudent.adjustViewBounds = true
        }
    }
    //수업 방식 선택
    private fun waySpinner()
    {
        val items = listOf("수업 방식", "대면", "비대면","대면,비대면")
        val adapter = context?.let { CustomArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, items) }
        binding.spinnerWayRegisterStudent.adapter = adapter
        binding.spinnerWayRegisterStudent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position == 0) {
                    way = null
                }

                else
                {
                    way = items[position]
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                way = null
            }
        }
    }
    //성별 선택
    private fun genderSpinner()
    {
        val items = listOf("성별", "남자", "여자")
        val adapter = context?.let { CustomArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, items) }
        binding.spinnerGenderRegisterStudent.adapter = adapter
        binding.spinnerGenderRegisterStudent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position == 0) {
                    gender = null
                }
                else
                {
                    gender = items[position]
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                gender = null
            }
        }
    }
    //학년 선택
    private fun bornYearSpinner()
    {
        val items = listOf("나이","사회인", "대학생", "n수생", "고3","고2","고1","중3","중2","중1","초6","초5","초4","초3","초2","초1","미취학 아동")
        val adapter = context?.let { CustomArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, items) }
        binding.spinnerBornYearRegisterStudent.adapter = adapter
        binding.spinnerBornYearRegisterStudent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position == 0) {
                    // 제목 항목이 선택된 경우 특별한 처리를 하지 않음
                    bornYear == null
                }
                else
                {
                    bornYear = items[position]
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                bornYear = null
            }
        }
    }
    //입력 정보 업로드 및 다음 프래그먼트로 이동
    private fun next()
    {
        binding.btnNextRegisterStudent.setOnClickListener {
            val navController = findNavController()
            if (resultUri != null) {
                context?.let { it1 -> studentView.uploadStudentProfileImage(resultUri!!, it1) }
            }
            val des = binding.etDesRegisterStudent.text.toString()
            if(des.isNotEmpty()&&gender!=null&&bornYear!=null&&way!=null)
            {
                studentView.uploadStudentInfo(des, bornYear!!, gender!!, way!!)

                navController.popBackStack()
                navController.navigate(R.id.studentLocalFragment)
            }
            else
            {
                Toast.makeText(context, "입력이 안 된 정보가 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}