package com.akane.scarletserenity.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.TchatMessage
import com.akane.scarletserenity.view.TchatAdapter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*
import kotlin.collections.ArrayList

class TchatActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val firestore = FirebaseFirestore.getInstance()
    val chatMessages = ArrayList<TchatMessage>()
    var chatRegistration: ListenerRegistration? = null
    var roomId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        //checkUser()
        initList()
        setViewListeners()
    }
    private fun setViewListeners() {
        button_send.setOnClickListener {
            sendChatMessage()
        }
    }
    private fun initList() {
        if (user == null)
            return
        list_chat.layoutManager = LinearLayoutManager(this)
        val adapter = TchatAdapter(chatMessages, user.uid)
        list_chat.adapter = adapter
        listenForChatMessages()
    }
    private fun listenForChatMessages() {
        roomId = intent.getStringExtra("INTENT_EXTRA_ROOMID")
        if (roomId == null) {
            finish()
            return
        }
        chatRegistration = firestore.collection("rooms")
            .document(roomId!!)
            .collection("messages")
            .addSnapshotListener { messageSnapshot, exception ->
                if (messageSnapshot == null || messageSnapshot.isEmpty)
                    return@addSnapshotListener
                chatMessages.clear()
                for (messageDocument in messageSnapshot.documents) {
                    chatMessages.add(
                        TchatMessage(
                            messageDocument["text"] as String,
                            messageDocument["user"] as String,
                            messageDocument["timestamp"] as Timestamp
                        ))
                }
                chatMessages.sortBy { it.timestamp }
                list_chat.adapter?.notifyDataSetChanged()
            }
    }
    private fun sendChatMessage() {
        val message = edittext_chat.text.toString()
        edittext_chat.setText("")
        firestore.collection("rooms").document(roomId!!).collection("messages")
            .add(mapOf(
                Pair("text", message),
                Pair("user", user?.uid),
                Pair("timestamp", Timestamp.now())
            ))
    }
    override fun onDestroy() {
        chatRegistration?.remove()
        super.onDestroy()
    }
}