package god.example.god_of_teaching.view.nav.chat.adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import god.example.god_of_teaching.databinding.AdapterChatRoomBinding
import god.example.god_of_teaching.model.dataclass.ChatMessageInfo
import god.example.god_of_teaching.view.nav.chat.FullScreenPhotoActivity

//채팅방 메세지 어댑터
class ChatRoomAdapter(teacherUid: String) : ListAdapter<ChatMessageInfo, ChatRoomAdapter.ViewHolder>(differ){
    //Chat_Screen에서 정보 받아오기
    var itemUid = teacherUid


    inner class ViewHolder(private val binding: AdapterChatRoomBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: ChatMessageInfo)
        {

            //상대방이 보낸 메세지
                if(item.userId==itemUid)
                {
                    //상대방이 사진 보냈을 때
                    if (item.messageType == "photo" && item.photoUri != null) {
                        //이미지 확대
                        binding.ivOtherPhoto.setOnClickListener {
                            val intent = Intent(binding.root.context, FullScreenPhotoActivity::class.java)
                            intent.putExtra("IMAGE_URL", item.photoUri)
                            binding.root.context.startActivity(intent)
                        }

                        binding.layoutPhoto.isGone = false
                        binding.tvMessage.isGone = true
                        //상대방이 사진 보낸시간
                        binding.tvPhotoText.text = item.messageTime
                        binding.tvPhotoText.gravity = Gravity.START

                        //이미지 할당
                        Glide.with(binding.root.context)
                            .load(item.photoUri)
                            .centerCrop()
                            //.placeholder(R.drawable.redheart) // 로딩 중 이미지 리소스
                            .into(binding.ivOtherPhoto)


                    }
                    else//상대방이 메세지 보냈을 때
                    {

                        binding.layoutPhoto.isGone = true
                        binding.tvMessage.isGone = false
                        //메세지 설정
                        val message = item.message
                        val messageTime = "\n${item.messageTime}"
                        val spannableString = SpannableString(message + messageTime)
                        // message 부분만 볼드체로 + 검정색
                        if (message != null) {
                            spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, message.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            spannableString.setSpan(ForegroundColorSpan(Color.BLACK), 0, message.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }

                        // TextView에 SpannableString 적용
                        binding.tvMessage.text = spannableString
                        binding.tvMessage.gravity = Gravity.START
                    }
                }
                else//내가 메세지 보냈을 때
                {
                    var messageTime : String? = null
                    //내가 사진 보냈을 때
                    if (item.messageType == "photo" && item.photoUri != null) {
                        //사진 확대
                        binding.ivMyPhoto.setOnClickListener {
                            val intent = Intent(binding.root.context, FullScreenPhotoActivity::class.java)
                            intent.putExtra("IMAGE_URL", item.photoUri)
                            binding.root.context.startActivity(intent)
                        }
                        binding.layoutPhoto.isGone = false
                        binding.tvMessage.isGone = true
                        //사진 시간 + 읽었는지 체크
                        //상대방이 읽었는지 확인 + 채팅 시간
                        if(item.readCheck==true) {
                            messageTime = "${item.messageTime}"
                        }
                        else {
                            messageTime = "${item.messageTime} 안 읽음"
                        }
                        binding.tvPhotoText.text = messageTime
                        binding.tvPhotoText.gravity = Gravity.END


                        //이미지 반영
                        Glide.with(binding.root.context)
                            .load(item.photoUri)
                            .centerCrop()
                            //.placeholder(R.drawable.redheart)
                            .into(binding.ivMyPhoto)

                    }
                    else{  //내가 메세지 보냈을 때
                        binding.layoutPhoto.isGone = true
                        binding.tvMessage.isGone = false
                        //상대방이 읽었는지 확인 + 채팅 시간
                   // if(item.readCheck==true) {
                        messageTime = "\n${item.messageTime}"
                    //}
//                    else {
//                        messageTime = "\n${item.messageTime} 안 읽음"
//                    }
                        //메세지 설정
                        val message = item.message
                        val spannableString = SpannableString(message + messageTime)
                        // message 부분만 볼드체로 + 검정색
                        if (message != null) {
                            spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, message.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            spannableString.setSpan(ForegroundColorSpan(Color.BLACK), 0, message.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }

                        // TextView에 SpannableString 적용
                        binding.tvMessage.text = spannableString
                        binding.tvMessage.gravity = Gravity.END
                    }

                }

            //이미지 확대
//            binding.ivOtherPhoto.setOnClickListener {
//                val intent = Intent(binding.root.context, FullScreenPhotoActivity::class.java)
//                intent.putExtra("IMAGE_URL", item.photoUri)
//                binding.root.context.startActivity(intent)
//            }
//            binding.ivOtherPhoto.setOnClickListener {
//                val intent = Intent(binding.root.context, FullScreenPhotoActivity::class.java)
//                intent.putExtra("IMAGE_URL", item.photoUri)
//                binding.root.context.startActivity(intent)
//            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterChatRoomBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
    companion object{
        val differ = object: DiffUtil.ItemCallback<ChatMessageInfo>()
        {
            override fun areContentsTheSame(oldItem: ChatMessageInfo, newItem: ChatMessageInfo): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

            override fun areItemsTheSame(oldItem: ChatMessageInfo, newItem: ChatMessageInfo): Boolean {
                return oldItem == newItem
            }
        }
    }


}