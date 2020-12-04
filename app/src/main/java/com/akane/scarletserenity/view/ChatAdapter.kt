package com.akane.scarletserenity.view



import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.GlideApp
import com.akane.scarletserenity.model.character.Character
import com.akane.scarletserenity.model.character.CharacterHelper
import com.akane.scarletserenity.model.chat.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.list_item_chat.view.*
import kotlinx.android.synthetic.main.list_item_monster.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(val chatMessages: List<Message>, val uid: String): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    val user = Firebase.auth.currentUser

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

            CharacterHelper.getCharacter(chatMessage.user)
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null) {
                        val modelCharacter = documentSnapshot.toObject(Character::class.java)!!

                        if (modelCharacter.img == null){
                            holder.itemView.iv_avatar_sent.setImageResource(R.drawable.no_avatar)
                        } else{
                            val storageReference = Firebase.storage.getReferenceFromUrl(modelCharacter.img!!)

                            GlideApp.with(holder.itemView.context)
                                .load(storageReference)
                                .into(holder.itemView.iv_avatar_sent)
                        }


                    } else {
                        Log.d("TAG", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }



            holder.itemView.frame_chat_received.visibility = View.GONE
            holder.itemView.frame_chat_sent.visibility = View.VISIBLE

        } else {
            holder.itemView.textview_chat_received.text = chatMessage.text
            holder.itemView.character_chat_received.text = chatMessage.character
            holder.itemView.date_received.text = date

            CharacterHelper.getCharacter(chatMessage.user)
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        var modelCharacterReceived:Character?
                        modelCharacterReceived = documentSnapshot.toObject(Character::class.java)!!

                        if (modelCharacterReceived.img.isNullOrEmpty()){
                            holder.itemView.iv_avatar_received.setImageResource(R.drawable.no_avatar)
                        } else{
                            val storageReference = Firebase.storage.getReferenceFromUrl(modelCharacterReceived.img!!)

                            GlideApp.with(holder.itemView.context)
                                .load(storageReference)
                                .into(holder.itemView.iv_avatar_received)
                        }


                    } else {
                        Log.d("TAG", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }
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
        private var chatAvatarSent: ImageView? = null
        private var chatAvatarReceived: ImageView? = null

        init {
            chatTextSent = itemView.findViewById(R.id.textview_chat_sent)
            chatTextReceived = itemView.findViewById(R.id.textview_chat_received)
            chatCharacterSent = itemView.findViewById(R.id.character_chat_sent)
            chatCharacterReceived = itemView.findViewById(R.id.character_chat_received)
            chatDateSent = itemView.findViewById(R.id.date_sent)
            chatDateReceived = itemView.findViewById(R.id.date_received)
            chatAvatarSent = itemView.findViewById(R.id.iv_avatar_sent)
            chatAvatarReceived = itemView.findViewById(R.id.iv_avatar_received)
        }

    }

}