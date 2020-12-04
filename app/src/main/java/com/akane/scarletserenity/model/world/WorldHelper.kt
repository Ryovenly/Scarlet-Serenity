package com.akane.scarletserenity.model.world

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

object WorldHelper {

    // --- COLLECTION REFERENCE ---

    fun getWorldCollection(): CollectionReference? {
        return FirebaseFirestore.getInstance().collection("worlds")
    }
}