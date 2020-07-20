package com.akane.scarletserenity.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.Character
import com.akane.scarletserenity.model.CharacterHelper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainCharacterActivity : AppCompatActivity(){

    val user = Firebase.auth.currentUser
    lateinit var modelCharacter: Character

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_character)


        getCharacter()

        val mButton = findViewById<Button>(R.id.bt_chat) as Button
        //mButton.text = getString(R.string.understood)

        mButton.setOnClickListener() {
            startChatActivity()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getCharacter() {
        CharacterHelper.getCharacter(user?.uid)
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    modelCharacter = documentSnapshot.toObject(Character::class.java)!!

                    val mHealthBar = findViewById<ProgressBar>(R.id.pb_health) as ProgressBar
                    val mManaBar = findViewById<ProgressBar>(R.id.pb_mana) as ProgressBar

                    val mHealth = findViewById<TextView>(R.id.tv_health) as TextView
                    val mMana = findViewById<TextView>(R.id.tv_mana) as TextView

                    val hpMax:Int? = modelCharacter?.hpMax
                    val hp:Int? = modelCharacter?.hp
                    val manaMax:Int? = modelCharacter?.manaMax
                    val mana:Int? = modelCharacter?.mana

                    val calculHp = hp!! * 100 / hpMax!!
                    val calculMana = mana!! * 100 / manaMax!!
                    // Remplissage des barres
                    mHealthBar.progress = calculHp
                    mManaBar.progress = calculMana

                    Log.d("TAG", "mes hp $calculHp mes mana $calculMana" )
                    mHealth.text = "${getString(R.string.health)} : $hp / $hpMax"
                    mMana.text = "${getString(R.string.mana)} : $mana / $manaMax"


                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }

    private fun startChatActivity() {
        val intent = Intent(this, LobbyActivity::class.java)
        startActivity(intent)
    }

}