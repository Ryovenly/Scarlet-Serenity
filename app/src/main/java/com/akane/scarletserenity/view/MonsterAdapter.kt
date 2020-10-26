package com.akane.scarletserenity.view

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.GlideApp
import com.akane.scarletserenity.model.monster.Monster
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.list_item_monster.view.*

class MonsterAdapter(val monsters: List<Monster>): RecyclerView.Adapter<MonsterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)

    }

    override fun getItemCount(): Int {
        return monsters.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val monster = monsters[position]

            holder.itemView.tv_name.text = "${holder.itemView.context.getString((R.string.name))}: ${monster.name}"
            holder.itemView.tv_description.text = "${holder.itemView.context.getString((R.string.description))}: ${monster.description}"
            holder.itemView.tv_strength.text = "${holder.itemView.context.getString((R.string.strength))}: ${monster.strength}"
            holder.itemView.tv_intelligence.text = "${holder.itemView.context.getString((R.string.intelligence))}: ${monster.intelligence}"
            holder.itemView.tv_agility.text = "${holder.itemView.context.getString((R.string.agility))}: ${monster.agility}"
            holder.itemView.tv_luck.text = "${holder.itemView.context.getString((R.string.luck))}: ${monster.luck}"
            holder.itemView.tv_po.text = "${holder.itemView.context.getString((R.string.po))}: ${monster.po}"
            holder.itemView.tv_reward.text = "${holder.itemView.context.getString((R.string.reward))}: ${monster.reward}"

        // Reference to an image file in Cloud Storage
        // Create a reference with an initial file path and name

        val storageReference = Firebase.storage.getReferenceFromUrl(monster.img)
        Log.d("TAG", "img : $monster")

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        GlideApp.with(holder.itemView.context)
            .load(storageReference)
            .into(holder.itemView.iv_monster)


    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_monster, parent, false)
    ) {

        private var name: TextView? = null
        private var description: TextView? = null
        private var strength: TextView? = null
        private var intelligence: TextView? = null
        private var agility: TextView? = null
        private var luck: TextView? = null
        private var po: TextView? = null
        private var reward: TextView? = null
        private var img: ImageView? = null

        init {
            name = itemView.findViewById(R.id.tv_name)
            description = itemView.findViewById(R.id.tv_description)
            strength = itemView.findViewById(R.id.tv_strength)
            intelligence = itemView.findViewById(R.id.tv_intelligence)
            agility = itemView.findViewById(R.id.tv_agility)
            luck = itemView.findViewById(R.id.tv_luck)
            po = itemView.findViewById(R.id.tv_po)
            reward = itemView.findViewById(R.id.tv_reward)
            img = itemView.findViewById(R.id.iv_monster)

        }

    }

}