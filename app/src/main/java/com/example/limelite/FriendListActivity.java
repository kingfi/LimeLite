package com.example.limelite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.limelite.adapters.FriendsListAdapter;
import com.example.limelite.adapters.ProfileAdapter;
import com.example.limelite.models.Relationships;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity {

    public static final String TAG = "FriendsListActivity";
    private TextView textViewFriendListCount;
    private RecyclerView recyclerViewFriendList;
    private ArrayList<Relationships> relationsList;
    private ArrayList<ParseUser> friends;
    private FriendsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        //Initialize Variables
//        textViewFriendListCount = findViewById(R.id.textViewFriendsCount);
        recyclerViewFriendList = findViewById(R.id.recyclerViewFriendList);

        // Unwrap intent
        relationsList = Parcels.unwrap(getIntent().getParcelableExtra("relationsList"));


        // Set up and connect FriendsListAdapter to FriendsListActivity

        // Set the data source
        friends = new ArrayList<>();
        // create the adapter
        adapter = new FriendsListAdapter(this, friends);
        // set the adapter on the recycler view
        recyclerViewFriendList.setAdapter(adapter);
        // set the layout manager on the recycler view
        recyclerViewFriendList.setLayoutManager(new LinearLayoutManager(this));

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
        adapter.notifyDataSetChanged();
    }
}