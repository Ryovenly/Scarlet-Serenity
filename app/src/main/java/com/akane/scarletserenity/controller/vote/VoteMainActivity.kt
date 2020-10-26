package com.akane.scarletserenity.controller.vote

import android.content.Intent
import android.os.Bundle
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import kotlinx.android.synthetic.main.activity_main_vote.*

class VoteMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_vote)

        setViewListeners()
    }


    private fun setViewListeners() {

        bt_create_vote.setOnClickListener {
            startCreateVoteActivity()
        }

        bt_participate_vote.setOnClickListener {
            startParticipateVoteLobbyActivity()
        }

        bt_leave.setOnClickListener {
            finish()
        }
    }


    private fun startCreateVoteActivity() {
        val intent = Intent(this, CreateVoteActivity::class.java)
        startActivity(intent)
    }

    private fun startParticipateVoteLobbyActivity() {
        val intent = Intent(this, ParticipateVoteLobbyActivity::class.java)
        startActivity(intent)
    }

}