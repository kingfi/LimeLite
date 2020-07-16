package com.example.limelite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.limelite.models.Link;
import com.parse.ParseException;

import org.parceler.Parcels;

public class EditLinkActivity extends AppCompatActivity {

    private Link link;
    private Spinner spinnerType;
    private EditText editTextLink;
    private Button buttonSaveLink;
    private Button buttonDeleteLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_link);

        //unwrap the post passed in via intent, using its simple name as a key
        link = Parcels.unwrap(getIntent().getParcelableExtra(Link.class.getSimpleName()));

        // Initialize variables
        spinnerType = findViewById(R.id.spinnerType);
        editTextLink = findViewById(R.id.editTextLink);
        buttonSaveLink = findViewById(R.id.buttonSaveLink);
        buttonDeleteLink = findViewById(R.id.buttonDeleteLink);

        editTextLink.setText(link.getUrl());

        //Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.linkTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        // Set spinner position to be type of the link
        int position = adapter.getPosition(link.getType());
        spinnerType.setSelection(position);

        // Save button functionality
        buttonSaveLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link.setType(spinnerType.getSelectedItem().toString());
                link.setUrl(editTextLink.getText().toString());

                try {
                    link.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }
        });

        // Delete button functionality


    }

}