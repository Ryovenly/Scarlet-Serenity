package com.akane.scarletserenity.controller.vote

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.model.GlideApp
import com.akane.scarletserenity.model.character.Character
import com.akane.scarletserenity.model.character.CharacterHelper
import com.akane.scarletserenity.model.vote.Purpose
import com.akane.scarletserenity.model.vote.Scrutin
import com.akane.scarletserenity.model.vote.ScrutinHelper
import com.akane.scarletserenity.model.vote.Voter
import com.akane.scarletserenity.view.PurposeParticipateAdapter
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_participate_vote.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.days

class ParticipateVote: BaseActivity(),PurposeParticipateAdapter.ItemClickListener {

    var purposes = ArrayList<Purpose>()
    var voters = 0
    private lateinit var scrutin:Scrutin
    private lateinit var purpose: Purpose
    lateinit var adapter: PurposeParticipateAdapter
    var purposeRegistration: ListenerRegistration? = null

    lateinit var modelCharacter: Character

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participate_vote)


        getScrutin()
        initListRecyclerView()
        onViewListener()

    }

    private fun onViewListener(){
        bt_leave.setOnClickListener {
            finish()
        }

        bt_voters_size.setOnClickListener {
            initVoters()
        }
    }


    private fun initListRecyclerView() {

        rv_purpose_create.layoutManager = LinearLayoutManager(this)
        adapter = PurposeParticipateAdapter(purposes)
        rv_purpose_create.adapter = adapter
        adapter.setClickListener(this)
        listenForPurposes()
    }


    private fun getScrutin() {

        val scrutinId = intent.getStringExtra("INTENT_EXTRA_PARTICIPATE_SCRUTINID")

        ScrutinHelper.getScrutin(scrutinId)
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

    private fun listenForPurposes() {

        val scrutinId = intent.getStringExtra("INTENT_EXTRA_PARTICIPATE_SCRUTINID")
        voters = 0

        purposeRegistration = ScrutinHelper.getPurposeCollection(scrutinId)
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
                rv_purpose_create.adapter?.notifyDataSetChanged()
                tv_voters.text = "Nombre de votants : $voters"
               // initVoters()
                Log.d("TAG", "Voters : $voters ", exception)
            }

    }

    override fun onItemClick(view: View?, position: Int) {

        val scrutinId = intent.getStringExtra("INTENT_EXTRA_PARTICIPATE_SCRUTINID")
        var isVoted = false

//        for (purpose in purposes) {
//            ScrutinHelper.getVoterCollection(scrutinId, purpose.title)
//                ?.whereEqualTo("id", user?.uid.toString())
//                ?.get()
//                ?.addOnSuccessListener { votersData->
//                    votersData.contains(element = position.days)
//                    for (voter in votersData){
//                        isVoted = true
//                        Log.d("TAG", "Voters loop : $isVoted " )
//                    }
//                }

            if(isVoted == false) {
                Log.d("TAG", "Voters 1 : $isVoted " )
                view?.findViewById<Button>(R.id.bt_voter)?.text = "A voté"

                CharacterHelper.getCharacter(user?.uid)
                    .addOnSuccessListener { documentSnapshot ->
                        modelCharacter = documentSnapshot.toObject(Character::class.java)!!
                        val scrutinId = intent.getStringExtra("INTENT_EXTRA_PARTICIPATE_SCRUTINID")
                        ScrutinHelper.createVote(
                            scrutinId,
                            purposes[position].title,
                            Voter(modelCharacter.pseudo, user?.uid),
                            user?.uid!!
                        )
                    }


                Toast.makeText(this, "COUCOU", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("TAG", "Voters 2 : $isVoted " )
                view?.findViewById<Button>(R.id.bt_voter)?.text = "Déjà voté"
                Toast.makeText(this, "Déjà Voté ^^", Toast.LENGTH_SHORT).show()
            }


   //     }


// }

    }


    @SuppressLint("SetTextI18n")
    private fun initScrutin(){

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

    @SuppressLint("SetTextI18n")
    private fun initVoters(){
        tv_voters.text = "Nombre de votants : $voters"
        Log.d("TAG", "Voters : $voters ")
    }

}