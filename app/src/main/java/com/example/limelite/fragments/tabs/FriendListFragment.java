package com.example.limelite.fragments.tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.limelite.R;
import com.example.limelite.adapters.FriendsListAdapter;
import com.example.limelite.models.Relationships;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class FriendListFragment extends Fragment {

    public static final String TAG = "FriendsListFragment";
    private TextView textViewFriendListCount;
    private RecyclerView recyclerViewFriendList;
    private ArrayList<Relationships> relationsList;
    private ArrayList<ParseUser> friends;
    private FriendsListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.friends_list_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize Variables
        textViewFriendListCount = view.findViewById(R.id.textViewFriendRequestCount);
        recyclerViewFriendList = view.findViewById(R.id.recyclerViewFriendRequestList);

        // Populate Relationships list and Display number of friends
        relationsList = new ArrayList<>();
        try {
            queryRelationships();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Set up and connect FriendsListAdapter to FriendsListActivity

        // Set the data source
        friends = new ArrayList<>();
        // create the adapter
        adapter = new FriendsListAdapter(getContext(), friends);
        // set the adapter on the recycler view
        recyclerViewFriendList.setAdapter(adapter);
        // set the layout manager on the recycler view
        recyclerViewFriendList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get friends
        getFriends();
    }

    private void getFriends() {

        for (Relationships relation: relationsList) {
            if (relation.getStatus() == 1){
                if (relation.getRequestee().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                    friends.add(relation.getRequestor());
                }else{
                    friends.add(relation.getRequestee());
                }
            }
        }
        // Set Friend Count text view
        textViewFriendListCount.setText("Friends: " + friends.size());
        adapter.notifyDataSetChanged();
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
        textViewFriendListCount.setText("Friends: " + friends);

    }
}