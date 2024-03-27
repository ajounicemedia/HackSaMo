package god.example.god_of_teaching.view.nav.chat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import god.example.god_of_teaching.R
import god.example.god_of_teaching.databinding.AdapterChatListBinding
import god.example.god_of_teaching.model.dataclass.ChatListInfo


class ChatListAdapter(private val onclick: (ChatListInfo)->Unit,
                      private val notificationCheck: (ChatListInfo) -> Boolean,//알림 해제 했는지 안 했는지
                      private val blackListCheck: (ChatListInfo) -> Boolean//차단 했는지 안 했는지
) : ListAdapter<ChatListInfo, ChatListAdapter.ViewHolder>(differ) {

    inner class ViewHolder(private val binding: AdapterChatListBinding): RecyclerView.ViewHolder(binding.root){
        //채팅방 목록에 채팅 불러오기
        fun bind(item: ChatListInfo)
        {
            //프사인데 나중에 기능 추가할 때 쓰기
            //item?.friend_Id?.let { getImageData(it,binding.profileImageView) }
            //새 메시지 왔는지 확인
            binding.tvNewMessage.isVisible = item.newMessage==true
            binding.tvNickname.text = item.otherName
            binding.tvLastMassage.text = item.lastMessage
            binding.tvTime.text = item.lastMessageTime
            binding.root.setOnClickListener {
                onclick(item)
            }
            Log.d("123123123123",notificationCheck(item).toString())
            Log.d("123123123123",blackListCheck(item).toString())
           if(blackListCheck(item))
            {
            //차단 버튼 보이게
            binding.ivSettingNotifyBlacklist.setImageResource(R.drawable.blacklist_chat)
            binding.ivSettingNotifyBlacklist.isVisible = true
            }
            else if(notificationCheck(item))
            {
                //알림x 버튼 보이게
                binding.ivSettingNotifyBlacklist.isVisible = true
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterChatListBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
    companion object{
        val differ = object: DiffUtil.ItemCallback<ChatListInfo>()
        {
            override fun areContentsTheSame(oldItem: ChatListInfo, newItem: ChatListInfo): Boolean {
                return oldItem.lastMessage == newItem.lastMessage

            }

            override fun areItemsTheSame(oldItem: ChatListInfo, newItem: ChatListInfo): Boolean {
                return oldItem == newItem
            }
        }
    }
}