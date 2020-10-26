package com.akane.scarletserenity.controller.once

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.annotation.RequiresApi
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import kotlinx.android.synthetic.main.activity_speech.*
import java.util.*

class SpeechActivity: BaseActivity() {

    private var textToSpeech: TextToSpeech? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech)

        textToSpeech = TextToSpeech(this,this)

        onViewListener()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onViewListener(){
        bt_leave.setOnClickListener(){
            finish()
        }

        bt_speech.isEnabled = false
        bt_speech.setOnClickListener(){
            speak()
        }
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.FRENCH)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                bt_speech!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun speak(){
        val text = et_speech!!.text.toString()
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
        super.onDestroy()
    }

}