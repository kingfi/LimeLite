package com.example.limelite.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.limelite.activities.FriendActivity.TAG
import com.example.limelite.R
import com.example.limelite.models.Link

class FriendAdapter (private val context: Context, private var links: MutableList<Link>) : RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

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


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val imageViewLinkType: ImageView = itemView.findViewById(R.id.imageViewLinkType)
        private val textViewLink: TextView = itemView.findViewById(R.id.textViewLink)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(link: Link){
            textViewLink.text = link.url

            if (link.type == "Facebook") {
                Glide.with(context).load(R.drawable.ic_facebook).into(imageViewLinkType)
            } else if (link.type == "Instagram") {
                Glide.with(context).load(R.drawable.ic_instagram).into(imageViewLinkType)
            } else if (link.type == "LinkedIn") {
                Glide.with(context).load(R.drawable.ic_linkedin).into(imageViewLinkType)
            } else if (link.type == "YouTube") {
                Glide.with(context).load(R.drawable.ic_youtube).into(imageViewLinkType)
            } else if (link.type == "Twitter") {
                Glide.with(context).load(R.drawable.ic_twitter).into(imageViewLinkType)
            } else if (link.type == "Snapchat") {
                Glide.with(context).load(R.drawable.ic_snapchat).into(imageViewLinkType)
            } else {
                Glide.with(context).load(R.drawable.ic_link).into(imageViewLinkType)
            }

        }

        // Go to links when clicked
        override fun onClick(p0: View?) {
            //get item position
            val position : Int = adapterPosition

            //Make sure position is valid i.e actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                //Get the link at the position
                val link : Link = links[position]

                Log.i(TAG, "Link is " + link.url!! )
                var i = Intent(Intent.ACTION_VIEW, Uri.parse(link.url!!))
                context.startActivity(i)

            }
        }

    }

}