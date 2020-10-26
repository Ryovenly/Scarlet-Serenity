package com.akane.scarletserenity.controller.once

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.model.monster.Monster
import com.akane.scarletserenity.view.MonsterAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_bestiaire.*

class BestiaireActivity : BaseActivity(){

    var monsterRegistration: ListenerRegistration? = null
    val monsters = ArrayList<Monster>()
    val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bestiaire)

        initListRecyclerView()
        onViewListener()

    }

    private fun onViewListener(){
        bt_leave.setOnClickListener {
            finish()
        }
    }


    private fun initListRecyclerView() {
        if (user == null)
            return
        rv_bestiaire.layoutManager = LinearLayoutManager(this)
        val adapter = MonsterAdapter(monsters)
        rv_bestiaire.adapter = adapter
        listenForMonsters()
    }

    private fun listenForMonsters() {

        monsterRegistration = firestore.collection("monsters")
            .document("data")
            .collection("monster")
            .addSnapshotListener { monstersData, exception ->
                if (monstersData == null || monstersData.isEmpty)
                    return@addSnapshotListener
                for (monster in monstersData.documents) {
                    monsters.add(
                        Monster(
                            monster["name"] as String,
                            monster["description"] as String,
                            monster["img"] as String,
                            monster["hp"] as Long,
                            monster["strength"] as Long,
                            monster["intelligence"] as Long,
                            monster["agility"] as Long,
                            monster["luck"] as Long,
                            monster["po"] as Long,
                            monster["reward"] as String
                        )
                    )
                }
                rv_bestiaire.adapter?.notifyDataSetChanged()
            }

    }

}