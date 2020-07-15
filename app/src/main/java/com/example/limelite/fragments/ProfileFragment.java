package com.example.limelite.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.limelite.adapters.ProfileAdapter;
import com.example.limelite.R;
import com.example.limelite.SettingsActivity;
import com.example.limelite.models.Link;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment" ;
    private TextView textViewProfileUsername;
    private TextView textViewFriendsCount;
    private ImageView buttonSettings;
    private RecyclerView recyclerViewLinks;
    private List<Link> allLinks;
    private ProfileAdapter adapter;
    private ImageView imageViewProfile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewProfileUsername = view.findViewById(R.id.textViewProfileUsername);
        textViewFriendsCount = view.findViewById(R.id.textViewFriendsCount);
        buttonSettings = view.findViewById(R.id.buttonSettings);
        recyclerViewLinks = view.findViewById(R.id.recyclerViewLinks);
        imageViewProfile = view.findViewById(R.id.imageViewProfilePic);
        textViewFriendsCount = view.findViewById(R.id.textViewFriendsCount);

        ParseFile profile = (ParseFile) ParseUser.getCurrentUser().get("profilePic");
        if (profile != null) {
            Glide.with(getContext()).load(profile.getUrl()).into(imageViewProfile);
        }

        // Set Friend Count



        // Set username to view
        textViewProfileUsername.setText((String) ParseUser.getCurrentUser().get("firstName"));

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SettingsActivity.class);
                startActivity(i);
            }
        });

        // create the data source
        allLinks = new ArrayList<>();
        // create the adapter
        adapter = new ProfileAdapter(getContext(), allLinks);
        // set the adapter on the recycler view
        recyclerViewLinks.setAdapter(adapter);
        // set the layout manager on the recycler view
        recyclerViewLinks.setLayoutManager(new LinearLayoutManager(getContext()));
        queryUserLinks();


    }

    private void queryUserLinks() {
        ParseQuery<Link> query = ParseQuery.getQuery(Link.class);

        //include author information
        query.include(Link.KEY_LINK_USER);
        query.whereEqualTo(Link.KEY_LINK_USER, ParseUser.getCurrentUser());
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