package com.akane.scarletserenity.view



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.chat.Message
import kotlinx.android.synthetic.main.list_item_chat.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(val chatMessages: List<Message>, val uid: String): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        val formatDate = SimpleDateFormat("EEEE dd MMMM yyyy HH:mm", Locale.FRANCE)
        val date = formatDate.format(chatMessage.timestamp.toDate()).toString()

        if (chatMessage.user == uid) {
            holder.itemView.textview_chat_sent.text = chatMessage.text
            holder.itemView.character_chat_sent.text = chatMessage.character
            holder.itemView.date_sent.text = date
            holder.itemView.frame_chat_received.visibility = View.GONE
            holder.itemView.frame_chat_sent.visibility = View.VISIBLE

        } else {
            holder.itemView.textview_chat_received.text = chatMessage.text
            holder.itemView.character_chat_received.text = chatMessage.character
            holder.itemView.date_received.text = date
            holder.itemView.frame_chat_sent.visibility = View.GONE
            holder.itemView.frame_chat_received.visibility = View.VISIBLE

        }
    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_chat, parent, false)
    ) {

        private var chatTextSent: TextView? = null
        private var chatTextReceived: TextView? = null
        private var chatCharacterSent: TextView? = null
        private var chatCharacterReceived: TextView? = null
        private var chatDateSent: TextView? = null
        private var chatDateReceived: TextView? = null

        init {
            chatTextSent = itemView.findViewById(R.id.textview_chat_sent)
            chatTextReceived = itemView.findViewById(R.id.textview_chat_received)
            chatCharacterSent = itemView.findViewById(R.id.character_chat_sent)
            chatCharacterReceived = itemView.findViewById(R.id.character_chat_received)
            chatDateSent = itemView.findViewById(R.id.date_sent)
            chatDateReceived = itemView.findViewById(R.id.date_received)
        }

    }

}