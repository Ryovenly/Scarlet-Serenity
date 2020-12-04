package com.akane.scarletserenity.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akane.scarletserenity.R
import com.akane.scarletserenity.model.GlideApp
import com.akane.scarletserenity.model.world.World
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.list_item_world.view.*

class WorldAdapter (val worlds: List<World>): RecyclerView.Adapter<WorldAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)

    }

    override fun getItemCount(): Int {
        return worlds.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val world = worlds[position]

        holder.itemView.tv_world.text = world.name


        // Reference to an image file in Cloud Storage
        // Create a reference with an initial file path and name

        val storageReference = Firebase.storage.getReferenceFromUrl(world.img)

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        GlideApp.with(holder.itemView.context)
            .load(storageReference)
            .into(holder.itemView.iv_world)


    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_world, parent, false)
    ) {

        private var name: TextView? = null
        private var img: ImageView? = null

        init {
            name = itemView.findViewById(R.id.tv_world)
            img = itemView.findViewById(R.id.iv_world)

        }

    }

}