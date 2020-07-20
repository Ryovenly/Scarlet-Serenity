package com.akane.scarletserenity.view



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.TchatMessage
import kotlinx.android.synthetic.main.list_item_chat.view.*

class TchatAdapter(val chatMessages: List<TchatMessage>, val uid: String): RecyclerView.Adapter<TchatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatMessage = chatMessages[position]

        if (chatMessage.user == uid) {
            holder.itemView.textview_chat_sent.text = chatMessage.text
            holder.itemView.textview_chat_received.visibility = View.GONE
        } else {
            holder.itemView.textview_chat_received.text = chatMessage.text
            holder.itemView.textview_chat_sent.visibility = View.GONE
        }
    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_chat, parent, false)
    ) {

        private var chatTextSent: TextView? = null
        private var chatTextReceived: TextView? = null

        init {
            chatTextSent = itemView.findViewById(R.id.textview_chat_sent)
            chatTextReceived = itemView.findViewById(R.id.textview_chat_received)
        }

    }

}