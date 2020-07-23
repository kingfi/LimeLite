package com.example.limelite.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.limelite.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

import com.parse.SaveCallback;
import com.parse.facebook.ParseFacebookUtils;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Initialize widgets variables
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonSignUp;
    private LoginButton buttonFacebookLogin;
    private Collection<String> permissions = Arrays.asList("public_profile", "email");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Check to see if already logged in
        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        ParseFacebookUtils.initialize(this);

        // Assign widgets to widget variables
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonFacebookLogin = findViewById(R.id.buttonFacebookLogin);

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


        // Facebook Login
        buttonFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            ParseUser.logOut();
                            Log.d("FB", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("FB", "User signed up and logged in through Facebook!");
                            getUserDetailsFromFB(user);
                            goMainActivity();
                            Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("FB", "User logged in through Facebook!");
                            goMainActivity();
                            Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void getUserDetailsFromFB (final ParseUser user) {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                String firstName = "";
                String lastName = "";
                String email = "";
                String username = "";

                Log.i("FB", object.toString());
                Log.i("FB", response.toString());

                try {
                    username = object.getString("name");
                    Log.i("FB", username);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    email = object.getString("email");
                    Log.i("FB", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                user.setUsername(username);
                user.setEmail(email);

                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e("FB", "Issue w/ Facebook login " + e);
                        } else{
                            Log.i("FB", "FB Login good");
                        }
                    }
                });
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        request.setParameters(parameters);
        request.executeAsync();

        if (!ParseFacebookUtils.isLinked(user)) {
            ParseFacebookUtils.linkWithReadPermissionsInBackground(user, this, permissions, new SaveCallback() {
                @Override
                public void done(ParseException ex) {
                    if (ParseFacebookUtils.isLinked(user)) {
                        Log.d("FB", "Woohoo, user logged in with Facebook!");
                    }
                }
            });
        }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}