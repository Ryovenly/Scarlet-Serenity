package com.akane.scarletserenity.model.monster

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

object MonsterHelper {


    // --- COLLECTION REFERENCE ---

    fun getMonsterCollection(): CollectionReference? {
        return FirebaseFirestore.getInstance().collection("monsters").document("data").collection("monster")
    }

    // --- CREATE ---
    fun createMonster(newMonster: Monster): Task<Void>? {
        return getMonsterCollection()
            ?.document()
            ?.set(newMonster)
    }

}