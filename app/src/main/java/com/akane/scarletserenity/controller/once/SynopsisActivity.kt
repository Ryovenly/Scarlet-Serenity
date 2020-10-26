package com.akane.scarletserenity.controller.once

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.controller.character.CharacterActivity

class SynopsisActivity : BaseActivity() {


    private lateinit var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_synopsis)

        mp = MediaPlayer.create(this, R.raw.rereation)
        mp.isLooping = true
        mp.setVolume(0.7f, 0.7f)

        mp.start()

        val mTextView = findViewById<TextView>(R.id.textView) as TextView
        mTextView.text = getString(R.string.synopsis)

        val mButton = findViewById<Button>(R.id.button) as Button
        mButton.text = getString(R.string.understood)

        mButton.setOnClickListener() {
            startCharacterActivity()
        }
    }


    private fun startCharacterActivity() {
        mp.stop()
        val intent = Intent(this, CharacterActivity::class.java)
        startActivity(intent)
    }
}