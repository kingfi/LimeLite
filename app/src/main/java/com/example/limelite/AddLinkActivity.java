package com.example.limelite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.limelite.models.Link;
import com.parse.ParseException;
import com.parse.ParseUser;

public class AddLinkActivity extends AppCompatActivity {

    private Spinner spinnerAddType;
    private EditText editTextAddLink;
    private Button buttonAddLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_link);

        //Initialize the variables
        spinnerAddType = findViewById(R.id.spinnerAddType);
        editTextAddLink = findViewById(R.id.editTextAddLink);
        buttonAddLink = findViewById(R.id.buttonAddLink);

        //Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.linkTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddType.setAdapter(adapter);

        // onClick functionality for Add button
        buttonAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Link newLink = new Link();
                newLink.setUrl(editTextAddLink.getText().toString());
                newLink.setType(spinnerAddType.getSelectedItem().toString());
                newLink.setUser(ParseUser.getCurrentUser());

                try {
                    newLink.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }
        });

    }
}