package com.akane.scarletserenity.model.vote

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.util.ArrayList

class Scrutin(
    var id: String?,
    var title: String,
    var img: String,
    var description: String,
    var date_start: Timestamp,
    var date_end: Timestamp,
    var _end: Boolean,
    var organizer: String?
) {



    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this("","", "","", Timestamp.now(), Timestamp.now(),false,"")

}