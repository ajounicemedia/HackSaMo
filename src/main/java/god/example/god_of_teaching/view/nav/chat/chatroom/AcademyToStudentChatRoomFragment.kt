package god.example.god_of_teaching.view.nav.chat.chatroom

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.FragmentChatRoomBinding
import god.example.god_of_teaching.model.`object`.util.NavigationUtil
import god.example.god_of_teaching.view.nav.chat.adapter.ChatRoomAdapter
import god.example.god_of_teaching.viewmodel.AcademyToStudentChatViewModel
import god.example.god_of_teaching.viewmodel.ChatViewModel
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

//학원 -> 학생 채팅방 프래그먼트
@AndroidEntryPoint
class AcademyToStudentChatRoomFragment : Fragment(){
    private var _binding: FragmentChatRoomBinding?=null
    private val binding get() = _binding!!
    private var otherName : String?=null
    private var otherUid : String?=null
    private var myChatType : String?=null
    private var otherChatType : String?=null
    private var firstChat : Boolean?=null
    private val academyChatView : AcademyToStudentChatViewModel by viewModels()
    private val chatView : ChatViewModel by viewModels()
    private lateinit var chatMessageAdapter: ChatRoomAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager



    // ActivityResultLauncher 초기화
    private lateinit var selectPhotosLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        otherName = arguments?.getString("other_name")
        otherUid = arguments?.getString("other_uid")
        myChatType = arguments?.getString("my_chat_type",null)
        otherChatType = arguments?.getString("other_chat_type",null)
        firstChat = arguments?.getBoolean("first_chat", false)

        otherUid?.let { academyChatView.getMessage(it) }//채팅정보가져오기
        //otherUid?.let { chatView.changeAcademyReadCheck(it) }//읽음처리

        // ActivityResultLauncher 인스턴스 설정
        selectPhotosLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            // 선택된 사진의 URI 처리
            handleSelectedPhotos(uris)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatRoomBinding.inflate(inflater,container,false)



        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basicSetting()
        sendMessage()
        createAdapter()
        observeChatMessages()
        //selectPhoto()
        createMenu()
        updateBlockMenuItem()
        updateNotificationMenuItem()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //뷰 기본세팅
    private fun basicSetting()
    {
        val navigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (navigationView != null) {
            navigationView.visibility = View.GONE
        }
        binding.toolbarTitleChatRoom.text = otherName
        val navController = findNavController()
        NavigationUtil.handleBackPress(this,navController)

    }
    //어댑터 생성
    private fun createAdapter()
    {

        linearLayoutManager = LinearLayoutManager(context).apply {
            stackFromEnd = true
        }

        binding.rvChatList.layoutManager = linearLayoutManager
        chatMessageAdapter = otherUid?.let { ChatRoomAdapter(it) }!!
        binding.rvChatList.adapter = chatMessageAdapter


    }

