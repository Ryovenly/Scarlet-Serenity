package com.akane.scarletserenity.model


import com.google.firebase.Timestamp
import java.util.*

class TchatMessage (
    val text: String,
    val user: String,
    val timestamp: Timestamp
)