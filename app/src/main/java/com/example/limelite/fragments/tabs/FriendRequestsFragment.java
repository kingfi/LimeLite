package com.example.limelite.fragments.tabs;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.limelite.R;
import com.example.limelite.adapters.FriendsListAdapter;
import com.example.limelite.adapters.RequestListsAdapter;
import com.example.limelite.models.Relationships;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FriendRequestsFragment extends Fragment {

    private TextView textViewFriendRequestCount;
    private RecyclerView recyclerViewFriendRequestList;
    private ArrayList<Relationships> relationsList;
    private ArrayList<ParseUser> requestedFriends;
    private RequestListsAdapter adapter;
    private Integer requestCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.friend_requests_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Variables
        textViewFriendRequestCount = view.findViewById(R.id.textViewFriendRequestCount);
        recyclerViewFriendRequestList = view.findViewById(R.id.recyclerViewFriendRequestList);
        requestCount = 0;

        // Populate Relationships list and Display number of friends
        relationsList = new ArrayList<>();
        try {
            queryRelationships();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Set up and connect RequestListsAdapter

        // Set the data source
        requestedFriends = new ArrayList<>();
        // create the adapter
        adapter = new RequestListsAdapter(getContext(), requestedFriends);
        // set up adapter's onItemClick events
        adapter.setOnItemClickListener(new RequestListsAdapter.OnItemClickListener() {
            @Override
            public void onReject(int position) {
                Log.i("FRAG", "REJECT");
                // Delete the relationship object from the server
                ParseUser user = requestedFriends.get(position);

                for (Relationships relation: relationsList) {
                    if (relation.getRequestor().getObjectId().equals(user.getObjectId())) {
                        try{
                            relation.delete();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                // Remove user at location and notify adapter
                requestedFriends.remove(position);
                adapter.notifyDataSetChanged();

                textViewFriendRequestCount.setText("Requests: " + (requestCount - 1));

                getParentFragment().getFragmentManager().beginTransaction().detach(getParentFragment()).attach(getParentFragment()).commit();

            }

            @Override
            public void onAccept(int position) {
                Log.i("FRAG", "ACCEPT");

                ParseUser user = requestedFriends.get(position);
                // Update relationship status
                for (Relationships relation: relationsList) {
                    if (relation.getRequestor().getObjectId().equals(user.getObjectId())) {
                        try{
                            relation.setStatus(1);
                            relation.save();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                // Remove user at location and notify adapter
                requestedFriends.remove(position);
                adapter.notifyDataSetChanged();

                textViewFriendRequestCount.setText("Requests: " + (requestCount - 1));

                getParentFragment().getFragmentManager().beginTransaction().detach(getParentFragment()).attach(getParentFragment()).commit();


            }
        });
        // set the adapter on the recycler view
        recyclerViewFriendRequestList.setAdapter(adapter);
        // set the layout manager on the recycler view
        recyclerViewFriendRequestList.setLayoutManager(new LinearLayoutManager(getContext()));

        getRequests();

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

        for (Relationships relation: relationsList) {
            if (relation.getStatus() == 0 && (relation.getRequestee().getObjectId().equals(ParseUser.getCurrentUser().getObjectId()))) {
                requestCount ++;
            }
        }
        textViewFriendRequestCount.setText("Requests: " + requestCount);

    }

    private void getRequests() {

        for (Relationships relation: relationsList) {
            if (relation.getStatus() == 0){
                if (relation.getRequestee().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                    requestedFriends.add(relation.getRequestor());
                }
            }
        }
        // Set Friend Count text view
        textViewFriendRequestCount.setText("Requests: " + requestedFriends.size());
        adapter.notifyDataSetChanged();
    }
}