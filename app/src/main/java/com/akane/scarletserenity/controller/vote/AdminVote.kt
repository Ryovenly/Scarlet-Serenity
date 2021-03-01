package com.akane.scarletserenity.controller.vote

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.model.GlideApp
import com.akane.scarletserenity.model.character.Character
import com.akane.scarletserenity.model.vote.Purpose
import com.akane.scarletserenity.model.vote.Scrutin
import com.akane.scarletserenity.model.vote.ScrutinHelper
import com.akane.scarletserenity.view.PurposeAdminAdapter
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_admin_vote.*
import java.text.SimpleDateFormat
import java.util.*

class AdminVote : BaseActivity() {

    var purposes = ArrayList<Purpose>()
    var voters = 0
    var isVoted = false
    lateinit var scrutin:Scrutin
    lateinit var purpose: Purpose
    lateinit var adapter: PurposeAdminAdapter
    var purposeRegistration: ListenerRegistration? = null

    lateinit var modelCharacter: Character

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_vote)

        getScrutin()
        onViewListener()
        initListRecyclerView()



    }


    fun onViewListener(){
        bt_leave.setOnClickListener { finish() }

        bt_voters_size.setOnClickListener { initVoters() }
    }

    fun initListRecyclerView() {

        val scrutinId = intent.getStringExtra("INTENT_EXTRA_PARTICIPATE_SCRUTINID")
        rv_purpose_admin.layoutManager = LinearLayoutManager(this)
        adapter = PurposeAdminAdapter(purposes,scrutinId!!)
        rv_purpose_admin.adapter = adapter

        listenForPurposes()

    }


    fun getScrutin() {

        val scrutinId = intent.getStringExtra("INTENT_EXTRA_PARTICIPATE_SCRUTINID")

        ScrutinHelper.getScrutin(scrutinId!!)
            ?.addOnSuccessListener { document ->
                if (document != null) {
                    scrutin = document.toObject(Scrutin::class.java)!!
                    initScrutin()
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            ?.addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }

    @SuppressLint("SetTextI18n")
    fun initScrutin(){

        tv_topic.text = scrutin.title

        val storageReference = Firebase.storage.getReferenceFromUrl(scrutin.img)

        GlideApp.with(applicationContext)
            .load(storageReference)
            .into(iv_topic)

        tv_description.text = "${getString(R.string.description)} : ${scrutin.description}"

        val formatDate = SimpleDateFormat("EEEE dd MMMM yyyy HH:mm", Locale.FRANCE)

        tv_start_date.text = "${getString(R.string.start_date)} : ${formatDate.format(scrutin.date_start.toDate())}"
        tv_end_date.text = "${getString(R.string.end_date)} : ${formatDate.format(scrutin.date_end.toDate())}"


    }

    private fun listenForPurposes() {

        val scrutinId = intent.getStringExtra("INTENT_EXTRA_PARTICIPATE_SCRUTINID")
        voters = 0

        purposeRegistration = ScrutinHelper.getPurposeCollection(scrutinId!!)
            ?.addSnapshotListener { purposesData, exception ->
                if (purposesData == null || purposesData.isEmpty)
                    return@addSnapshotListener
                for (purpose in purposesData.documents) {
                    purposes.add(
                        Purpose(
                            purpose["title"] as String,
                            purpose["description"] as String
                        )
                    )
                    ScrutinHelper.getVoterCollection(scrutinId,purpose["title"] as String)?.addSnapshotListener{voterData,exception ->
                        if (voterData == null || voterData.isEmpty)
                            return@addSnapshotListener
                        voters += voterData.size()
                        Log.d("TAG", "Voters : $voters ", exception)
                    }
                }
                rv_purpose_admin.adapter?.notifyDataSetChanged()
                tv_voters.text = "Nombre de votants : $voters"
                // initVoters()
                Log.d("TAG", "Voters : $voters ", exception)

            }

    }


    @SuppressLint("SetTextI18n")
    private fun initVoters(){
        tv_voters.text = "Nombre de votants : $voters"
        Log.d("TAG", "Voters : $voters ")
    }

}