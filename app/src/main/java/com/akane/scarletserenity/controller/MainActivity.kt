package com.akane.scarletserenity.controller

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.UserHelper
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {


    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        handleResponseAfterSignIn(requestCode, resultCode, data!!)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Oncreate", "test")

        // variables

        val mBtLogin = findViewById<Button>(R.id.btLogin) as Button
        mBtLogin.setText(R.string.bt_login)

        // music
        mp = MediaPlayer.create(this, R.raw.airtone)
        mp.isLooping = true
        mp.setVolume(0.7f, 0.7f)
        totalTime = mp.duration

        mp.start()
        // reNovation by airtone (c) copyright 2019 Licensed under a Creative Commons Attribution (3.0) license. http://dig.ccmixter.org/files/airtone/60674


// Create and launch sign-in intent

        mBtLogin.setOnClickListener()
        {
            if (this.checkCurrentUser()) {
                Log.d("Log", "Changement d'activity")
                mBtLogin.setText(R.string.bt_login2)
                createUserInFirestore()
                mp.stop()
                this.startCharacterActivity()
            } else {
                this.startSignInActivity()
            }
        }

    }


    val user = Firebase.auth.currentUser

    private fun createUserInFirestore(){

        if (this.checkCurrentUser() != null){

            val uid = this.user?.uid
            val username = this.user?.displayName

            UserHelper.createUser(uid, username)
            Log.d("ça marche", "on est là")

        }
    }

    private fun startSignInActivity(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

        companion object {

            private const val RC_SIGN_IN = 123
        }

    private fun checkCurrentUser(): Boolean {
        // [START check_current_user]
        val user = Firebase.auth.currentUser
        return user != null
        // [END check_current_user]
    }

    private fun startCharacterActivity() {
        val intent = Intent(this, CharacterActivity::class.java)
        startActivity(intent)
    }

    protected fun onFailureListener(): OnFailureListener? {
        return OnFailureListener {
            Toast.makeText(
                applicationContext,
                getString(R.string.error_unknown_error),
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun handleResponseAfterSignIn(
        requestCode: Int,
        resultCode: Int,
        data: Intent
    ) {
        val response = IdpResponse.fromResultIntent(data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) { // SUCCESS
                createUserInFirestore()
                //showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed))
            }
//            else { // ERRORS
//                if (response == null) {
//                    //showSnackBar(
//                        this.coordinatorLayout,
//                        getString(R.string.error_authentication_canceled)
//                    )
//                } else if (response.getErrorCode() === ErrorCodes.NO_NETWORK) {
//                    showSnackBar(this.coordinatorLayout, getString(R.string.error_no_internet))
//                } else if (response.getErrorCode() === ErrorCodes.UNKNOWN_ERROR) {
//                    showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error))
//                }
//            }
        }
    }

}
