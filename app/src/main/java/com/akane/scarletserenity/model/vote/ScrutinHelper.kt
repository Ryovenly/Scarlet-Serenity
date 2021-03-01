package com.akane.scarletserenity.model.vote

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

object ScrutinHelper {

    // --- COLLECTION REFERENCE ---

    fun getScrutinCollection(): CollectionReference? {
        return FirebaseFirestore.getInstance().collection("scrutins")
    }

    fun getPurposeCollection(scrutinId: String): CollectionReference? {
        return getScrutinCollection()?.document(scrutinId)?.collection("purposes")
    }

    fun getVoterCollection(scrutinId: String, purposeId: String): CollectionReference? {
        return getPurposeCollection(scrutinId)?.document(purposeId)?.collection("voters")
    }

    // --- CREATE ---
    fun createScrutin(scrutinId: String,scrutin: Scrutin): Task<Void>? {
        return getScrutinCollection()
            ?.document(scrutinId)
            ?.set(scrutin)
    }

    fun createPurpose(scrutinId: String, purpose: Purpose, title: String): Task<Void>? {
        return getPurposeCollection(scrutinId)
            ?.document(title)
            ?.set(purpose)
    }

    fun createVote(scrutinId: String,purposeId: String,voter: Voter, userId:String): Task<Void>? {
        return getVoterCollection(scrutinId,purposeId)?.document(userId)?.set(voter)
    }


    // --- GET ---
    fun getScrutin(scrutinId: String): Task<DocumentSnapshot>? {
        return getScrutinCollection()
            ?.document(scrutinId)
            ?.get()
    }



    fun getScrutinId(): String? {
        return getScrutinCollection()
            ?.document()?.id
    }

    fun getVoters(scrutinId: String, purposeId: String): Int {

        var size = 0
        getVoterCollection(scrutinId, purposeId)?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful){
                size = task.getResult()?.size()!!
            }
        }

        return size
    }
    // --- DELETE ---

    fun deleteScrutin(scrutinId: String): Task<Void>? {
        return getScrutinCollection()
            ?.document(scrutinId)
            ?.delete()
    }

    // --- UPDATE ---

//    fun updatePurpose(scrutinId: String, purposes: ArrayList<Purpose>): Task<Void>? {
//        return getScrutinCollection()
//            ?.document(scrutinId)
//            ?.update("purpose",purposes)
//    }



//    fun updatePlayerJoined(roomId:String,pseudo:String,userId:String?): Task<Void>? {
//        return getShifumiCollection()
//            ?.document(roomId)
//            ?.update(
//                "player2",
//                pseudo,
//                "userId2",
//                userId,
//                "status",
//                "joined"
//            )
//    }
}