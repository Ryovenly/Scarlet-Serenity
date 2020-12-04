package com.akane.scarletserenity.model.character

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime

class Character(
    var pseudo: String,
    var gender: String,
    var money: Int?,
    var hpMax: Int?,
    var manaMax: Int?,
    var staminaMax: Int?,
    var hungerMax: Int?,
    var hp: Int?,
    var mana: Int?,
    var stamina: Int?,
    var hunger: Int?,
    var strength: Int?,
    var intelligence: Int?,
    var agility: Int?,
    var luck: Int?,
    var created: String?,
    var lastLogin: String?,
    var img: String?
                ){
    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this("", "", 100,0,0,100,100,0,0,0,
        0,0,0,0,0,"","","")
}