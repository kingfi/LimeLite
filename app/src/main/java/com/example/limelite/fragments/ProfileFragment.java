package com.example.limelite.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.limelite.R;
import com.example.limelite.SettingsActivity;
import com.parse.ParseUser;


public class ProfileFragment extends Fragment {

    TextView textViewProfileUsername;
    TextView textViewFriendsCount;
    ImageView buttonSettings;
    RecyclerView recyclerViewLinks;


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

        // Set username to view
        textViewProfileUsername.setText(ParseUser.getCurrentUser().getUsername());

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SettingsActivity.class);
                startActivity(i);
            }
        });


    }
}