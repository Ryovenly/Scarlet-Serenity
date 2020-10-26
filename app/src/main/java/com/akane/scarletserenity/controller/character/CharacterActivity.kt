package com.akane.scarletserenity.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.once.CreateCharacterActivity
import com.akane.scarletserenity.model.character.Character
import com.akane.scarletserenity.model.character.CharacterHelper
import com.akane.scarletserenity.model.user.User
import com.akane.scarletserenity.model.user.UserHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_character.*


class CharacterActivity : BaseActivity()  {
    val email = user?.email
    val photoUrl = user?.photoUrl
    var modelCurrentUser: User? =
        User("", "")
    lateinit var modelCharacter: Character
    val TAG = "chaud"


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        val mEmail = findViewById<TextView>(R.id.user_mail) as TextView
        mEmail.text = email
        val mlogout = findViewById<Button>(R.id.bt_logout) as Button
        getDocument()
        checkCharacter()

        speech(getString(R.string.characterSpeech))

        Glide.with(this)
            .load(photoUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(findViewById<ImageView>(R.id.photo_url) as ImageView)

        mlogout.setOnClickListener(){
            signOutUserFromFirebase()
        }


    }

    private fun checkCurrentUser(): Boolean {
        // [START check_current_user]
        val user = Firebase.auth.currentUser
        return user != null
        // [END check_current_user]
    }

    private fun signOutUserFromFirebase() {
        AuthUI.getInstance().signOut(this)
        startMainActivity()
    }
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("ResourceType")
    private fun checkCharacter() {
        val docRef = CharacterHelper.getCharacter(user?.uid)
        val mAvatar = findViewById<ImageView>(R.id.iv_character) as ImageView
        docRef
            .addOnSuccessListener { document ->

                if (document.data.isNullOrEmpty()){
                    // go create character activity
                    mAvatar.setImageResource(R.drawable.add_character)
                    var mContent = findViewById<TextView>(R.id.tv_content) as TextView
                    mContent.setText(R.string.content_createcharacter)
                    Log.d(TAG, "Y a pas de personnage encore $document")
                    mAvatar.setOnClickListener {
                        startCreateCharacterActivity()
                    }

                } else {
                    // go Menu character Activity
                    val ava = R.drawable.female_1
                    mAvatar.setImageResource(ava)
                    getCharacter()
                    mAvatar.setOnClickListener {
                        startMainCharacterActivity()
                    }

                    Log.d(TAG, "Y a un personnage")


                }
            }
    }

    private fun getDocument() {
        UserHelper.getUser(user?.uid)
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    modelCurrentUser = document.toObject(User::class.java)
                    Log.d(TAG, "user: $modelCurrentUser")
                    var mName = findViewById<TextView>(R.id.user_name) as TextView
                    mName.text = modelCurrentUser?.username
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    @SuppressLint("SetTextI18n")
    private fun attributesCharacter(){
        val mContent = findViewById<TextView>(R.id.tv_content) as TextView
        mContent.setText(R.string.content_character)
        val mCharacterName = findViewById<TextView>(R.id.tv_characterName) as TextView
        mCharacterName.text = modelCharacter.pseudo

        val mStrength = findViewById<TextView>(R.id.tv_strength) as TextView
        val mIntelligence = findViewById<TextView>(R.id.tv_intelligence) as TextView
        val mAgility = findViewById<TextView>(R.id.tv_agility) as TextView
        val mLuck = findViewById<TextView>(R.id.tv_luck) as TextView

        mStrength.text =
            "${getString(R.string.strength)}: ${modelCharacter.strength}"
        mIntelligence.text =
            "${getString(R.string.intelligence)}: ${modelCharacter.intelligence}"
        mAgility.text =
            "${getString(R.string.agility)}: ${modelCharacter.agility}"
        mLuck.text =
            "${getString(R.string.luck)}: ${modelCharacter.luck}"

        val gender= modelCharacter?.gender

        if (gender == "FEMALE"){
            iv_character.setImageResource(R.drawable.female_1)
        } else {
            iv_character.setImageResource(R.drawable.male_1)
        }
    }

    private fun getCharacter() {
        CharacterHelper.getCharacter(user?.uid)
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    modelCharacter = documentSnapshot.toObject(Character::class.java)!!
                    attributesCharacter()

                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    private fun startCreateCharacterActivity() {
        val intent = Intent(this, CreateCharacterActivity::class.java)
        startActivity(intent)
    }

    private fun startMainCharacterActivity() {
        val intent = Intent(this, MainCharacterActivity::class.java)
        startActivity(intent)
    }
}

