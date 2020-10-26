package com.akane.scarletserenity.model


import com.google.firebase.Timestamp

class Message(
    val text: String,
    val user: String,
    val timestamp: Timestamp,
    val character: String
)