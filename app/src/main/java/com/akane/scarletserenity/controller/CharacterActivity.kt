package com.akane.scarletserenity.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.Character
import com.akane.scarletserenity.model.CharacterHelper
import com.akane.scarletserenity.model.User
import com.akane.scarletserenity.model.UserHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class CharacterActivity : AppCompatActivity() {
    val user = Firebase.auth.currentUser

    //val name = user?.displayName
    val email = user?.email
    val photoUrl = user?.photoUrl
    var modelCurrentUser: User? = User("test","test")
    lateinit var modelCharacter: Character
    val TAG = "chaud"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)



        //val currentUser = UserHelper.getUser(this.user?.uid).javaClass
        //getCurrentUserFromFirestore()
        //this.updateUIWhenCreating()

        Log.d("ça marche", "on est là $modelCurrentUser")
        Log.d("ça marche", "on est là ${modelCurrentUser?.username}")
//        var mName = findViewById<TextView>(R.id.user_name) as TextView
//        mName.text = modelCurrentUser?.username
        var mEmail = findViewById<TextView>(R.id.user_mail) as TextView
        mEmail.text = email
        var mlogout = findViewById<Button>(R.id.bt_logout) as Button
        getDocument()
        checkCharacter()
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Get Post object and use the values to update the UI
//                val post = dataSnapshot.getValue<User>()
//                // ...
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
//                // ...
//            }
//        }
//        postReference.addValueEventListener(postListener)

        if (this.checkCurrentUser() != null){
            Glide.with(this)
                .load(photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(findViewById<ImageView>(R.id.photo_url) as ImageView);

        }

        mlogout.setOnClickListener(){
            signOutUserFromFirebase()
        }


    }


    // 4 - Get Current User from Firestore
    private fun getCurrentUserFromFirestore() {
        UserHelper.getUser(user?.uid)
            .addOnSuccessListener { documentSnapshot ->
                modelCurrentUser = documentSnapshot.toObject(User::class.java)
            }
    }

    // 4 - Get Current User from Firestore
    private fun getCurrentUserFromFirestoreTrue() {
        UserHelper.getUser(user?.uid)
            .addOnSuccessListener { document ->
                modelCurrentUser = document.toObject(User::class.java)
            }
    }

    private fun updateUIWhenCreating() {
        // 5 - Get additional data from Firestore
        UserHelper.getUser(this.user?.uid)
            .addOnSuccessListener { documentSnapshot ->
                val currentUser = documentSnapshot.toObject(User::class.java)
                val username =
                    if (TextUtils.isEmpty(currentUser?.username)) getString(R.string.info_no_username_found) else currentUser?.username
//                var mName = findViewById<TextView>(R.id.user_name) as TextView
//                mName.text = "username"
                Log.d("ça marche", "username $username")
                Log.d("ça marche", "currentUser $currentUser")
            }
    }



    private fun checkCurrentUser(): Boolean {
        // [START check_current_user]
        val user = Firebase.auth.currentUser
        return user != null
        // [END check_current_user]
    }

    private fun signOutUserFromFirebase() {
        AuthUI.getInstance()
            .signOut(this)
        //.addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK))
        startMainActivity()
    }
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun getUserProfile() {
        // [START get_user_profile]

        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user?.email
            val photoUrl = user?.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }
        // [END get_user_profile]
    }

    @SuppressLint("ResourceType")
    private fun checkCharacter() {
        var docRef = CharacterHelper.getCharacter(user?.uid)
        var mAvatar = findViewById<ImageView>(R.id.iv_character) as ImageView
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
    private fun getCharacter() {
        CharacterHelper.getCharacter(user?.uid)
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    modelCharacter = documentSnapshot.toObject(Character::class.java)!!
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

