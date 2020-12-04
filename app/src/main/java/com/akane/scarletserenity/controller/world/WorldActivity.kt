package com.akane.scarletserenity.controller.world

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.model.character.Character
import com.akane.scarletserenity.model.character.CharacterHelper
import com.akane.scarletserenity.model.world.World
import com.akane.scarletserenity.model.world.WorldHelper
import com.akane.scarletserenity.view.WorldAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_world.*

class WorldActivity: BaseActivity() {

    var worldRegistration: ListenerRegistration? = null
    val worlds = ArrayList<World>()
    val firestore = FirebaseFirestore.getInstance()
    lateinit var modelCharacter: Character

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_world)

        initListRecyclerView()
        onViewListener()

        getModelCharacter()

    }


    private fun onViewListener(){
        bt_leave.setOnClickListener {
            finish()
        }
    }

    private fun initListRecyclerView() {
        if (user == null)
            return
        rv_world.layoutManager = LinearLayoutManager(this)
        val adapter = WorldAdapter(worlds)
        rv_world.adapter = adapter
        listenForWorlds()
    }


    private fun listenForWorlds() {

        worldRegistration = WorldHelper.getWorldCollection()
            ?.addSnapshotListener { worldsData, exception ->
                if (worldsData == null || worldsData.isEmpty)
                    return@addSnapshotListener
                for (world in worldsData.documents) {
                    worlds.add(
                        World(
                            world["id"] as Long,
                            world["name"] as String,
                            world["img"] as String
                        )
                    )
                }
                worlds.sortBy { it.id }
                rv_world.adapter?.notifyDataSetChanged()
            }

    }

    private fun getModelCharacter() {
        CharacterHelper.getCharacter(user?.uid)
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    modelCharacter = documentSnapshot.toObject(Character::class.java)!!

                    viewStamina()

                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }

    @SuppressLint("SetTextI18n")
    private fun viewStamina(){
        val mStaminaBar = findViewById<ProgressBar>(R.id.pb_stamina)

        val mStamina = findViewById<TextView>(R.id.tv_stamina)

        val staminaMax:Int? = modelCharacter?.staminaMax
        val stamina:Int? = modelCharacter?.stamina

        val calculStamina = stamina!! * 100 / staminaMax!!
        // Remplissage des barres
        mStaminaBar.progress = calculStamina

        Log.d("TAG", "mes hp $calculStamina" )
        mStamina.text = "${getString(R.string.stamina)} : $stamina / $staminaMax"
    }

}