package com.example.limelite.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.limelite.activities.EditLinkActivity
import com.example.limelite.models.Link
import com.example.limelite.R
import org.parceler.Parcels

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


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val imageViewLinkType: ImageView = itemView.findViewById(R.id.imageViewLinkType)
        private val textViewLink: TextView = itemView.findViewById(R.id.textViewLink)
        private val constraintLayoutItemLnk: ConstraintLayout = itemView.findViewById(R.id.constraintLayout_item_link)

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


            constraintLayoutItemLnk.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation))

        }

        // onClick to go to EditLinkAcitivty
        override fun onClick(p0: View?) {
            //get item position
            val position : Int = adapterPosition

            //Make sure position is valid i.e actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                //Get the link at the position
                val link : Link = links[position]

                //Create the intent for the new activity
                val i =  Intent(context, EditLinkActivity::class.java)

                //Serialize the link using the parceler
                i.putExtra(Link::class.java.simpleName, Parcels.wrap(link))

                // Show activity
                context.startActivity(i)
            }
        }

    }

}


