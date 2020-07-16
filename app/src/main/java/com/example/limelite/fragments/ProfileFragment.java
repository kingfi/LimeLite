package com.example.limelite.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.limelite.adapters.ProfileAdapter;
import com.example.limelite.R;
import com.example.limelite.SettingsActivity;
import com.example.limelite.models.Link;
import com.example.limelite.models.Relationships;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

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
    private ImageView imageViewProfilePic;
    private ArrayList<Relationships> relationsList;
    public static final int SETTINGS_RESULT_CODE = 123;


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
        imageViewProfilePic = view.findViewById(R.id.imageViewProfilePic);
        textViewFriendsCount = view.findViewById(R.id.textViewFriendsCount);



        try {
            ParseUser user = ParseUser.getCurrentUser().fetch();
            ParseFile profile = user.getParseFile("profilePic");
            if (profile != null) {
                Log.i(TAG, profile.getUrl());
                Glide.with(getContext()).load(profile.getUrl()).into(imageViewProfilePic);
                Log.i(TAG, "Profile Set");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // Populate Relationships list and Display number of friends
        relationsList = new ArrayList<>();
        try {
            queryRelationships();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Set username to view
        textViewProfileUsername.setText((String) ParseUser.getCurrentUser().get("firstName"));

        //onClick for button settings
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SettingsActivity.class);
                startActivityForResult(i, SETTINGS_RESULT_CODE);
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

    private void queryRelationships() throws ParseException {
        ParseQuery queryRequestor = ParseQuery.getQuery(Relationships.class);
        ParseQuery queryRequestee = ParseQuery.getQuery(Relationships.class);


        queryRequestor.include(Relationships.KEY_REQUESTOR);
        queryRequestor.whereEqualTo(Relationships.KEY_REQUESTOR, ParseUser.getCurrentUser());

        queryRequestee.include(Relationships.KEY_REQUESTEE);
        queryRequestee.whereEqualTo(Relationships.KEY_REQUESTEE, ParseUser.getCurrentUser());

        relationsList.addAll((ArrayList<Relationships>) queryRequestor.find());
        relationsList.addAll((ArrayList<Relationships>) queryRequestee.find());

        Integer friends = 0;
        for (Relationships relation: relationsList) {
            if (relation.getStatus() == 1) {
                friends ++;
            }
        }
        textViewFriendsCount.setText("Friends: " + friends);

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

//    
}