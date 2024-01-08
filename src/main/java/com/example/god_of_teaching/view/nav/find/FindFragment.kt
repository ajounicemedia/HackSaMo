package com.example.god_of_teaching.view.nav.find


import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.god_of_teaching.R
import com.example.god_of_teaching.databinding.FragmentFindBinding
import com.example.god_of_teaching.model.dataclass.AcademyInfo
import com.example.god_of_teaching.model.dataclass.StudentInfo
import com.example.god_of_teaching.model.dataclass.TeacherInfo
import com.example.god_of_teaching.viewmodel.FindViewModel
import com.example.god_of_teaching.viewmodel.WishListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

//찾기 프래그먼트
@AndroidEntryPoint
class FindFragment : Fragment() {
    private var _binding: FragmentFindBinding?=null
    private val binding get() = _binding!!
    private val findView : FindViewModel by activityViewModels()
    private val wishListViewModel : WishListViewModel by activityViewModels()

    private var selectedLocation : MutableList<String>?= null
    private var selectedSubject : MutableList<String>?= null
    private var gender : String?=null

    //유저가 선택할 때 바로 데이터 반영
    private var teacherListData : List<TeacherInfo>? = null
    private var academyListData : List<AcademyInfo>? = null
    private var studentListData : List<StudentInfo>?=null

