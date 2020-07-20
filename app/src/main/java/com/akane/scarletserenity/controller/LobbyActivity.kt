package com.akane.scarletserenity.controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.akane.scarletserenity.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_lobby.*

class LobbyActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        //checkUser()
        setViewListeners()
    }
    private fun setViewListeners() {
        button_enter.setOnClickListener { enterRoom() }
    }
    private fun enterRoom() {
        button_enter.isEnabled = false
        val roomId = edittext_roomid.text.toString()
        if (roomId.isEmpty()) {
            showErrorMessage()
            return
        }
        firestore.collection("users").document(user!!.uid).collection("rooms")
            .document(roomId).set(mapOf(
                Pair("id", roomId)
            ))
        val intent = Intent(this, TchatActivity::class.java)
        intent.putExtra("INTENT_EXTRA_ROOMID", roomId)
        startActivity(intent)
    }
    private fun showErrorMessage() {
        textview_error_enter.visibility = View.VISIBLE
        button_enter.isEnabled = true
    }
}