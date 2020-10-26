package com.akane.scarletserenity.model.shifumi

class ShifumiGame(var choicePlayer1: String,
                  var choicePlayer2: String,
                  var scorePlayer1: Int,
                  var scorePlayer2: Int,
                  var timer: Long,
                  var readyPlayer1: Boolean,
                  var readyPlayer2: Boolean
) {
    constructor() : this("", "",0,0,0,false,false)
}