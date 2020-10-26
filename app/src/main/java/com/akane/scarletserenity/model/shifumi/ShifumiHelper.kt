package com.akane.scarletserenity.model.shifumi

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

object ShifumiHelper {

    // --- COLLECTION REFERENCE ---

    fun getShifumiCollection(): CollectionReference? {
        return FirebaseFirestore.getInstance().collection("games").document("shifumi").collection("rooms")
    }

    // --- CREATE ---
    fun createShifumiRoom(roomId: String,newRoom: ShifumiRoom): Task<Void>? {
        return getShifumiCollection()
            ?.document(roomId)
            ?.set(newRoom)
    }


    // --- GET ---
    fun getShifumiRoom(roomId: String): Task<DocumentSnapshot>? {
        return getShifumiCollection()
            ?.document(roomId)
            ?.get()
    }

    fun checkShifumiRoom(): Task<QuerySnapshot>? {
        return getShifumiCollection()
            ?.whereEqualTo("status", "open")
            ?.limit(1)
            ?.get()
    }

    fun getRoomId(): String? {
        return getShifumiCollection()
            ?.document()?.id
    }

    // --- DELETE ---

    fun deleteShifumiRoom(roomId: String): Task<Void>? {
        return getShifumiCollection()
            ?.document(roomId)
            ?.delete()
    }

    // --- UPDATE ---

    fun updatePlayerJoined(roomId:String,pseudo:String,userId:String?): Task<Void>? {
        return getShifumiCollection()
            ?.document(roomId)
            ?.update(
                "player2",
                pseudo,
                "userId2",
                userId,
                "status",
                "joined"
            )
    }

    // --- COLLECTION REFERENCE ---

    fun getShifumiGameCollection(roomId: String): CollectionReference? {
        return getShifumiCollection()
            ?.document(roomId)?.collection("game")
    }

    // --- CREATE ---

    fun createShifumiGamePlayer(roomId: String): Task<Void>? {
        return getShifumiGameCollection(
            roomId
        )?.document("game")?.set(
            ShifumiGame(
                "",
                "",
                0,
                0,
                10,
                false,
                false
            )
        )
    }

    // --- GET ---

    fun getShifumiGame(roomId: String): Task<DocumentSnapshot>? {
        return getShifumiGameCollection(
            roomId
        )?.document("game")?.get()
    }

    // --- UPDATE ---

    fun setShifumiGamePlayer1(roomId: String,choice:String): Task<Void>? {
        return getShifumiGameCollection(
            roomId
        )?.document("game")?.update(mapOf(
            Pair("choicePlayer1", choice)
        ))
    }

    fun setShifumiGamePlayer2(roomId: String,choice:String): Task<Void>? {
        return getShifumiGameCollection(
            roomId
        )?.document("game")?.update(mapOf(
            Pair("choicePlayer2", choice)
        ))
    }

    fun updateScorePlayer1(roomId: String, score:Int): Task<Void>? {
        return getShifumiGameCollection(
            roomId
        )?.document("game")?.update(mapOf(
            Pair("scorePlayer1", score)
        ))
    }

    fun updateScorePlayer2(roomId: String, score:Int): Task<Void>? {
        return getShifumiGameCollection(
            roomId
        )?.document("game")?.update(mapOf(
            Pair("scorePlayer2", score)
        ))
    }

    fun updateTimer(roomId: String, timer: Long): Task<Void>? {
        return getShifumiGameCollection(
            roomId
        )?.document("game")?.update(mapOf(
            Pair("timer", timer)
        ))
    }

    fun updateValidatePlayer1(roomId: String): Task<Void>? {
        return getShifumiGameCollection(
            roomId
        )?.document("game")?.update(mapOf(
            Pair("readyPlayer1", true)
        ))
    }

    fun updateValidateFalsePlayer1(roomId: String): Task<Void>? {
        return getShifumiGameCollection(
            roomId
        )?.document("game")?.update(mapOf(
            Pair("readyPlayer1", false)
        ))
    }

    fun updateValidatePlayer2(roomId: String): Task<Void>? {
        return getShifumiGameCollection(
            roomId
        )?.document("game")?.update(mapOf(
            Pair("readyPlayer2", true)
        ))
    }

    fun updateValidateFalsePlayer2(roomId: String): Task<Void>? {
        return getShifumiGameCollection(
            roomId
        )?.document("game")?.update(mapOf(
            Pair("readyPlayer2", false)
        ))
    }

}