    //채팅 갱신
    private fun observeChatMessages() {
        academyChatView.chatMessages?.observe(viewLifecycleOwner){ messages ->
            //어댑터 다시 생성해야할거 대비
//            if (!::chatMessageAdapter.isInitialized) {
//                createAdapter()
//            }
            //채팅방내에서 새메시지 받을 때 채팅 리스트에서 새로운 메세지 안 보이게하기위해
            otherUid?.let { academyChatView.changeMessageStatus(it) }
            chatMessageAdapter.submitList(messages)
            linearLayoutManager.smoothScrollToPosition(binding.rvChatList,null,chatMessageAdapter.itemCount)//메세지 받았을 때 위로
        }
    }
    //메세지 전송
    private fun sendMessage()
    {

        binding.btnSend.setOnClickListener {
            if(firstChat==true) {
                if (chatView.checkBlackList(otherName!!)) {
                    Toast.makeText(context, "차단된 유저에게는 메세지를 전송할 수 없습니다.", Toast.LENGTH_SHORT).show()
                } else {//차단이 아니면
                    val message = binding.etMessage.text.toString()
                    if (message.isNotEmpty()) {
                        val currentDate = Calendar.getInstance().time
                        //채팅 리스트에서 표시될 시간
                        val dateFormatChatList =
                            java.text.SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA)
                        dateFormatChatList.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                        val chatListTime = dateFormatChatList.format(currentDate)
                        //채팅방 내에서 표시될 시간
//                val dateFormatChatRoom = java.text.SimpleDateFormat("HH:mm", Locale.KOREA)
//                dateFormatChatRoom.timeZone = TimeZone.getTimeZone("Asia/Seoul")
//                val chatRoomTime = dateFormatChatRoom.format(currentDate)

                        val fcmKey = getString(R.string.my_fcm_key)
                        academyChatView.createChatRoom(otherUid!!, otherName!!, message, chatListTime, otherChatType!!, myChatType!!)//채팅리스트 업데이트
                        academyChatView.updateChatRoom(otherUid!!, message, chatListTime, fcmKey)//채팅방 업데이트
                        binding.etMessage.text.clear()//전송시 에딧 텍스트 비워줌
                        linearLayoutManager.smoothScrollToPosition(
                            binding.rvChatList,
                            null,
                            chatMessageAdapter.itemCount
                        )//전송시 위로
                    }
                }
            }
            else {
                if (chatView.checkBlackList(otherName!!)) {
                    Toast.makeText(context, "차단된 유저에게는 메세지를 전송할 수 없습니다.", Toast.LENGTH_SHORT).show()
                } else {//차단이 아니면
                    val message = binding.etMessage.text.toString()
                    if (message.isNotEmpty()) {
                        val currentDate = Calendar.getInstance().time
                        //채팅 리스트에서 표시될 시간
                        val dateFormatChatList = java.text.SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA)
                        dateFormatChatList.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                        val chatListTime = dateFormatChatList.format(currentDate)

                        //채팅방 내에서 표시될 시간
//                val dateFormatChatRoom = java.text.SimpleDateFormat("HH:mm", Locale.KOREA)
//                dateFormatChatRoom.timeZone = TimeZone.getTimeZone("Asia/Seoul")
//                val chatRoomTime = dateFormatChatRoom.format(currentDate)

                        val fcmKey = getString(R.string.my_fcm_key)
                        academyChatView.updateChatList(otherUid!!, otherName!!, message, chatListTime)//채팅리스트 업데이트
                        academyChatView.updateChatRoom(otherUid!!, message, chatListTime, fcmKey)//채팅방 업데이트
                        binding.etMessage.text.clear()//전송시 에딧 텍스트 비워줌
                        linearLayoutManager.smoothScrollToPosition(binding.rvChatList, null, chatMessageAdapter.itemCount
                        )//전송시 위로
                    }
                }
            }
        }

    }
    // 사진 선택 메서드
//    private fun selectPhoto() {
//        binding.ivSelectPhoto.setOnClickListener {
//            selectPhotosLauncher.launch("image/*")
//        }
//
//    }

    // 사진선택하는 메소드
    private fun handleSelectedPhotos(uris: List<Uri>) {

        val currentDate = Calendar.getInstance().time

        //채팅 리스트에서 표시될 시간
        val dateFormatChatList = java.text.SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA)
        dateFormatChatList.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        val chatListTime = dateFormatChatList.format(currentDate)

        //채팅방 내에서 표시될 시간
