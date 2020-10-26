package com.akane.scarletserenity.controller.multi

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_lobby.*

class LobbyActivity : BaseActivity() {

    val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        setViewListeners()
    }
    private fun setViewListeners() {
        textAssign()

        button_enter.setOnClickListener { enterRoom() }
        bt_returnLobby.setOnClickListener { finish() }
        bt_general.setOnClickListener { enterRoom("1") }
        bt_general2.setOnClickListener { enterRoom("2") }
    }

    private fun textAssign() {
        edittext_roomid.hint = getString(R.string.roomHint)
        button_enter.text = getString(R.string.lobbyEnter)
    }

    private fun enterRoom() {
        val roomId = edittext_roomid.text.toString()
        if (roomId.isEmpty()) {
            showErrorMessage()
            return
        }
        firestore.collection("rooms")
            .document(roomId).set(mapOf(
                Pair("id", roomId)
            ))
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("INTENT_EXTRA_ROOMID", roomId)
        startActivity(intent)
    }

    private fun enterRoom(id:String) {
        if (id.isEmpty()) {
            showErrorMessage()
            return
        }
        firestore.collection("rooms")
            .document(id).set(mapOf(
                Pair("id", id)
            ))
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("INTENT_EXTRA_ROOMID", id)
        startActivity(intent)
    }
    private fun showErrorMessage() {
        textview_error_enter.visibility = View.VISIBLE
    }
}