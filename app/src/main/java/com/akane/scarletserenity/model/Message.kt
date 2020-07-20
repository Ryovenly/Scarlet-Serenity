package com.akane.scarletserenity.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*


class Message {

    private var message: String? = null
    private var dateCreated: Date? = null
    private var userSender: User? = null
    private var characterSender: Character? = null
    private var urlImage: String? = null

    fun Message() {}

    fun Message(
        message: String?,
        userSender: User?,
        characterSender: Character?
    ) {
        this.message = message
        this.userSender = userSender
        this.characterSender = characterSender
    }

    fun Message(
        message: String?,
        urlImage: String?,
        userSender: User?,
        characterSender: Character?
    ) {
        this.message = message
        this.urlImage = urlImage
        this.userSender = userSender
        this.characterSender = characterSender
    }

    // --- GETTERS ---
    fun getMessage(): String? {
        return message
    }

    @ServerTimestamp
    fun getDateCreated(): Date? {
        return dateCreated
    }

    fun getUserSender(): User? {
        return userSender
    }

    fun getCharacterSender(): Character? {
        return characterSender
    }

    fun getUrlImage(): String? {
        return urlImage
    }

    // --- SETTERS ---
    fun setMessage(message: String?) {
        this.message = message
    }

    fun setDateCreated(dateCreated: Date?) {
        this.dateCreated = dateCreated
    }

    fun setUserSender (userSender: User?){
        this.userSender = userSender
    }

    fun setCharacterSender(characterSender: Character?) {
        this.characterSender = characterSender
    }

    fun setUrlImage(urlImage: String?) {
        this.urlImage = urlImage
    }
}