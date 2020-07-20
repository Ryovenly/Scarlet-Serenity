package com.akane.scarletserenity.controller

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.CharacterHelper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

class CreateCharacterActivity: AppCompatActivity() {

    val user = Firebase.auth.currentUser
    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_character)

        // music
        mp = MediaPlayer.create(this, R.raw.rereation)
        mp.isLooping = true
        mp.setVolume(0.7f, 0.7f)
        totalTime = mp.duration

        mp.start()

        val mQuestion = findViewById<TextView>(R.id.tv_question) as TextView
        mQuestion.setText(R.string.questionCreateCharacter)

        val mGenderQuestion = findViewById<TextView>(R.id.tv_gender) as TextView
        mGenderQuestion.setText(R.string.questionGender)

        val mPseudo = findViewById<EditText>(R.id.in_pseudo) as EditText


        val mBtValidation = findViewById<Button>(R.id.bt_validation) as Button
        mBtValidation.setText(R.string.bt_validationCreateCharacter)

        var mGender = ""

        val mFemale = findViewById<ImageButton>(R.id.ib_girl) as ImageButton
        mFemale.alpha = 0.4F
        val mMale = findViewById<ImageButton>(R.id.ib_boy) as ImageButton
        mMale.alpha = 0.4F

        mFemale.setOnClickListener {
            // Passage du paramètre mgender en Female + imagebutton modifier
            mGender = "FEMALE"
            mFemale.alpha = 1F
            mMale.alpha = 0.4F
        }



        mMale.setOnClickListener {
            // Passage du paramètre mgender en Male + imagebutton modifier
            mGender = "MALE"
            mMale.alpha = 1F
            mFemale.alpha = 0.4F
        }


        mBtValidation.setOnClickListener {
            // check pseudo empty?
            if (mPseudo.text.toString() == ""){
                // afficher Toast disant qu'il faut rentrer un pseudo -> Vérifier si le pseudo est pris?

                Toast.makeText(getApplicationContext(), getString(R.string.checkPseudo), Toast.LENGTH_LONG).show();

            }
            // check avatar (avatar imposé pour l'instant)
            //else if () {}

            // check genre selectionné
            else if (mGender == "MALE" || mGender == "FEMALE"){
                // create character

                var mStrength = 5
                var mIntelligence = 5

            CharacterHelper.createCharacter(user?.uid,
                mPseudo.text.toString(),
                mGender,
                1000,
                25*mStrength,
                20*mIntelligence,
                100,
                100,
                25*mStrength,
                20*mIntelligence,
                100,
                100,
                mStrength,
                mIntelligence,
                5,
                5,
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString())

            //Toast.makeText(getApplicationContext(), getString(R.string.createCharacter), Toast.LENGTH_LONG).show();
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.createCharacter))
                builder.setMessage(getString(R.string.createCharacterMessage))
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(R.string.createCharacterMessageButton) { dialog, which ->
                    startQuizActivity()
                }
                builder.show()
            } else
            {
                // toast remplir les champs svp
                Toast.makeText(getApplicationContext(), getString(R.string.checkAll), Toast.LENGTH_LONG).show();
            }

        }

    }


    private fun startQuizActivity() {
        mp.stop()
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }


}