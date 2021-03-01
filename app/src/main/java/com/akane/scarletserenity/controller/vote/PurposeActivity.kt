package com.akane.scarletserenity.controller.vote

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.akane.scarletserenity.R
import com.akane.scarletserenity.controller.BaseActivity
import com.akane.scarletserenity.model.vote.Purpose
import com.akane.scarletserenity.model.vote.ScrutinHelper
import com.akane.scarletserenity.view.PurposeAdapter
import kotlinx.android.synthetic.main.activity_create_purpose.*
import java.util.*

class PurposeActivity : BaseActivity(),PurposeAdapter.ItemClickListener {

    val purposes = ArrayList<Purpose>()
    lateinit var mTitle:EditText
    lateinit var mDescription:EditText
    lateinit var adapter:PurposeAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_purpose)

        mTitle = findViewById(R.id.ed_title_purpose)
        mDescription = findViewById(R.id.ed_description_purpose)
        setViewListeners()
        initListRecyclerView()


    }

    private fun initListRecyclerView() {

        rv_purpose.layoutManager = LinearLayoutManager(this)
        adapter = PurposeAdapter(purposes)
        rv_purpose.adapter = adapter
        adapter.setClickListener(this)
    }

    private fun setViewListeners(){
        bt_validate_purpose.setOnClickListener {
            addPurpose()
        }

        bt_validate_all_purposes.setOnClickListener {
            addPurposesInDB()
        }
    }

    private fun addPurpose(){

        purposes.add(Purpose(mTitle.text.toString(),mDescription.text.toString()))
        rv_purpose.adapter?.notifyDataSetChanged()
        mTitle.text.clear()
        mDescription.text.clear()

    }

    private fun addPurposesInDB(){
        val scrutinId = intent.getStringExtra("INTENT_EXTRA_PURPOSE")

        for(purpose in purposes){
            ScrutinHelper.createPurpose(scrutinId,purpose,purpose.title)
        }



        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.scrutin_created_title))
            .setMessage(getString(R.string.scrutin_created_description))
            .setPositiveButton(
                getString(R.string.leave)
            ) { dialog, which-> dialog.cancel()
                startVoteMainActivity() }
            .create()
            .show()
    }

    private fun deletePurpose(position: Int){
        purposes.removeAt(position)
    }

    private fun startVoteMainActivity() {
        val intent = Intent(this, VoteMainActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClick(view: View?, position: Int) {
        deletePurpose(position)
        rv_purpose.adapter?.notifyDataSetChanged()
    }
}