//        val dateFormatChatRoom = java.text.SimpleDateFormat("HH:mm", Locale.KOREA)
//        dateFormatChatRoom.timeZone = TimeZone.getTimeZone("Asia/Seoul")
//        val chatRoomTime = dateFormatChatRoom.format(currentDate)
        // 선택된 사진을 Firebase Storage에 업로드하고 채팅 메시지 전송
        if (uris.isNotEmpty()) {
            if (uris.size > 3) {
                Snackbar.make(binding.root, "사진은 3개까지 선택 가능합니다", Snackbar.LENGTH_SHORT).show()
            } else {
                // 사진 업로드 및 메시지 전송
                val fcmKey = getString(R.string.my_fcm_key)
                academyChatView.sendPhotoMessage(otherUid!!, uris,chatListTime,otherName!!,fcmKey)
            }
        }
    }

    //채팅내 메뉴
    private fun createMenu()
    {

        binding.toolbarChatRoom.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                //조건 추가 학생일 때 선생님일 때
                R.id.other_info ->
                {
                    if(myChatType=="student")
                    {
                        chatView.getOtherProfileImage("academies",otherUid!!)
                        chatView.uri?.observe(viewLifecycleOwner){ uri->
                            if(uri!="null") {
                                val navController = findNavController()
                                val bundle = Bundle()
                                bundle.putString("academyImageUrl", uri.toString())
                                bundle.putString("other_uid", otherUid)
                                navController.navigate(R.id.academyInfoFragment, bundle)
                            }
                            else//상대방 프사 없을시
                            {
                                val navController = findNavController()
                                val bundle = Bundle()
                                bundle.putString("other_uid", otherUid)
                                navController.navigate(R.id.academyInfoFragment, bundle)
                            }
                        }

                    }
                    if (myChatType=="academy")//학원이면 학생정보 보기
                    {
                        chatView.getOtherProfileImage("students",otherUid!!)
                        chatView.uri?.observe(viewLifecycleOwner){ uri->
                            if(uri!="null") {
                                val navController = findNavController()
                                val bundle = Bundle()
                                bundle.putString("studentImageUrl", uri.toString())
                                bundle.putString("other_uid", otherUid)
                                navController.navigate(R.id.studentInfoFragment, bundle)
                            }
                            else
                            {
                                val navController = findNavController()
                                val bundle = Bundle()
                                bundle.putString("other_uid", otherUid)
                                navController.navigate(R.id.studentInfoFragment, bundle)
                            }
                        }
                    }
                    true
                }
                R.id.blacklist -> {//차단은 로컬에 넣어서 데이터 불러오는거 최소화
                    blackList()
                    true
                }
                // 알림 끄기
                R.id.notification_setting -> {
                    notification()
                    //레포지토리에 참 트루 넣어주자

                    true
                }
                R.id.report-> {//신고하기 버튼 신고하면 차단에도 추가
                    report()

                    true
                }
                R.id.go_out_chatroom-> {//채팅방 나가기
                    academyChatView.goOutChatRoom(otherUid!!)
                    val navController = findNavController()
                    navController.popBackStack()
                    true
                }
                // 신고하기 추가
                else -> false
            }

        }
    }
    //블랙리스트 추가 삭제
    private fun blackList()
    {
        if (!chatView.checkBlackList(otherName!!))
        {
            chatView.addBlackList(otherName!!)
        }
        else {
            chatView.removeBlackList(otherName!!)
        }
        updateBlockMenuItem()
    }
    //알림 추가 해제
    private fun notification()
    {
        if(!chatView.checkBlackNotify(otherUid!!))
        {
            chatView.addBlackListNotify(otherUid!!)
        }
        else{
            chatView.removeBlackListNotify(otherUid!!)
        }
        updateNotificationMenuItem()
    }
    private fun report()
    {
        if (!chatView.checkBlackList(otherName!!))//이미 차단목록에 있는지 확인
        {
            chatView.addBlackList(otherName!!)
            if(!chatView.checkReport(otherUid!!))//이미 신고했는지 확인
            {
                val navController = findNavController()
                val bundle = Bundle()
                bundle.putString("other_uid",otherUid!!)
                navController.navigate(R.id.reportReasonFragment,bundle)//신고화면으로 이동
            }
            else
            {
                Toast.makeText(context, "이미 신고한 유저입니다.", Toast.LENGTH_SHORT).show()
            }

        }
        else{
            if(!chatView.checkReport(otherUid!!))//이미 신고했는지 확인
            {
                val navController = findNavController()
                val bundle = Bundle()
                bundle.putString("other_uid",otherUid!!)
                navController.navigate(R.id.reportReasonFragment,bundle)
            }
            else
            {
                Toast.makeText(context, "이미 신고한 유저입니다.", Toast.LENGTH_SHORT).show()
            }
        }
        //updateBlockMenuItem()
    }
    //상대유저가 블랙리스트인지 체크
    private fun updateBlockMenuItem() {
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar_chat_room)
        toolbar?.menu?.findItem(R.id.blacklist)?.let { menuItem ->
            otherName?.let {
                menuItem.title = if (chatView.checkBlackList(otherName!!)) "차단 해제" else "차단"
            }
        }
    }
    //알림차단했는지 안 했는지 확인
    private fun updateNotificationMenuItem() {
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar_chat_room)
        toolbar?.menu?.findItem(R.id.notification_setting)?.let { menuItem ->
            otherUid?.let {
                menuItem.title = if (chatView.checkBlackNotify(otherUid!!)) "알림 켜기" else "알림 끄기"
            }
        }
    }
}