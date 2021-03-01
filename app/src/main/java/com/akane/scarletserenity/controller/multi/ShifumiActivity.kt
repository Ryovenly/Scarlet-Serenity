package com.akane.scarletserenity.controller.multi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.controller.character.MainCharacterActivity
import com.akane.scarletserenity.controller.vote.VoteMainActivity
import com.akane.scarletserenity.model.character.Character
import com.akane.scarletserenity.model.character.CharacterHelper
import com.akane.scarletserenity.model.shifumi.ShifumiGame
import com.akane.scarletserenity.model.shifumi.ShifumiHelper
import com.akane.scarletserenity.model.shifumi.ShifumiPlayer
import com.akane.scarletserenity.model.shifumi.ShifumiRoom
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.activity_shifumi.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShifumiActivity: BaseActivity() {

    private var roomId: String? = null
    var time = 10000L
    var gameFinish = false
    var isReady = false

    lateinit var countdown_timer: CountDownTimer

    var roomData: ShifumiRoom =
        ShifumiRoom(
            "",
            "aka",
            "",
            "aka",
            "",
            Timestamp.now(),
            ""
        )
    var gameData: ShifumiGame =
        ShifumiGame(
            "",
            "",
            0,
            0,
            10000L,
            false,
            false
        )
    lateinit var player: ShifumiPlayer
    lateinit var playerOpponent: ShifumiPlayer
    lateinit var choicePlayer: String
    lateinit var choiceOpponent: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shifumi)
        setRoomData()
        setFirstPlayersData()
        createGame()
        giveUp()

        choicePlayer()
        //onClickListeners()
        time = gameData.timer

        startTimer(time)




