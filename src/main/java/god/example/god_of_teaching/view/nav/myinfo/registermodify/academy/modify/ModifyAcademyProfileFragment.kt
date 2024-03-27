package god.example.god_of_teaching.view.nav.myinfo.registermodify.academy.modify

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
import god.example.god_of_teaching.databinding.FragmentModifyAcademyProfileBinding
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.viewmodel.AcademyViewModel
import java.io.File

//프사 추가 안 함
@AndroidEntryPoint
class ModifyAcademyProfileFragment : Fragment() {
    private val REQUEST_GALLERY = 1001
    private var _binding: FragmentModifyAcademyProfileBinding?=null
    private val binding get() = _binding!!
    private val academyView: AcademyViewModel by viewModels()
    private var uid: String?= null//사진 받아오기 위해
    private var classAge: MutableList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        classAge = mutableListOf()
        uid = arguments?.getString("uid")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_modify_academy_profile,container,false)
        binding.vm = academyView
        binding.lifecycleOwner=this
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //getExistingItem()
        basicSetting()
        modifyMyInfo()
        bindProfilePicture()
        pullPicture()
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
        MenuUtil.setupMenu(requireActivity(),navController)
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarFixAcademyInfo,"학원 정보 수정",binding.toolbarTitleFixAcademyInfo)
        NavigationUtil.handleBackPress(this,navController)
    }
    //어떤 정보를 수정할 것인지
    private fun modifyMyInfo()
    {
        //한줄소개 수정
        binding.tvDesFixAcademyInfo.setOnClickListener {
            val navController = findNavController()
            val bundle = Bundle()
            bundle.putString("modified_item", "한줄소개")
            navController.navigate(R.id.modifyAcademyInfoTextFragment,bundle)
        }
        //과목수정
        binding.tvSubjectFixAcademyInfo.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.modifyAcademySubjectFragment)
        }
        //수업대상수정
        binding.tvClassAgeFixAcademyInfo.setOnClickListener {
            classAgeDialog()
        }
        //학원소개수정
        binding.tvIntroduceFixAcademyInfo.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.modifyAcademyIntroduceFragment)
        }
        //검색가능한지역수정
        binding.tvAvailableSearchedFixAcademyInfo.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.modifyAcademySearchedLoaclFragment)
        }
    }
    //수업 대상 수정
    private fun classAgeDialog()
    {
//        val builder = AlertDialog.Builder(requireContext())
//        val wayChoices = arrayOf("사회인", "대학생", "n수생", "고등학생", "중학생", "초등학생", "미취학 아동")
//        val customTitle = LayoutInflater.from(requireContext()).inflate(R.layout.text_dialog_title_color, null)
//        val titleTextView = customTitle.findViewById<TextView>(R.id.customDialogTitle)
//        titleTextView.text = "수업 대상"
//        builder.setCustomTitle(customTitle)
//
//        builder.setItems(wayChoices) { _, which ->
//            val selectedWay = wayChoices[which]
//            academyView.modifyAcademyInfo("수업 대상", selectedWay)
//        }
//        val dialog = builder.create()
//        dialog.show()
        val items = listOf("사회인", "대학생", "n수생", "고등학생", "중학생", "초등학생", "미취학 아동")
        val checkedItems = BooleanArray(items.size) // 선택된 항목들을 저장할 배열

        // 이전에 선택한 항목들을 checkedItems 배열에 반영합니다.
        for (i in items.indices) {
            if (classAge?.contains(items[i]) == true) {
                checkedItems[i] = true
            }
        }

        val builder = context?.let { androidx.appcompat.app.AlertDialog.Builder(it, R.style.CustomAlertDialog) }

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

        builder?.setPositiveButton("확인") { dialog, _ ->

            classAge?.let { academyView.modifyAcademyInfoArray("수업 대상", it) }
            dialog.dismiss()
            Log.d("123123123",classAge.toString())
        }

        builder?.setNegativeButton("취소") { dialog, _ ->
            classAge?.clear()
            dialog.dismiss()
        }

        builder?.show()

    }
    // 사진관련코드/////////////////////////////////////////////
    //기본 사진인지 아닌지 확인
    private fun bindProfilePicture()
    {
        binding.ivProfileImgAcademyInfo.setOnClickListener {
            val currentDrawable = binding.ivProfileImgAcademyInfo.drawable as? BitmapDrawable
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
            binding.ivProfileImgAcademyInfo.tag = "custom"
            resultUri = UCrop.getOutput(data!!)!!
            binding.ivProfileImgAcademyInfo.scaleType = ImageView.ScaleType.FIT_CENTER
            binding.ivProfileImgAcademyInfo.setImageURI(resultUri)
            binding.ivProfileImgAcademyInfo.adjustViewBounds = true
            Log.d("UCropResult", "URI: $resultUri")



            context?.let { academyView.uploadAcademyProfileImage(resultUri!!, it) }
            //사이즈 조절
//            val layoutParams = binding.ivProfileImgFixTeacherInfo.layoutParams as ViewGroup.MarginLayoutParams
//            layoutParams.setMargins(100, 100, 100, 100)
            // binding.ivProfileImgFixTeacherInfo.layoutParams = layoutParams
        }
    }
    //기본 이미지로 되돌이키는 코드
    private fun resetToDefaultImage() {
        binding.ivProfileImgAcademyInfo.setImageResource(R.drawable.teacher_profileimage)
        binding.ivProfileImgAcademyInfo.tag = "default"
        // 로컬의 이미지 파일 삭제
        // 로컬의 이미지 파일 삭제
        val file = File(context?.filesDir, uid+ "academy.jpg")
        if (file.exists()) {
            file.delete()
        }

        // ImageView의 속성 조절
        binding.ivProfileImgAcademyInfo.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.ivProfileImgAcademyInfo.adjustViewBounds = true

    }
    //기존에 로컬에서 사진 불러오기
    private fun pullPicture()
    {

        val fileName  = uid+"academy.jpg"
        Log.d("123456기존에 이미지 있나", fileName!!)
        val imageFile = fileName?.let { File(context?.filesDir, it) }
        if (imageFile != null) {
            if (imageFile.exists()) {
                Log.d("123456기존에 이미지 있나", fileName!!)
                val loadedBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                binding.ivProfileImgAcademyInfo.setImageBitmap(loadedBitmap)
                binding.ivProfileImgAcademyInfo.scaleType =
                    ImageView.ScaleType.FIT_CENTER
                binding.ivProfileImgAcademyInfo.adjustViewBounds = true
            } else {
                binding.ivProfileImgAcademyInfo.setImageResource(R.drawable.teacher_profileimage)
            }

        } else {
            Log.d("123456기존에 이미지 있나", "이미지 없다")
        }

    }
    //기존 수업대상 체크되게 하기
//    private fun getExistingItem()
//    {

//    }

}