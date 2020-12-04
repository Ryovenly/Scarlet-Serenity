package com.akane.scarletserenity.controller.character

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.controller.once.BestiaireActivity
import com.akane.scarletserenity.controller.multi.LobbyActivity
import com.akane.scarletserenity.controller.multi.LobbyMultiActivity
import com.akane.scarletserenity.controller.once.SpeechActivity
import com.akane.scarletserenity.controller.vote.VoteMainActivity
import com.akane.scarletserenity.controller.world.WorldActivity
import com.akane.scarletserenity.model.GlideApp
import com.akane.scarletserenity.model.character.Character
import com.akane.scarletserenity.model.character.CharacterHelper
import com.akane.scarletserenity.model.monster.Monster
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main_character.*
import kotlinx.android.synthetic.main.activity_profil_character.*

class MainCharacterActivity : BaseActivity(){

    lateinit var modelCharacter: Character
    val monster: Monster =
        Monster(
            "",
            "",
            "",
            0,
            0,
            0,
            0,
            0,
            0,
            ""
        )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_character)

        getModelCharacter()

        setViewListeners()

    }
    private fun setViewListeners() {
        val mButton = findViewById<Button>(R.id.bt_chat) as Button
        //mButton.text = getString(R.string.understood)

        mButton.setOnClickListener {
            startChatActivity()
        }

        acces_multi.setOnClickListener {
            startMultiActivity()
        }

        bt_star.setOnClickListener {
            startSpeechActivity()
        }

        iv_bestiaire.setOnClickListener {
            startBestiaireActivity()
           // MonsterHelper.createMonster(monster)
        }

        bt_vote_activity.setOnClickListener {
            startVoteActivity()
        }

        ivCharacterAvatar.setOnClickListener {
            startProfileCharacterActivity()
        }

        bt_world.setOnClickListener {
            startWorldActivity()
        }

    }



    private fun viewGender(){
        val gender = modelCharacter?.gender
        val avatar = modelCharacter.img!!

        if (avatar.isNullOrEmpty()){
            if (gender == "FEMALE"){
                ivCharacterAvatar.setImageResource(R.drawable.female_1)
            } else {
                ivCharacterAvatar.setImageResource(R.drawable.male_1)
            }
        } else{
            val storageReference = Firebase.storage.getReferenceFromUrl(modelCharacter.img!!)

            GlideApp.with(applicationContext)
                .load(storageReference)
                .into(ivCharacterAvatar)
        }


        if (gender == "FEMALE"){
            ivCharacterBody.setImageResource(R.drawable.girl)
        } else {
            ivCharacterBody.setImageResource(R.drawable.boy)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun viewHpMana(){
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
    }

    @SuppressLint("SetTextI18n")
    private fun getModelCharacter() {
        CharacterHelper.getCharacter(user?.uid)
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    modelCharacter = documentSnapshot.toObject(Character::class.java)!!

                    viewGender()
                    viewHpMana()


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

    private fun startMultiActivity() {
        val intent = Intent(this, LobbyMultiActivity::class.java)
        startActivity(intent)
    }

    private fun startSpeechActivity() {
        val intent = Intent(this, SpeechActivity::class.java)
        startActivity(intent)
    }

    private fun startBestiaireActivity() {
        val intent = Intent(this, BestiaireActivity::class.java)
        startActivity(intent)
    }

    private fun startVoteActivity() {
        val intent = Intent(this, VoteMainActivity::class.java)
        startActivity(intent)
    }

    private fun startProfileCharacterActivity() {
        val intent = Intent(this, ProfilCharacterActivity::class.java)
        startActivity(intent)
    }

    private fun startWorldActivity() {
        val intent = Intent(this, WorldActivity::class.java)
        startActivity(intent)
    }

}