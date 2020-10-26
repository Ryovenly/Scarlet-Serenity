package com.akane.scarletserenity.controller

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.akane.scarletserenity.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.properties.Delegates

open class BaseActivity: AppCompatActivity(),TextToSpeech.OnInitListener {

    val user = Firebase.auth.currentUser
    val db = FirebaseFirestore.getInstance()
    lateinit var tts:TextToSpeech
    val dataEmp = "gs://scarlet-serenity.appspot.com/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

    }

    // Quitter le jeu en appuyant sur le bouton retour

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return finishAffinity()
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun speech(text:String){

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts

            // check localLanguage

            val localLanguage = Locale.getDefault().getLanguage()
            var result by Delegates.notNull<Int>()

            // fr
            if (localLanguage == "fr"){
                result = tts!!.setLanguage(Locale.FRANCE)
            }else {
                result = tts!!.setLanguage(Locale.US)
            }



            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                Log.e("TTS","Marche pas")
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }

    public override fun onDestroy() {
        // Shutdown TTS
        tts!!.stop()
        tts!!.shutdown()
        super.onDestroy()
    }

}