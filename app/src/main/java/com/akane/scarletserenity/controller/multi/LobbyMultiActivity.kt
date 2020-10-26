package com.akane.scarletserenity.controller.multi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.model.character.Character
import com.akane.scarletserenity.model.character.CharacterHelper
import com.akane.scarletserenity.model.shifumi.ShifumiHelper
import com.akane.scarletserenity.model.shifumi.ShifumiRoom
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.activity_multi_lobby.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class LobbyMultiActivity: BaseActivity() {

    lateinit var room: ShifumiRoom
    lateinit var modelCurrentCharacter: Character
    var isThereRoom = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_lobby)

        setViewListeners()

    }


    private fun setViewListeners() {
        bt_shifumi.setOnClickListener {
            checkRoomShifumi()
        }
        bt_leave.setOnClickListener {
            finish()
        }
    }

    private fun checkRoomShifumi(){

        getCharacter()

        // check open room

        ShifumiHelper.checkShifumiRoom()
            ?.addOnSuccessListener { documents ->
                for (document in documents) {
                    val roomId = document.id

                    ShifumiHelper.updatePlayerJoined(roomId,modelCurrentCharacter.pseudo, user?.uid)

                    room = document.toObject(ShifumiRoom::class.java)!!
                    Log.w("TAG", "vide ok: $room ")

                    isThereRoom = true

                    enterRoom(roomId)

                    Log.w("TAG", "création ok: $document ")

                }


                if(isThereRoom == true){
                    Log.w("TAG", "il y a une room déjà crée ")
                }else{
                    // create room
                    val newRoom =
                        ShifumiRoom(
                            user?.uid, modelCurrentCharacter.pseudo, "", "", "open",
                            Timestamp.now(), ""
                        )

                    val roomId = ShifumiHelper.getRoomId()

                    ShifumiHelper.createShifumiRoom(roomId!!,newRoom)

                    // On se met en attente du 2ème joueur

                    var isJoined = false

                    GlobalScope.async {
                        isJoined = checkOpponent(roomId)
                        if (isJoined){
                            enterRoom(roomId)
                            Log.w("TAG", "J'entre ")
                        }
                        Log.w("TAG", "joined $isJoined ")
                    }


                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(getString(R.string.matchmaking_title))
                        .setMessage(getString(R.string.matchmaking_description))
                        .setCancelable(true)
                        .setNegativeButton(
                        "Leave"
                        ) { dialog, which-> dialog.cancel()
                            ShifumiHelper.deleteShifumiRoom(roomId) }
                        .create()
                        .show()
                    Log.w("TAG", "Pas de room donc création ")


                }


                Log.w("TAG", "ça passe sur le check: ")
                //

            }
            ?.addOnFailureListener { exception ->

                Log.w("TAG", "Création ", exception)
            }
    }

    suspend fun checkOpponent(roomId: String): Boolean {

        var joined = false

        while (!joined) {
            Log.w("TAG", "Boucle $joined ")
            delay(1000)
            ShifumiHelper.getShifumiRoom(roomId)
                ?.addOnSuccessListener { document ->
                    if (document.get("status") == "joined") {
                        joined = true
                    }

                }

        }
        Log.w("TAG", "joined $joined ")
        return joined

    }

    private fun enterRoom(roomId: String){
        val intent = Intent(this, ShifumiActivity::class.java)
        intent.putExtra("INTENT_EXTRA_SHIFUMI_ROOMID", roomId)
        startActivity(intent)
    }


    private fun getCharacter() {
        CharacterHelper.getCharacter(user?.uid)
            .addOnSuccessListener { document ->
                if (document != null) {
                    modelCurrentCharacter = document.toObject(Character::class.java)!!
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }
}