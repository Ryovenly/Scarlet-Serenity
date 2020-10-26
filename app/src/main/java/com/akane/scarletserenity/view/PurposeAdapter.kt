package com.akane.scarletserenity.view

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.vote.Purpose
import kotlinx.android.synthetic.main.list_item_purpose_create.view.*


class PurposeAdapter(val purposes: List<Purpose>): RecyclerView.Adapter<PurposeAdapter.ViewHolder>() {

    lateinit var mClickListener:ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)

    }

    override fun getItemCount(): Int {
        return purposes.size
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener!!
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val purpose = purposes[position]

        holder.itemView.tv_number.text = "${holder.itemView.context.getString((R.string.number))}: ${position + 1}"
        holder.itemView.tv_title_purpose.text = "${holder.itemView.context.getString((R.string.title))} : ${purpose.title}"
        holder.itemView.tv_description_purpose.text = "${holder.itemView.context.getString((R.string.description))} : ${purpose.description}"
        holder.itemView.bt_delete_purpose.setOnClickListener { mClickListener.onItemClick(
            it,
            holder.adapterPosition
        ) }


        Log.d("TAG", "img : $purpose")

    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_purpose_create, parent, false)
    ) {

        private var number: TextView? = null
        private var title: TextView? = null
        private var description: TextView? = null
        private var delete: AppCompatImageButton? = null

        init {
            number = itemView.findViewById(R.id.tv_number)
            title = itemView.findViewById(R.id.tv_title_purpose)
            description = itemView.findViewById(R.id.tv_description_purpose)
            delete = itemView.findViewById(R.id.bt_delete_purpose)

        }

    }

}