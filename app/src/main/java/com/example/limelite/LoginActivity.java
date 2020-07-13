package com.example.limelite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    // Initialize widgets variables
    EditText editTextUsername;
    EditText editTextPassword;
    Button buttonLogin;
    Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Check to see if already logged in
        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        // Assign widgets to widget variables
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        // OnClick for Sign Up button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSignUpActivity();
            }
        });

        // OnClick to login with Parse
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick login button");
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                loginUser(username, password);
            }

            // Function to login to Parse
            private void loginUser(String username, String password) {
                Log.i(TAG, "Attempting to login user " + username);
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e !=null){
                            Log.e(TAG, "Issue with login", e);
                            return;
                        }
                        // Navigate to the main activity
                        goMainActivity();
                        Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    // Function to go to the sign up activity
    private void goSignUpActivity() {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);

        // Make sure user cannot click back button back to LoginActivity
        finish();
    }

    // Function to go to the main activity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

        // Make sure user cannot click back button back to LoginActivity
        finish();
    }
}