package com.akane.scarletserenity.model.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.akane.scarletserenity.R
import java.util.*

class TimeStartPickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker, hour: Int, minute: Int) {
        // Do something with the time chosen by the user

        val tv = activity?.findViewById<TextView>(R.id.tv_time_start)
        Log.d("TAG", "get failed with  $hour" )
        Log.d("TAG", "get failed with $minute")
        tv!!.text = "${if (hour < 10) "0$hour" else hour}:${if (minute < 10) "0$minute" else minute}"
    }
}
