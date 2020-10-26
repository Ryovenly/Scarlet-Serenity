package com.akane.scarletserenity.view

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.GlideApp
import com.akane.scarletserenity.model.monster.Monster
import com.akane.scarletserenity.model.vote.Scrutin
import com.google.common.math.LongMath.mod
import com.google.firebase.Timestamp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.list_item_monster.view.*
import kotlinx.android.synthetic.main.list_item_scrutin.view.*

class ScrutinParticipateAdapter(val scrutins: List<Scrutin>): RecyclerView.Adapter<ScrutinParticipateAdapter.ViewHolder>() {

    lateinit var mClickListener: ScrutinParticipateAdapter.ItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)

    }

    override fun getItemCount(): Int {
        return scrutins.size
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
        val scrutin = scrutins[position]

        val dateNow = Timestamp.now().seconds
        val dateEnd = scrutin.date_end.seconds
        val timeLeft = dateEnd - dateNow
        val hour = timeLeft/3600
        val minutes = (timeLeft - hour * 3600) / 60

        holder.itemView.tv_topic.text = scrutin.title
        holder.itemView.tv_time_left.text = "${if (timeLeft>0)" ${holder.itemView.context.getString((R.string.time_left))} $hour h $minutes" else "Temps dépassé"} "
        holder.itemView.bt_vote.setOnClickListener { mClickListener.onItemClick(
            it,
            holder.adapterPosition
        ) }

        // Reference to an image file in Cloud Storage
        // Create a reference with an initial file path and name

        val storageReference = Firebase.storage.getReferenceFromUrl(scrutin.img)
        Log.d("TAG", "img : $scrutin")

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        GlideApp.with(holder.itemView.context)
            .load(storageReference)
            .into(holder.itemView.iv_topic)


    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_scrutin, parent, false)
    ) {

        private var title: TextView? = null
        private var img: ImageView? = null
        private var timeLeft: TextView? = null
        private var vote: TextView? = null

        init {
            title = itemView.findViewById(R.id.tv_topic)
            img = itemView.findViewById(R.id.iv_topic)
            timeLeft = itemView.findViewById(R.id.tv_time_left)
            vote = itemView.findViewById(R.id.bt_vote)

        }

    }


}