//        GlobalScope.launch{
//            val finish: Boolean = timer()
//
//            if (finish == true){
//                Log.d("TAG", "Timer down")
//            }
//
//        }

    }

    private fun giveUp(){
        bt_giveup.setOnClickListener(){

            // rendre le deuxième joueur vainqueur

            finish()
        }
    }

    private fun onClickListeners(){
        bt_validate.setOnClickListener(){
            if (choicePlayer.isNotEmpty()){

                if (player.userId == roomData.userId1){
                    ShifumiHelper.updateValidatePlayer1(roomId!!)
                }else{
                    ShifumiHelper.updateValidatePlayer2(roomId!!)
                }

                GlobalScope.async {

                    isReady = checkReady()
                    if (isReady){
                        getGameData()
                        startShifumi()
                        Log.w("TAG", "J'entre ")
                    }
                    Log.w("TAG", "joined $isReady ")
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setViewListeners() {

        getRoomData()
        getGameData()



        tv_characterOpponent.text = playerOpponent.pseudo
        tv_characterPlayer.text = player.pseudo



        var scorePlayer = 0
        var scoreOpponent = 0

        if (user?.uid == roomData.userId1){

             scorePlayer = gameData.scorePlayer1
             scoreOpponent = gameData.scorePlayer2

        } else{

             scorePlayer = gameData.scorePlayer2
             scoreOpponent = gameData.scorePlayer1
        }


        tv_oppenentScore.text = "Score : $scoreOpponent"
        tv_playerScore.text = "Score : $scorePlayer"

    }

    private fun getRoomData(){
        roomId = intent.getStringExtra("INTENT_EXTRA_SHIFUMI_ROOMID")
        if (roomId == null) {
            finish()
            return
        }

        ShifumiHelper.getShifumiRoom(roomId!!)
            ?.addOnSuccessListener { document ->
                if (document != null) {
                    roomData = document.toObject(ShifumiRoom::class.java)!!
                } else {
                    Log.d("TAG", "No such document")
                }

            }
    }


    private fun setRoomData(){

        roomId = intent.getStringExtra("INTENT_EXTRA_SHIFUMI_ROOMID")
        if (roomId == null) {
            finish()
            return
        }

        ShifumiHelper.getShifumiRoom(roomId!!)
            ?.addOnSuccessListener { document ->
                if (document != null) {
                    roomData = document.toObject(ShifumiRoom::class.java)!!
                    setFirstPlayersData()
                } else {
                    Log.d("TAG", "No such document")
                }

            }
    }

    private fun setFirstPlayersData(){

        if(roomData.userId1 == user?.uid){
            player = ShifumiPlayer(
                roomData.player1!!,
                roomData.userId1!!,
                0,
                ""
            )
            playerOpponent =
                ShifumiPlayer(
                    roomData.player2!!,
                    roomData.userId2!!,
                    0,
                    ""
                )

            Log.d("TAG", "userId1 = player")
        } else{
            player = ShifumiPlayer(
                roomData.player2!!,
                roomData.userId2!!,
                0,
                ""
            )
            playerOpponent =
                ShifumiPlayer(
                    roomData.player1!!,
                    roomData.userId1!!,
                    0,
                    ""
                )

            Log.d("TAG", "userId2 = player")
        }
        setViewListeners()

    }

    private fun createGame(){
        ShifumiHelper.createShifumiGamePlayer(roomId!!)
    }


    private fun choicePlayer(){
        bt_rockPlayer.setOnClickListener(){
            bt_rockPlayer.alpha = 1F
            bt_paperPlayer.alpha = 0.4F
            bt_scissorsPlayer.alpha = 0.4F

            choicePlayer = "rock"
            setChoicePlayerData(choicePlayer)
        }

        bt_paperPlayer.setOnClickListener(){
            bt_rockPlayer.alpha = 0.4F
            bt_paperPlayer.alpha = 1F
            bt_scissorsPlayer.alpha = 0.4F

            choicePlayer = "paper"
            setChoicePlayerData(choicePlayer)
        }

        bt_scissorsPlayer.setOnClickListener(){
            bt_rockPlayer.alpha = 0.4F
            bt_paperPlayer.alpha = 0.4F
            bt_scissorsPlayer.alpha = 1F

            choicePlayer = "scissors"
            setChoicePlayerData(choicePlayer)
        }
    }


    private fun setChoicePlayerData(choicePlayer: String){

        if (player.userId == roomData.userId1){
            ShifumiHelper.setShifumiGamePlayer1(roomId!!,choicePlayer)
        } else{
            ShifumiHelper.setShifumiGamePlayer2(roomId!!,choicePlayer)
        }
    }


    private fun getGameData(){
        roomId = intent.getStringExtra("INTENT_EXTRA_SHIFUMI_ROOMID")
        if (roomId == null) {
            finish()
            return
        }

        ShifumiHelper.getShifumiGame(roomId!!)?.addOnSuccessListener { document ->
            if (document != null) {
                gameData = document.toObject(ShifumiGame::class.java)!!
            } else {
                Log.d("TAG", "No such document")
            }

        }
    }

    private fun getChoicePlayerData(){
        getRoomData()
        getGameData()

        if (user?.uid == roomData.userId1){
            choicePlayer = gameData.choicePlayer1
            choiceOpponent = gameData.choicePlayer2
        } else{
            choicePlayer = gameData.choicePlayer2
            choiceOpponent = gameData.choicePlayer1
        }
    }

    private fun showChoice(){

        GlobalScope.launch{
            waitShowPlayer()
            waitShowOpponent()
        }

    }

    suspend fun waitShowPlayer() {
        if (choicePlayer == "rock"){
            this.runOnUiThread { bt_choicePlayer.setImageResource(R.drawable.pierre) }
            delay(1000)
            this.runOnUiThread { bt_choicePlayer.setImageResource(R.drawable.nothing) }


        } else if (choicePlayer == "paper"){
            this.runOnUiThread { bt_choicePlayer.setImageResource(R.drawable.feuille) }
            delay(1000)
            this.runOnUiThread{bt_choicePlayer.setImageResource(R.drawable.nothing)}


        } else if (choicePlayer == "scissors"){
            this.runOnUiThread { bt_choicePlayer.setImageResource(R.drawable.ciseaux) }
            delay(1000)
            this.runOnUiThread{bt_choicePlayer.setImageResource(R.drawable.nothing)}


        } else {
            this.runOnUiThread { bt_choicePlayer.setImageResource(R.drawable.nothing) }
            delay(1000)
            this.runOnUiThread{bt_choicePlayer.setImageResource(R.drawable.nothing)}


        }

    }

    suspend fun waitShowOpponent() {

        if (choiceOpponent == "rock"){
            this.runOnUiThread { bt_choiceOpponent.setImageResource(R.drawable.pierre) }
            delay(1000)
            this.runOnUiThread { bt_choiceOpponent.setImageResource(R.drawable.nothing) }


        } else if (choiceOpponent == "paper"){
            this.runOnUiThread { bt_choiceOpponent.setImageResource(R.drawable.feuille) }
            delay(1000)
            this.runOnUiThread { bt_choiceOpponent.setImageResource(R.drawable.nothing) }


        } else if (choiceOpponent == "scissors"){
            this.runOnUiThread { bt_choiceOpponent.setImageResource(R.drawable.ciseaux) }
            delay(1000)
            this.runOnUiThread { bt_choiceOpponent.setImageResource(R.drawable.nothing) }


        } else {
            this.runOnUiThread { bt_choiceOpponent.setImageResource(R.drawable.nothing) }
            delay(1000)
            this.runOnUiThread { bt_choiceOpponent.setImageResource(R.drawable.nothing) }


        }
    }

//    suspend fun timer(){
//
//        getGameData()
//
//        var timer = gameData.timer
//
//        if (user?.uid == roomData.userId1){
//            delay(1000)
//            timer --
//            ShifumiHelper.updateTimer(roomId!!,timer)
//        } else{
//            choicePlayer = gameData.choicePlayer2
//            choiceOpponent = gameData.choicePlayer1
//        }
//
//    }


    private fun startTimer(time_in_seconds: Long) {

        setViewListeners()

        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {

                startShifumi()
                    if (!gameFinish){
                        time = 10000L
                        return startTimer(time)
                    }else{
                            winnerAndFinishActivity()
                    }


                //time = 10000
            }

            override fun onTick(p0: Long) {
                time = p0
                updateTextUI()
            }
        }
        countdown_timer.start()

        //isRunning = true

    }

     fun winnerAndFinishActivity(){

        getRoomData()
//         this.runOnUiThread {Toast.makeText(getApplicationContext(), "Le gagnant est ${roomData.winner}", Toast.LENGTH_LONG).show();}

         val builder = AlertDialog.Builder(this)
         builder.setTitle("Le gagnant est ${roomData.winner}")
//             .setMessage(getString(R.string.matchmaking_description))
             .setCancelable(false)
             .setNegativeButton(
                 "Leave"
             ) { dialog, which-> dialog.cancel()
                 startMainCharacterActivity() }
             .create()
             .show()

    }

    @SuppressLint("SetTextI18n")
    private fun updateTextUI() {
        val seconds = (time / 1000) % 60

        tv_timer.text = "Timer : $seconds"
    }


    suspend fun checkReady(): Boolean {

        var ready = false

        while (!ready) {
            Log.w("TAG", "Boucle $ready ")
            delay(1000)
            ShifumiHelper.getShifumiGame(roomId!!)
                ?.addOnSuccessListener { document ->
                    if (document.get("readyPlayer1") == true && document.get("readyPlayer2") == true) {
                        ready = true
                    }

                }

        }
        Log.w("TAG", "joined $ready ")
        return ready

    }


    private fun startShifumi(){
        getChoicePlayerData()

        shifumi(choicePlayer,choiceOpponent)
    }

    private fun shifumi(choicePlayer: String, choiceOpponent: String){

        getChoicePlayerData()

        showChoice()


            if (choicePlayer == "rock" && choiceOpponent == "scissors"){
                // player win

                // score +1

                if (user?.uid == roomData.userId1){

                    val scorePlayer = gameData.scorePlayer1 + 1
                    ShifumiHelper.updateScorePlayer1(roomId!!,scorePlayer)



                } else{

                    val scorePlayer = gameData.scorePlayer2 + 1
                    ShifumiHelper.updateScorePlayer2(roomId!!,scorePlayer)
                }

                Toast.makeText(getApplicationContext(), getString(R.string.shifumi_win_point), Toast.LENGTH_LONG).show();

            } else if (choicePlayer == "rock" && choiceOpponent == "paper"){
                // player lose

                Toast.makeText(getApplicationContext(), getString(R.string.shifumi_lose_point), Toast.LENGTH_LONG).show();


            } else if (choicePlayer == "rock" && choiceOpponent == "rock"){
                // player draw

                Toast.makeText(getApplicationContext(), getString(R.string.shifumi_draw_point), Toast.LENGTH_LONG).show();


            } else if (choicePlayer == "paper" && choiceOpponent == "rock"){
                // player win

                // score +1

                if (user?.uid == roomData.userId1){

                    val scorePlayer = gameData.scorePlayer1 + 1
                    ShifumiHelper.updateScorePlayer1(roomId!!,scorePlayer)


                } else{

                    val scorePlayer = gameData.scorePlayer2 + 1
                    ShifumiHelper.updateScorePlayer2(roomId!!,scorePlayer)
                }

                Toast.makeText(getApplicationContext(), getString(R.string.shifumi_win_point), Toast.LENGTH_LONG).show();


            } else if (choicePlayer == "paper" && choiceOpponent == "scissors"){
                // player lose

                Toast.makeText(getApplicationContext(), getString(R.string.shifumi_lose_point), Toast.LENGTH_LONG).show();


            } else if (choicePlayer == "paper" && choiceOpponent == "paper"){
                // player draw

                Toast.makeText(getApplicationContext(), getString(R.string.shifumi_draw_point), Toast.LENGTH_LONG).show();

            } else if (choicePlayer == "scissors" && choiceOpponent == "paper"){
                // player win

                // score +1

                if (user?.uid == roomData.userId1){

                    val scorePlayer = gameData.scorePlayer1 + 1
                    ShifumiHelper.updateScorePlayer1(roomId!!,scorePlayer)


                } else{

                    val scorePlayer = gameData.scorePlayer2 + 1
                    ShifumiHelper.updateScorePlayer2(roomId!!,scorePlayer)
                }

                Toast.makeText(getApplicationContext(), getString(R.string.shifumi_win_point), Toast.LENGTH_LONG).show();


            } else if (choicePlayer == "scissors" && choiceOpponent == "rock"){
                // player lose

                Toast.makeText(getApplicationContext(), getString(R.string.shifumi_lose_point), Toast.LENGTH_LONG).show();


            } else if (choicePlayer == "scissors" && choiceOpponent == "scissors"){
                // player draw

                Toast.makeText(getApplicationContext(), getString(R.string.shifumi_draw_point), Toast.LENGTH_LONG).show();


            } else if (choicePlayer.isEmpty() && choiceOpponent.length > 1){

                Toast.makeText(getApplicationContext(), getString(R.string.shifumi_null_point), Toast.LENGTH_LONG).show();

            } else if(choicePlayer.length > 1 && choiceOpponent.isEmpty()) {

                // score +1

                if (user?.uid == roomData.userId1) {

                    val scorePlayer = gameData.scorePlayer1 + 1
                    ShifumiHelper.updateScorePlayer1(roomId!!, scorePlayer)


                } else {

                    val scorePlayer = gameData.scorePlayer2 + 1
                    ShifumiHelper.updateScorePlayer2(roomId!!, scorePlayer)
                }

                Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.shifumi_win_point),
                    Toast.LENGTH_LONG
                ).show();

            }
            //time = 10000L

            ShifumiHelper.updateValidateFalsePlayer1(roomId!!)
            ShifumiHelper.updateValidateFalsePlayer2(roomId!!)
            getRoomData()
            getGameData()
            isReady = false
            endGame()
            setViewListeners()


    }

    private fun endGame(){
        getGameData()
        getRoomData()

        if (gameData.scorePlayer1 == 3){
            ShifumiHelper.setWinner(roomId!!,roomData.player1!!)
            CharacterHelper.getCharacter(roomData.userId1).addOnSuccessListener {doc ->
                val modelCharacter = doc.toObject(Character::class.java)!!
                CharacterHelper.updateLuck(roomData.userId1,modelCharacter.luck!!+1)
            }

            gameFinish = true
        } else if (gameData.scorePlayer2 == 3){
            ShifumiHelper.setWinner(roomId!!,roomData.player2!!)
            CharacterHelper.getCharacter(roomData.userId2).addOnSuccessListener {doc ->
                val modelCharacter = doc.toObject(Character::class.java)!!
                CharacterHelper.updateLuck(roomData.userId2,modelCharacter.luck!!+1)
            }
            gameFinish = true
        }

    }



}