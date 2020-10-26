package com.akane.scarletserenity.model.shifumi

import com.google.firebase.Timestamp

class ShifumiRoom(
    var userId1:String?,
    var player1:String?,
    var userId2:String?,
    var player2: String?,
    var status:String?,
    var created: com.google.firebase.Timestamp,
    var winner:String?
){


    constructor() : this("","","","","",Timestamp.now(),"")
}
