package god.example.god_of_teaching.view.nav.myinfo.registermodify.teacher.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentRegisterTeacherBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.view.nav.myinfo.registermodify.adapter.CustomArrayAdapter
import god.example.god_of_teaching.viewmodel.TeacherViewModel
import java.io.File

//선생님 정보 입력받는 프래그먼트
@AndroidEntryPoint
class RegisterTeacherFragment : Fragment() {

    private var _binding: FragmentRegisterTeacherBinding?=null
    private val binding get() = _binding!!
    private val REQUEST_GALLERY = 1001
    private var gender: String? = null
    private var status: String? = null
    private var way: String? = null
    private var bornYear: String?= null
    private val teacherView : TeacherViewModel by viewModels()
    private var resultUri:Uri?=null

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterTeacherBinding.inflate(inflater,container,false)
        Log.d("3213211", Firebase.auth.currentUser?.uid.toString())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        waySpinner()
        genderSpinner()
        statusSpinner()
        bornYearSpinner()
        next()
        bindProfilePicture()
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
            navigationView.visibility = View.GONE
        }
        val navController = findNavController()
        NavigationUtil.handleBackPressRegister(this,navController)
        binding.toolbarTitleRegisterTeacher.text = "선생님 등록하기"
    }
    //선택한사진 이미지뷰에 반영
    private fun bindProfilePicture()
    {
        binding.ivChoiceProfilePictureRegisterTeacher.setOnClickListener {
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
    //기본프로필인지 내가 고른 사진인지 판별하는 코드
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
    //기본 이미지로 변경하는 코드
    private fun resetToDefaultImage() {
        binding.ivChoiceProfilePictureRegisterTeacher.setImageResource(R.drawable.teacher_profileimage)

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
            binding.ivChoiceProfilePictureRegisterTeacher.scaleType = ImageView.ScaleType.FIT_CENTER
            binding.ivChoiceProfilePictureRegisterTeacher.setImageURI(resultUri)
            binding.ivChoiceProfilePictureRegisterTeacher.adjustViewBounds = true
        }
    }
    //수업 방식 스피너
    private fun waySpinner()
    {
        val items = listOf("수업 방식", "대면", "비대면","대면,비대면")
        val adapter = context?.let { CustomArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, items) }
        binding.spinnerWayRegisterTeacher.adapter = adapter
        binding.spinnerWayRegisterTeacher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
    //성별 선택 스피너
    private fun genderSpinner()
    {
        val items = listOf("성별", "남자", "여자")
        val adapter = context?.let { CustomArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, items) }
        binding.spinnerGenderRegisterTeacher.adapter = adapter

        binding.spinnerGenderRegisterTeacher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
    //학적 상태 선택 스피너
    private fun statusSpinner()
    {
        val items = listOf("학적 상태", "재학", "휴학","졸업")
        val adapter = context?.let { CustomArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, items) }
        binding.spinnerStatusRegisterTeacher.adapter = adapter

        binding.spinnerStatusRegisterTeacher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // 선택된 항목에 대한 처리
                if (position == 0) {
                    status = null
                } else {
                    status = items[position]
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>) {
                status = null
            }
        }
    }
    //출생년도 스피너
    private fun bornYearSpinner()
    {
        // 1. 2023년부터 1913년까지의 연도 리스트 생성
        val items = listOf("출생년도") + (2023 downTo 1913).map { it.toString() }

        val adapter = context?.let { CustomArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, items) }
        binding.spinnerBornYearRegisterTeacher.adapter = adapter
        binding.spinnerBornYearRegisterTeacher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
    //입력한 정보 업로드 및 다음 프래그먼트로 이동
    private fun next() {
        binding.btnNextRegisterTeacher.setOnClickListener {
            val des = binding.etDesRegisterTeacher.text.toString()
            val campus = binding.etUniversityRegisterTeacher.text.toString()
            val campusLocal = binding.etCampusLocalRegisterTeacher.text.toString()
            val major = binding.etCampusLocalRegisterTeacher.text.toString()
            if (des.isEmpty() || campus.isEmpty()
                || campusLocal.isEmpty() || major.isEmpty()
                || gender == null || status == null || way == null || bornYear == null
            )
            {
                Toast.makeText(context, "입력이 안 된 정보가 있습니다.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val navController = findNavController()
                teacherView.uploadTeacherInfo(
                    des, bornYear!!, gender!!, campus, campusLocal, major, status!!, way!!
                )
                if (resultUri != null) {
                    context?.let { it1 -> teacherView.uploadTeacherProfileImage(resultUri!!, it1) }
                }
                navController.popBackStack()
                navController.navigate(R.id.uploadTeacherCertificateFragment)
            }


        }

    }
}