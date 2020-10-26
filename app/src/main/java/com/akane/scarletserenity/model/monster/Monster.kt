package com.akane.scarletserenity.model.monster

class Monster(
    var name: String,
    var description: String,
    var img: String,
    var hp: Long,
    var strength: Long,
    var intelligence: Long,
    var agility: Long,
    var luck: Long,
    var po: Long,
    var reward:String
) {
    constructor() : this("", "","",0,0,0,0,0,0,"")
}