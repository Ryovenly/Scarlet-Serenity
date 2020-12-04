package com.akane.scarletserenity.controller.character

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.model.GlideApp
import com.akane.scarletserenity.model.character.Character
import com.akane.scarletserenity.model.character.CharacterHelper
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_profil_character.*
import java.io.IOException
import java.util.*

class ProfilCharacterActivity: BaseActivity() {

    lateinit var modelCharacter: Character

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var filePathDatabase = ""
    private var pathData = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_character)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        getCharacter()
        setViewListener()
    }


    private fun setViewListener(){
        iv_character.setOnClickListener {
            launchGallery()
        }

        bt_leave.setOnClickListener {
            finish()
        }
    }


    private fun getCharacter() {
        CharacterHelper.getCharacter(user?.uid)
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    modelCharacter = documentSnapshot.toObject(Character::class.java)!!
                    attributesCharacter()

                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }


    @SuppressLint("SetTextI18n")
    private fun attributesCharacter(){

        tv_pseudo.text = modelCharacter.pseudo
        tv_gender.text = modelCharacter.gender
        tv_money.text = "${getString(R.string.money)}: ${modelCharacter.money}"
        tv_hp.text = "${getString(R.string.health)}: ${modelCharacter.hpMax}"
        tv_mana.text = "${getString(R.string.mana)}: ${modelCharacter.manaMax}"
        tv_stamina.text = "${getString(R.string.stamina)}: ${modelCharacter.staminaMax}"
        tv_hunger.text = "${getString(R.string.hunger)}: ${modelCharacter.hungerMax}"
        tv_strength.text = "${getString(R.string.strength)}: ${modelCharacter.strength}"
        tv_intelligence.text = "${getString(R.string.intelligence)}: ${modelCharacter.intelligence}"
        tv_agility.text = "${getString(R.string.agility)}: ${modelCharacter.agility}"
        tv_luck.text = "${getString(R.string.luck)}: ${modelCharacter.luck}"
        tv_created.text = "${getString(R.string.character_created)} ${modelCharacter.created}"

        if (modelCharacter.img.isNullOrEmpty()){
            iv_character.setImageResource(R.drawable.add_character)
        } else{
            val storageReference = Firebase.storage.getReferenceFromUrl(modelCharacter.img!!)

            GlideApp.with(applicationContext)
                .load(storageReference)
                .into(iv_character)
        }

        
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Choisir une image"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            uploadImage()

            pathData = dataEmp + filePathDatabase

            CharacterHelper.updateImage(user?.uid, pathData)

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                iv_character.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage(){

        if(filePath != null){
            filePathDatabase = "avatars/" + UUID.randomUUID().toString()
            val ref = storageReference?.child(filePathDatabase)
            val uploadTask = ref?.putFile(filePath!!)
            Toast.makeText(this, "L'image a bien été upload", Toast.LENGTH_SHORT).show()
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




}