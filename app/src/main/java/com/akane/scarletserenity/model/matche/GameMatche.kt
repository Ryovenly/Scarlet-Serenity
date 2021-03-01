package com.akane.scarletserenity.model.matche

import kotlin.random.Random

class GameMatche(
    var player1: PlayerMatche,
    var player2: PlayerMatche,
    var matches: Int,
    var isFinish: Boolean,
    var winner: PlayerMatche,
    var turn: PlayerMatche
) {

    val DefaultMatches = 12

    fun RandomTurn(){

        if(turn == null){
            when (Random.nextInt(1,2)){
                1 -> turn = player1
                2 -> turn = player2
            }
        }


    }



    fun IsGameEnded() {
        if (matches <= 0)
        isFinish = true

        when (turn){
            player1 -> winner = player2
            player2 -> winner = player1
        }

    }


}