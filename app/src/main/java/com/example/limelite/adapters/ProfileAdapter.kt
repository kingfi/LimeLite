package com.example.limelite.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.limelite.models.Link
import com.example.limelite.R

class ProfileAdapter (private val context: Context, private var links: MutableList<Link>) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_link, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return links.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val link = links[position]
        holder.bind(link)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewLinkType: ImageView = itemView.findViewById(R.id.imageViewLinkType)
        private val textViewLink: TextView = itemView.findViewById(R.id.textViewLink)

        fun bind(link: Link){
            textViewLink.text = link.url

            if (link.type == "facebook") {
                Glide.with(context).load(R.drawable.ic_facebook).into(imageViewLinkType)
            } else if (link.type == "instagram") {
                Glide.with(context).load(R.drawable.ic_instagram).into(imageViewLinkType)
            } else if (link.type == "linkedin") {
                Glide.with(context).load(R.drawable.ic_linkedin).into(imageViewLinkType)
            } else if (link.type == "youtube") {
                Glide.with(context).load(R.drawable.ic_youtube).into(imageViewLinkType)
            } else if (link.type == "twitter") {
                Glide.with(context).load(R.drawable.ic_twitter).into(imageViewLinkType)
            } else if (link.type == "snapchat") {
                Glide.with(context).load(R.drawable.ic_snapchat).into(imageViewLinkType)
            } else {
                Glide.with(context).load(R.drawable.ic_link).into(imageViewLinkType)
            }



        }

    }

}