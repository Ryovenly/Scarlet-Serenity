package com.akane.scarletserenity.model.widget

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.akane.scarletserenity.R
import java.util.*

class DateStartPickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(this.activity!!, this, year, month, day)
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        val realMonth = month+1
        val tv = activity?.findViewById<TextView>(R.id.tv_date_start)
        Log.d("TAG", "get failed with  $year" )
        Log.d("TAG", "get failed with $month")
        tv!!.text = "$year-${if (realMonth < 10) "0$realMonth" else realMonth}-${if (day < 10) "0$day" else day}"
    }
}