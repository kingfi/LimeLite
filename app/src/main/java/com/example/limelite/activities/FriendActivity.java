package com.example.limelite.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.limelite.R;
import com.example.limelite.adapters.FriendAdapter;
import com.example.limelite.adapters.ProfileAdapter;
import com.example.limelite.models.Link;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {

    public static final String TAG = "FriendActivity";
    private TextView textViewFriendUsername;
    private RecyclerView recyclerViewFriendLinks;
    private ImageView imageViewFriendPic;
    private ParseUser friend;
    private List<Link> allLinks;
    private FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        textViewFriendUsername = findViewById(R.id.textViewFriendUsername);
        recyclerViewFriendLinks = findViewById(R.id.recyclerViewFriendLinks);
        imageViewFriendPic = findViewById(R.id.imageViewFriendPic);

        // Unwrap intent
        friend = Parcels.unwrap(getIntent().getParcelableExtra(ParseUser.class.getSimpleName()));

        textViewFriendUsername.setText(friend.getUsername());

        // Load profile
        ParseFile profile = (ParseFile) friend.get("profilePic");
        Glide.with(this).load(profile.getUrl()).into(imageViewFriendPic);

        // create the data source
        allLinks = new ArrayList<>();
        // create the adapter
        adapter = new FriendAdapter(this, allLinks);
        // set the adapter on the recycler view
        recyclerViewFriendLinks.setAdapter(adapter);
        // set the layout manager on the recycler view
        recyclerViewFriendLinks.setLayoutManager(new LinearLayoutManager(this));
        queryUserLinks();



    }

    private void queryUserLinks() {
        ParseQuery<Link> query = ParseQuery.getQuery(Link.class);

        //include author information
        query.include(Link.KEY_LINK_USER);
        query.whereEqualTo(Link.KEY_LINK_USER, friend);
        query.setLimit(20);
        //query.addDescendingOrder(Link.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Link>() {
            @Override
            public void done(List<Link> links, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting links", e);
                }
                for (Link link : links) {
                    Log.i(TAG, "Link: " + link.getUrl());
                }
                allLinks.addAll(links);
                adapter.notifyDataSetChanged();
            }
        });
    }

}