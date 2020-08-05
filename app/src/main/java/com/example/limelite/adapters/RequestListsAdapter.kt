package com.example.limelite.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.limelite.R
import com.example.limelite.activities.FriendActivity
import com.parse.ParseUser
import org.parceler.Parcels

class RequestListsAdapter (private val context: Context, private var requesters: MutableList<ParseUser>)
    : RecyclerView.Adapter<RequestListsAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onReject(position: Int)
        fun onAccept(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false)
        return ViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return requesters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = requesters[position]
        holder.bind(friend)
    }

    inner class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        private val imageViewRequesterProfile: ImageView = itemView.findViewById(R.id.imageViewRequesterProfile)
        private val textViewRequesterName: TextView = itemView.findViewById(R.id.textViewRequesterName)
        private val buttonCancelRequest: ImageView = itemView.findViewById(R.id.buttonCancelRequest)
        private val buttonAcceptRequest: ImageView = itemView.findViewById(R.id.buttonAcceptRequest)


        init {
//            itemView.setOnClickListener(this)
            buttonCancelRequest.setOnClickListener(View.OnClickListener {
                if (listener != null) {
                    var position = adapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        listener.onReject(position)
                        Log.i("LOL", "HOLLA")
                    }

                }
            })

            buttonAcceptRequest.setOnClickListener(View.OnClickListener {
                if (listener != null) {
                    var position = adapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        listener.onAccept(position)
                        Log.i("LOL", "BYEEE")
                    }

                }
            })
        }


        fun bind(friend: ParseUser) {
            friend.fetch()
            textViewRequesterName.text = friend.username

            Glide.with(context).load(friend.getParseFile("profilePic")!!.url).into(imageViewRequesterProfile)
        }



//        override fun onClick(p0: View?) {
//            //get item position
//            val position : Int = adapterPosition
//
//            //Make sure position is valid i.e actually exists in the view
//            if (position != RecyclerView.NO_POSITION) {
//                //Get the link at the position
//                val friend : ParseUser = requesters[position]
//
//                //Create the intent for the new activity
//                val i =  Intent(context, FriendActivity::class.java)
//
//                //Serialize the link using the parceler
//                i.putExtra(ParseUser::class.java.simpleName, Parcels.wrap(friend))
//
//                // Show activity
//                context.startActivity(i)
//            }
//        }

    }


}