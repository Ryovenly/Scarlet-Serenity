package com.akane.scarletserenity.controller.multi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.model.character.Character
import com.akane.scarletserenity.model.character.CharacterHelper
import com.akane.scarletserenity.model.chat.Message
import com.akane.scarletserenity.view.ChatAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_chat.*
import kotlin.collections.ArrayList

class ChatActivity : BaseActivity() {

    val firestore = FirebaseFirestore.getInstance()
    val chatMessages = ArrayList<Message>()
    var chatRegistration: ListenerRegistration? = null
    var roomId: String? = null
    lateinit var modelCharacter: Character


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        textAssign()
        getCharacter()
        initListRecyclerView()
        setViewListeners()

    }

    private fun textAssign() {
        edittext_chat.hint = getString(R.string.chatText)
    }

    private fun getCharacter() {
        CharacterHelper.getCharacter(user?.uid)
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    modelCharacter = documentSnapshot.toObject(Character::class.java)!!

                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }

    private fun setViewListeners() {
        button_send.setOnClickListener {
            sendChatMessage()
        }
        bt_returnChat.setOnClickListener {
            finish()
        }
    }
    private fun initListRecyclerView() {
        if (user == null)
            return
        list_chatRV.layoutManager = LinearLayoutManager(this)
        val adapter = ChatAdapter(chatMessages, user.uid)
        list_chatRV.adapter = adapter
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
                        Message(
                            messageDocument["text"] as String,
                            messageDocument["user"] as String,
                            messageDocument["timestamp"] as Timestamp,
                            messageDocument["character"] as String
                        )
                    )
                }
                chatMessages.sortBy { it.timestamp }
                list_chatRV.adapter?.notifyDataSetChanged()
            }

    }

    override fun onDestroy() {
        chatRegistration?.remove()
        super.onDestroy()
    }
    private fun sendChatMessage() {

        val message = edittext_chat.text.toString()
        if (message.isEmpty()){
            Toast.makeText(getApplicationContext(), getString(R.string.message_chat_error), Toast.LENGTH_LONG).show();
        }
        else {
            edittext_chat.setText("")
            firestore.collection("rooms").document(roomId!!).collection("messages")
                .add(mapOf(
                    Pair("text", message),
                    Pair("user", user?.uid),
                    Pair("timestamp", Timestamp.now()),
                    Pair("character", modelCharacter.pseudo)
                ))
        }
    }




}