   // override fun onResume() {
     //   super.onResume()
     //   observeFindData()
//        addTextToBtn()
//        observeSelectObject()
    //}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFindBinding.inflate(inflater,container,false)
        loadWishList()




        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basicSetting()
        observeFindData()
        observeListData()
        goOtherFragment()
        selectObject()
        btnSelectGender()
        find()
        addTextToBtn()
        observeSelectObject()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뒤로가기 했을 때 종료
    private fun pressBack(root: View)
    {
        root.isFocusableInTouchMode  = true
        root.requestFocus()
        root.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                showExitDialog()
                return@setOnKeyListener true
            }
            false
        }
    }
    //앱 종료 다이얼로그 표시
    private fun showExitDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("앱 종료")
            .setMessage("앱을 종료하시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                activity?.let {
                    activity?.finish()
                }
            }
            .setNegativeButton("아니오", null)
            .show()
    }
    //뷰 기본세팅
    private fun basicSetting() {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            if (navigationView.visibility == View.GONE) {
                navigationView.visibility = View.VISIBLE
            }
        }
        pressBack(binding.root)

    }
    //버튼에 유저가 고른 조건 반영
    private fun addTextToBtn()
    {
        if(findView.selectedLocations.value!=null)
        {
            binding.btnSetupLocalFind.text = findView.selectedLocations.value.toString()
        }
        if(findView.selectedSubjects.value!=null)
        {
            binding.btnSetupSubjectFind.text = findView.selectedSubjects.value.toString()
        }
        if(findView.selectedGender.value!=null)
        {
            binding.btnGenderFind.text = findView.selectedGender.value
        }
    }
    //유저가 조건 선택했는지 확인
    private fun observeFindData()
    {
        findView.selectedLocations.observe(viewLifecycleOwner)
        {
            if(it!=null)
            {
                selectedLocation = it
            }
        }
        findView.selectedSubjects.observe(viewLifecycleOwner)
        {
            if(it!=null)
            {
                selectedSubject = it
            }
        }
    }
    //학원 찾기
    private fun find()
    {

        binding.btnNextFind.setOnClickListener {
            if(findView.objectAcademy.value==true)
            {
                if(selectedSubject!=null&&selectedLocation!=null)
                {
                    findView.findAcademys(selectedSubject!!, selectedLocation!!)
                    val navController = findNavController()
                    navController.navigate(R.id.showAcademyListFragment)
//                    프래그먼트로 이동
//                     teacherListData가 null일씨 해당 조건을 만족하는 ~~가 없습니다 추가
                }
                else
                {
                    Toast.makeText(context,"조건을 모두 선택하지 않으셨습니다",Toast.LENGTH_SHORT).show()
                }
            }
            else if(findView.objectTeacher.value==true)
            {
                if(selectedSubject!=null&&selectedLocation!=null&&gender!=null)
                {
                    findView.findTeachers(selectedSubject!!, selectedLocation!!,gender!!)
                    val navController = findNavController()
                    navController.navigate(R.id.showTeacherFragment)
                }
                else
                {
                    Toast.makeText(context,"조건을 모두 선택하지 않으셨습니다",Toast.LENGTH_SHORT).show()
                }
            }
            else if(findView.objectStudent.value==true)
            {
                if(selectedSubject!=null&&selectedLocation!=null&&gender!=null)
                {
                    findView.findStudents(selectedSubject!!, selectedLocation!!,gender!!)
                    val navController = findNavController()
                    navController.navigate(R.id.showStudentFragment)
                }
                else
                {
                    Toast.makeText(context,"조건을 모두 선택하지 않으셨습니다",Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(context,"검색할 대상을 선택해 주세요.",Toast.LENGTH_SHORT).show()
            }

        }

    }
    //다른 프래그먼트로 이동
    private fun goOtherFragment()
    {
        //지역설정
        binding.btnSetupLocalFind.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.setupLocalFragment)
        }
        //과목설정
        binding.btnSetupSubjectFind.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.setupSubjectFragment)
        }
    }
    //검색될 유저 리스트 관찰
    private fun observeListData() {
        findView.searchedTeachers.observe(viewLifecycleOwner) { teachers ->
            teacherListData = teachers
        }
        findView.searchedAcademys.observe(viewLifecycleOwner) {academys ->
            academyListData = academys
        }
        findView.searchedStudents.observe(viewLifecycleOwner) {students ->
            studentListData = students
        }
    }

    //성별 선택 버튼
    private fun btnSelectGender()
    {
        binding.btnGenderFind.setOnClickListener {
            selectGender()
        }
    }
    //성별 선택
    private fun selectGender()
    {
        val builder = AlertDialog.Builder(requireContext())

        val wayChoices = arrayOf("상관 없음", "남자", "여자")
        val customTitle = LayoutInflater.from(requireContext()).inflate(R.layout.text_dialog_title_color, null)
        val titleTextView = customTitle.findViewById<TextView>(R.id.customDialogTitle)
        titleTextView.text = "성별 선택"
        builder.setCustomTitle(customTitle)

        builder.setItems(wayChoices) { _, which ->
            gender = wayChoices[which]
            findView.selectedGender.value = wayChoices[which]
            addTextToBtn()
        }
        val dialog = builder.create()
        dialog.show()
    }
    //실시간으로 ui 색상 변경위해(유저가 어떤 항목 선택할껀지 확인 후)
    private fun observeSelectObject() {
        findView.objectAcademy.observe(viewLifecycleOwner) {academy->
            if(academy)
            {
                binding.btnAcademyFind.setBackgroundResource(R.drawable.custom_button_theme4)
                binding.btnAcademyFind.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.btnTeacherFind.setBackgroundResource(R.drawable.custom_button_theme3)
                binding.btnTeacherFind.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                binding.btnStudentFind.setBackgroundResource(R.drawable.custom_button_theme3)
                binding.btnStudentFind.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            else
            {
                binding.btnAcademyFind.setBackgroundResource(R.drawable.custom_button_theme3)
                binding.btnAcademyFind.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }
        findView.objectTeacher.observe(viewLifecycleOwner) {teacher ->
            if(teacher)
            {
                binding.btnAcademyFind.setBackgroundResource(R.drawable.custom_button_theme3)
                binding.btnAcademyFind.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                binding.btnStudentFind.setBackgroundResource(R.drawable.custom_button_theme3)
                binding.btnStudentFind.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                binding.btnTeacherFind.setBackgroundResource(R.drawable.custom_button_theme4)
                binding.btnTeacherFind.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            else
            {
                binding.btnTeacherFind.setBackgroundResource(R.drawable.custom_button_theme3)
                binding.btnTeacherFind.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }
        findView.objectStudent.observe(viewLifecycleOwner) {student ->
            if(student)
            {
                binding.btnStudentFind.setBackgroundResource(R.drawable.custom_button_theme4)
                binding.btnStudentFind.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.btnAcademyFind.setBackgroundResource(R.drawable.custom_button_theme3)
                binding.btnAcademyFind.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                binding.btnTeacherFind.setBackgroundResource(R.drawable.custom_button_theme3)
                binding.btnTeacherFind.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            else{
                binding.btnStudentFind.setBackgroundResource(R.drawable.custom_button_theme3)
                binding.btnStudentFind.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }
    }

    //어떤 것을 찾을지 결정
    private fun selectObject()
    {
        binding.btnAcademyFind.setOnClickListener {
            if(findView.objectAcademy.value==false)
            {
                findView.objectAcademy.value=true
                findView.objectStudent.value=false
                findView.objectTeacher.value=false
            }
            else
            {
                findView.objectAcademy.value=false
            }
        }
        binding.btnStudentFind.setOnClickListener {
            if(findView.objectStudent.value==false)
            {
                findView.objectAcademy.value=false
                findView.objectStudent.value=true
                findView.objectTeacher.value=false
            }
            else
            {
                findView.objectStudent.value=false
            }
        }
        binding.btnTeacherFind.setOnClickListener {
            if(findView.objectTeacher.value==false)
            {
                findView.objectAcademy.value=false
                findView.objectStudent.value=false
                findView.objectTeacher.value=true

            }
            else
            {
                findView.objectTeacher.value=false
            }
        }
    }
    private fun loadWishList()
    {
        wishListViewModel.loadTeacherWishList()
        wishListViewModel.loadAcademyWishList()
        wishListViewModel.loadStudentWishList()
    }


}