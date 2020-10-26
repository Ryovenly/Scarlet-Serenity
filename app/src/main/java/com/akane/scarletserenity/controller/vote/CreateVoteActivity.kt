package com.akane.scarletserenity.controller.vote

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.model.vote.Scrutin
import com.akane.scarletserenity.model.vote.ScrutinHelper
import com.akane.scarletserenity.model.widget.DateEndPickerFragment
import com.akane.scarletserenity.model.widget.DateStartPickerFragment
import com.akane.scarletserenity.model.widget.TimeEndPickerFragment
import com.akane.scarletserenity.model.widget.TimeStartPickerFragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_create_vote.*
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.UUID.randomUUID

class CreateVoteActivity : BaseActivity() {


    // val firestore = FirebaseFirestore.getInstance()
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var filePathDatabase = ""
    private var pathData = ""
    private lateinit var mDateTimeStartReal:Timestamp
    private lateinit var mDateTimeEndReal:Timestamp

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_vote)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        setViewListeners()



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                iv_choose_img.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setViewListeners() {

        iv_choose_img.setImageResource(R.drawable.vote)

        iv_choose_img.setOnClickListener {
            launchGallery()
        }

//        bt_upload_img.setOnClickListener {
//            uploadImage()
//        }


        // date

        bt_date_start.setOnClickListener {
            showDateStartPickerDialog(it)
        }

        bt_time_start.setOnClickListener {
            showTimeStartPickerDialog(it)
        }

        bt_date_end.setOnClickListener {
            showDateEndPickerDialog(it)
        }

        bt_time_end.setOnClickListener {
            showTimeEndPickerDialog(it)
        }

        bt_validate.setOnClickListener{
            uploadImage()
            setDataTime()
            pathData = dataEmp + filePathDatabase

            val scrutinId = ScrutinHelper.getScrutinId()

            ScrutinHelper.createScrutin(scrutinId!!,Scrutin(scrutinId,et_topic_label.text.toString(),pathData,et_description.text.toString(),mDateTimeStartReal,mDateTimeEndReal,false,
                user?.uid))

            purposeCreate(scrutinId)
        }
    }




    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Choisir une image"), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage(){

        if(filePath != null){
            filePathDatabase = "uploads/" + UUID.randomUUID().toString()
            val ref = storageReference?.child(filePathDatabase)
            val uploadTask = ref?.putFile(filePath!!)
            Toast.makeText(this, "ça marcheeeee", Toast.LENGTH_SHORT).show()
            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadRecordToDb(downloadUri.toString())
                } else {
                    // Handle failures
                    Toast.makeText(this, "ça marche pas", Toast.LENGTH_SHORT).show()
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(this, getString(R.string.upload_image), Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUploadRecordToDb(uri: String){
        val db = FirebaseFirestore.getInstance()

        val data = HashMap<String, Any>()
        data["imageUrl"] = uri

        db.collection("posts")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, getString(R.string.save_db), Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, getString(R.string.error_save_db), Toast.LENGTH_LONG).show()
            }
    }

    private fun showDateStartPickerDialog(v: View) {
        DateStartPickerFragment().show(supportFragmentManager, "datePicker")
    }

    private fun showTimeStartPickerDialog(v: View) {
        TimeStartPickerFragment().show(supportFragmentManager, "timePicker")
    }

    private fun showDateEndPickerDialog(v: View) {
        DateEndPickerFragment().show(supportFragmentManager, "datePicker")
    }

    private fun showTimeEndPickerDialog(v: View) {
        TimeEndPickerFragment().show(supportFragmentManager, "timePicker")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDataTime(){
        val mDateStart = tv_date_start.text
        val mTimeStart = tv_time_start.text

        val mDateEnd = tv_date_end.text
        val mTimeEnd = tv_time_end.text

        val mDateTimeStart = "${mDateStart}T$mTimeStart"
        val mDateTimeEnd = "${mDateEnd}T$mTimeEnd"

        val timestampStart = LocalDateTime.parse(mDateTimeStart)
        val timestampEnd = LocalDateTime.parse(mDateTimeEnd)

        mDateTimeStartReal = Timestamp(timestampStart.atZone(ZoneId.systemDefault()).toInstant().epochSecond,0)
        mDateTimeEndReal = Timestamp(timestampEnd.atZone(ZoneId.systemDefault()).toInstant().epochSecond,0)

//        mDateTimeStartReal = LocalDateTime.parse(mDateTimeStart)
//        mDateTimeEndReal = LocalDateTime.parse(mDateTimeEnd)

        val hello = randomUUID().toString()
        Log.d("TAG", "La date $mDateTimeStartReal" )

    }

    private fun purposeCreate(scrutinId: String){
        val intent = Intent(this, PurposeActivity::class.java)
        intent.putExtra("INTENT_EXTRA_PURPOSE", scrutinId)
        startActivity(intent)
    }
}