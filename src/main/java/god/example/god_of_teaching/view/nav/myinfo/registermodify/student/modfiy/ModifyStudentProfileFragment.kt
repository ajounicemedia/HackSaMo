package god.example.god_of_teaching.view.nav.myinfo.registermodify.student.modfiy

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentModifyStudentProfileBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.StudentViewModel
import java.io.File

//학생 정보 수정 프래그먼트
@AndroidEntryPoint
class ModifyStudentProfileFragment : Fragment() {
    private val REQUEST_GALLERY = 1001
    private var _binding: FragmentModifyStudentProfileBinding?=null
    private val binding get() = _binding!!
    private val studentView: StudentViewModel by viewModels()
    private var uid: String? = null//사진 받아오기 위해
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = arguments?.getString("uid")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_modify_student_profile,container,false)
        binding.vm = studentView
        binding.lifecycleOwner=this
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        modifyMyInfo()
        bindProfilePicture()
        pullPicture()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본 세팅
    private fun basicSetting()
    {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            navigationView.visibility = View.GONE
        }
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarFixStudentInfo,"학생 정보 수정",binding.toolbarTitleFixStudentInfo)
        NavigationUtil.handleBackPress(this,navController)
    }
    //변경할 항목 선택
    private fun modifyMyInfo()
    {
        //닉네임 변경
//        binding.tvModifyNicknameFixStudentInfo.setOnClickListener{
//            val navController = findNavController()
//            val bundle = Bundle()
//            bundle.putString("modified_item", "닉네임")
//            navController.navigate(R.id.modifyStudentInfoTextFragment,bundle)
//        }
        //설정 변경
        binding.tvModifyDesFixStudentInfo.setOnClickListener {
            val navController = findNavController()
            val bundle = Bundle()
            bundle.putString("modified_item", "한줄소개")
            navController.navigate(R.id.modifyStudentInfoTextFragment,bundle)
        }
        //수업 방식
        binding.tvModifyWayFixStudentInfo.setOnClickListener {
            wayDialog()
        }
        //지역
        binding.tvModifyLocalFixStudentInfo.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.modifyStudentLocalFragment)
        }
        //과목
        binding.tvModifySubjectFixStudentInfo.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.modifyStudentSubjectFragment)
        }
        //자기소개
        binding.tvModifyIntroduceFixStudentInfo.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.modifyStudentIntroduceFragment)
        }


    }
    //수업 방식 변경 선택
    private fun wayDialog()
    {
        val builder = AlertDialog.Builder(requireContext())
        val wayChoices = arrayOf("대면", "비대면", "대면,비대면")
        val customTitle = LayoutInflater.from(requireContext()).inflate(R.layout.text_dialog_title_color, null)
        val titleTextView = customTitle.findViewById<TextView>(R.id.customDialogTitle)
        titleTextView.text = "수업 방식"
        builder.setCustomTitle(customTitle)

        builder.setItems(wayChoices) { _, which ->
            val selectedWay = wayChoices[which]
            studentView.modifyStudentInfo("수업 방식", selectedWay)
        }
        val dialog = builder.create()
        dialog.show()
    }
    // 사진관련코드/////////////////////////////////////////////
    //기본 사진인지 아닌지 확인
    private fun bindProfilePicture()
    {
        binding.ivProfileImgFixStudentInfo.setOnClickListener {
            val currentDrawable = binding.ivProfileImgFixStudentInfo.drawable as? BitmapDrawable
            val defaultDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.teacher_profileimage) as? BitmapDrawable

            if (currentDrawable?.bitmap != defaultDrawable?.bitmap) {
                showImageOptionPopup(it)
            }
            else
            {
                uploadPhoto()
            }
        }
    }
    //이미지뷰 눌렀을 때 나오는 옵션
    private fun showImageOptionPopup(view: View) {
        val popupMenu = context?.let { PopupMenu(it, view) }
        if (popupMenu != null) {
            popupMenu.menuInflater.inflate(R.menu.profile_image_options_menu, popupMenu.menu)

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
            popupMenu.show()
        }
    }
    //갤러리 가기
    private fun uploadPhoto()
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY)
    }
    //사진 가져오기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var resultUri : Uri?=null
        if (requestCode == REQUEST_GALLERY && resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImageUri = data?.data
            //이미지 명 다르게해서 사진 재선택 가능하게하기
            val destinationUri = Uri.fromFile(File(context?.cacheDir, "cropped_${System.currentTimeMillis()}"))
            UCrop.of(selectedImageUri!!, destinationUri)
                .withAspectRatio(1f, 1f)  // 1:1 비율로 크롭
                .start(requireContext(), this)
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == AppCompatActivity.RESULT_OK) {
            binding.ivProfileImgFixStudentInfo.tag = "custom"
            resultUri = UCrop.getOutput(data!!)!!
            binding.ivProfileImgFixStudentInfo.scaleType = ImageView.ScaleType.FIT_CENTER
            binding.ivProfileImgFixStudentInfo.setImageURI(resultUri)
            binding.ivProfileImgFixStudentInfo.adjustViewBounds = true
            Log.d("UCropResult", "URI: $resultUri")


            context?.let { studentView.uploadStudentProfileImage(resultUri, it) }
            //사이즈 조절
//            val layoutParams = binding.ivProfileImgFixTeacherInfo.layoutParams as ViewGroup.MarginLayoutParams
//            layoutParams.setMargins(100, 100, 100, 100)
            // binding.ivProfileImgFixTeacherInfo.layoutParams = layoutParams
        }
    }
    //기본 이미지로 되돌이키는 코드
    private fun resetToDefaultImage() {
        binding.ivProfileImgFixStudentInfo.setImageResource(R.drawable.teacher_profileimage)
        binding.ivProfileImgFixStudentInfo.tag = "default"
        // 로컬의 이미지 파일 삭제
        // 로컬의 이미지 파일 삭제
        val file = File(context?.filesDir, uid+ "student.jpg")
        if (file.exists()) {
            file.delete()
        }

        // ImageView의 속성 조절
        binding.ivProfileImgFixStudentInfo.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.ivProfileImgFixStudentInfo.adjustViewBounds = true

    }
    //기존에 로컬에서 사진 불러오기
    private fun pullPicture()
    {

        val fileName  = uid+"student.jpg"
        Log.d("123456기존에 이미지 있나5", fileName!!)
        val imageFile = fileName?.let { File(context?.filesDir, it) }
        if (imageFile != null) {
            if (imageFile.exists()) {
                Log.d("123456기존에 이미지 있나55", fileName!!)
                val loadedBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                binding.ivProfileImgFixStudentInfo.setImageBitmap(loadedBitmap)
                binding.ivProfileImgFixStudentInfo.scaleType =
                    ImageView.ScaleType.FIT_CENTER
                binding.ivProfileImgFixStudentInfo.adjustViewBounds = true
            } else {
                binding.ivProfileImgFixStudentInfo.setImageResource(R.drawable.teacher_profileimage)
            }

        } else {
            Log.d("123456기존에 이미지 있나", "이미지 없다")
        }

    }


}