package com.akane.scarletserenity.controller.vote

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.controller.character.MainCharacterActivity
import com.akane.scarletserenity.model.vote.Scrutin
import com.akane.scarletserenity.model.vote.ScrutinHelper
import com.akane.scarletserenity.view.ScrutinParticipateAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_participate_vote_lobby.*
import java.util.*

class ParticipateVoteLobbyActivity: BaseActivity(), ScrutinParticipateAdapter.ItemClickListener {

    var scrutinRegistration: ListenerRegistration? = null
    val scrutins = ArrayList<Scrutin>()
    lateinit var adapter: ScrutinParticipateAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participate_vote_lobby)

        onViewListener()
        initListRecyclerView()

    }

    private fun onViewListener(){
        bt_leave.setOnClickListener {
            startMainCharacterActivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListRecyclerView() {

        rv_scrutin_participate_vote.layoutManager = LinearLayoutManager(this)
        adapter = ScrutinParticipateAdapter(scrutins)
        rv_scrutin_participate_vote.adapter = adapter
        adapter.setClickListener(this)
        listenForScrutins()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun listenForScrutins() {

        scrutinRegistration = ScrutinHelper.getScrutinCollection()
            ?.addSnapshotListener { scrutinsData, exception ->
                if (scrutinsData == null || scrutinsData.isEmpty)
                    return@addSnapshotListener
                for (scrutin in scrutinsData.documents) {
                    scrutins.add(
                        Scrutin(
                            scrutin["id"] as String?,
                            scrutin["title"] as String,
                            scrutin["img"] as String,
                            scrutin["description"] as String,
                            scrutin["date_start"] as Timestamp,
                            scrutin["date_end"] as Timestamp,
                            scrutin["_end"] as Boolean,
                            scrutin["organizer"] as String
                        )
                    )
                }
                rv_scrutin_participate_vote.adapter?.notifyDataSetChanged()
            }

    }

    override fun onItemClick(view: View?, position: Int) {
        val scrutinId = scrutins[position].id

      //  Toast.makeText(this, "$doc", Toast.LENGTH_SHORT).show()
        startParticipateActivity(scrutinId!!)


    }

    private fun startParticipateActivity(scrutinId: String){
        val intent = Intent(this, ParticipateVote::class.java)
        intent.putExtra("INTENT_EXTRA_PARTICIPATE_SCRUTINID", scrutinId)
        startActivity(intent)
    }

    private fun startMainCharacterActivity() {
        val intent = Intent(this, MainCharacterActivity::class.java)
        startActivity(intent)
    }

}