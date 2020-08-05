package com.example.limelite.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.limelite.activities.FriendActivity
import com.example.limelite.R
import com.parse.ParseUser
import org.parceler.Parcels

class FriendsListAdapter (private val context: Context, private var friends: MutableList<ParseUser>): RecyclerView.Adapter<FriendsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friends[position]
        holder.bind(friend)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val imageViewFriendProfile: ImageView = itemView.findViewById(R.id.imageViewRequesterProfile)
        private val textViewFriendName: TextView = itemView.findViewById(R.id.textViewRequesterName)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(friend: ParseUser) {
            friend.fetch()
            textViewFriendName.text = friend.username

            Glide.with(context).load(friend.getParseFile("profilePic")!!.url).into(imageViewFriendProfile)
        }


        override fun onClick(p0: View?) {
            //get item position
            val position : Int = adapterPosition

            //Make sure position is valid i.e actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                //Get the link at the position
                val friend : ParseUser = friends[position]

                //Create the intent for the new activity
                val i =  Intent(context, FriendActivity::class.java)

                //Serialize the link using the parceler
                i.putExtra(ParseUser::class.java.simpleName, Parcels.wrap(friend))

                // Show activity
                context.startActivity(i)
            }
        }

    }


}