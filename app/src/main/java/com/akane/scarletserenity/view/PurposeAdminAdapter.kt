package com.akane.scarletserenity.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.vote.Purpose
import com.akane.scarletserenity.model.vote.ScrutinHelper
import kotlinx.android.synthetic.main.list_item_purpose_admin.view.*


class PurposeAdminAdapter(val purposes: List<Purpose>, val scrutin: String): RecyclerView.Adapter<PurposeAdminAdapter.ViewHolder>() {


    lateinit var mClickListener: PurposeParticipateAdapter.ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)

    }

    override fun getItemCount(): Int {
        return purposes.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val purpose = purposes[position]

        val voters = ScrutinHelper.getVoters(scrutin, purpose.title)

        holder.itemView.tv_number.text = "${holder.itemView.context.getString((R.string.number))}: ${position + 1}"
        holder.itemView.tv_title_purpose.text = "${holder.itemView.context.getString((R.string.title))} : ${purpose.title}"
        holder.itemView.tv_description_purpose.text = "${holder.itemView.context.getString((R.string.description))} : ${purpose.description}"
        holder.itemView.tv_voters.text = "${holder.itemView.context.getString((R.string.voters))} : $voters"
    }



    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_purpose_admin, parent, false)
    ) {

        private var number: TextView? = null
        private var title: TextView? = null
        private var description: TextView? = null
        private var vote: TextView? = null

        init {
            number = itemView.findViewById(R.id.tv_number)
            title = itemView.findViewById(R.id.tv_title_purpose)
            description = itemView.findViewById(R.id.tv_description_purpose)
            vote = itemView.findViewById(R.id.tv_voters)

        }

    }
}