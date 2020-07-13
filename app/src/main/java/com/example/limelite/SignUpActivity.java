package com.example.limelite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    // Initialize widget variables
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextEmail;
    EditText editTextSignUpUsername;
    EditText editTextSignUpPassword;
    EditText editTextSignUpPasswordRetype;
    Button buttonSignUpPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Assign widgets to widget variables
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSignUpUsername = findViewById(R.id.editTextSignUpUsername);
        editTextSignUpPassword = findViewById(R.id.editTextSignUpPassword);
        editTextSignUpPasswordRetype = findViewById(R.id.editTextSignUpPasswordRetype);
        buttonSignUpPage = findViewById(R.id.buttonSignUpPage);

        // onClick listener for Sign Up button
        buttonSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize sign up variables
                String firstName;
                String lastName;
                String email;
                String username;
                String password;
                String passwordRetype;

                // Assign Sign Up Variables
                try {
                    firstName = editTextFirstName.getText().toString();
                    lastName = editTextLastName.getText().toString();
                    email = editTextEmail.getText().toString();
                    username = editTextSignUpUsername.getText().toString();
                    password = editTextSignUpPassword.getText().toString();
                    passwordRetype = editTextSignUpPasswordRetype.getText().toString();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(),"You need to fill all entries!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if passwords match
                if (!(password.equals(passwordRetype))) {
                    Toast.makeText(getBaseContext(),"Passwords must match!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // Create the ParseUser
                    ParseUser user = new ParseUser();
                    // Set core properties
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);
                    // Set custom properties
                    user.put("firstName", firstName);
                    user.put("lastName", lastName);
                    // Invoke signUpInBackground
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now.
                                Toast.makeText(getBaseContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(i);
                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                Toast.makeText(getBaseContext(), "Sign Up Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });


    }
}