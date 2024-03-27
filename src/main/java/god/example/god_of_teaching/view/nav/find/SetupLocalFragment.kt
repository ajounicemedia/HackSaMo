package god.example.god_of_teaching.view.nav.find

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentTeacherChoiceLocalBinding
import god.example.god_of_teaching.model.`object`.data.LocationData
import god.example.god_of_teaching.model.`object`.util.MenuUtil
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.model.`object`.util.ToolbarUtil
import god.example.god_of_teaching.view.nav.myinfo.registermodify.adapter.ChoiceBigLocalAdapter
import god.example.god_of_teaching.view.nav.myinfo.registermodify.adapter.ChoiceLocalAdapter
import god.example.god_of_teaching.view.nav.myinfo.registermodify.adapter.MyLocalAdapter
import god.example.god_of_teaching.viewmodel.FindViewModel

//지역 필터링 프래그먼트
@AndroidEntryPoint
class SetupLocalFragment : Fragment() {
    private var _binding : FragmentTeacherChoiceLocalBinding?=null
    private val binding get() = _binding!!
    private lateinit var bigLocalAdapter: ChoiceBigLocalAdapter
    private lateinit var localAdapter: ChoiceLocalAdapter
    private lateinit var myLocalAdapter: MyLocalAdapter
    private val localList= LocationData.locationData
    private lateinit var myLocalList : MutableList<String>
    private val findView : FindViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentTeacherChoiceLocalBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basicSetting()
        createAdapter()
        next()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본세팅
    private fun basicSetting()
    {
        myLocalList = mutableListOf()
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            navigationView.visibility = View.GONE
        }
        binding.btnNextChoiceLocal.text = "확인"
        val navController = findNavController()
        MenuUtil.setupMenu(requireActivity(),navController)
        ToolbarUtil.setupToolbar(activity as AppCompatActivity, binding.toolbarChoiceLocal,"지역 선택",binding.toolbarTitleChoiceLocal)
        NavigationUtil.handleBackPress(this,navController)
    }
    //지역 수정 및 원래 프래그먼트로 복귀
    private fun next()
    {
        binding.btnNextChoiceLocal.setOnClickListener {
            if (myLocalList.isNotEmpty()) {
                //myLocalList.add("전국")
                findView.selectedLocations.value = myLocalList
                val navController = findNavController()
                navController.popBackStack()
            } else {
                Toast.makeText(context, "1개 이상의 지역을 선택해야합니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //지역어댑터
    private fun createAdapter() {
        bigLocalAdapter = ChoiceBigLocalAdapter(LocationData.bigLocalList) { bigLocalPosition ->
            localAdapter = ChoiceLocalAdapter(localList[bigLocalPosition]) { localItem ->
                //5개 조건 + 동일한거 선택 안 되게
                if(myLocalList.size<5) {
                    if (!myLocalList.contains(localItem)) {
                        myLocalList.add(localItem)
                    }
                    else
                    {
                        Toast.makeText(context,"동일한 지역은 중복 등록 불가합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(context,"5개를 초과한 지역을 선택할 수 없습니다", Toast.LENGTH_SHORT).show()
                }
                myLocalAdapter = MyLocalAdapter(myLocalList) {myLocalPosition ->

                    myLocalList.removeAt(myLocalPosition)
                    myLocalAdapter.notifyItemRemoved(myLocalPosition)
                    myLocalAdapter.notifyItemRangeChanged(myLocalPosition, myLocalList.size - myLocalPosition)

                }
                binding.rvMyLocalChoiceLocal.layoutManager = LinearLayoutManager(context)
                binding.rvMyLocalChoiceLocal.adapter = myLocalAdapter
            }
            binding.rvLocalChoiceLocal.layoutManager = LinearLayoutManager(context)
            binding.rvLocalChoiceLocal.adapter = localAdapter
        }
        // bigLocalAdapter 선택하기 전에 localAdapter 초기화
        localAdapter = ChoiceLocalAdapter(localList[0]) { _ ->
            // 이 부분은 선택했을 때의 동작이므로 bigLocalAdapter에서 선택되었을 때와 동일하게 작동하도록 둘 수 있습니다.
            // 아니면 선택했을 때의 동작을 별도의 함수로 분리하여 여기서도 호출할 수 있습니다.
        }
        binding.rvLocalChoiceLocal.layoutManager = LinearLayoutManager(context)
        binding.rvLocalChoiceLocal.adapter = localAdapter
        binding.rvBigLocalChoiceLocal.layoutManager = LinearLayoutManager(context)
        binding.rvBigLocalChoiceLocal.adapter = bigLocalAdapter
    }
}