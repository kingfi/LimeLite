package com.example.limelite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.limelite.fragments.ProfileFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = "SettingsActivity";
    private ImageView imageViewSettingsProfile;
    private Switch switchVisibility;
    private Button buttonSaveSettings;
    private Button buttonLogout;
    private String photoFileName = "profile.jpg";
    private File photoFile = null;
    private ParseFile photofileParse = null;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        imageViewSettingsProfile = findViewById(R.id.imageViewSettingsProfile);
        switchVisibility = findViewById(R.id.switchVisibility);
        buttonSaveSettings = findViewById(R.id.buttonSaveSettings);
        buttonLogout = findViewById(R.id.buttonLogout);

        // set onClick listener for profile picture
        imageViewSettingsProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        // Set profile picture and Visibility switch
        try {
            user = ParseUser.getCurrentUser().fetch();
            ParseFile profile = (ParseFile) user.get("profilePic");
            if (profile != null) {
                Log.i(TAG, profile.getUrl());
                Glide.with(this).load(profile.getUrl()).into(imageViewSettingsProfile);
            }

            Boolean visible = user.getBoolean("visible");
            if (visible == false){
                switchVisibility.setChecked(false);
            }
            else{
                switchVisibility.setChecked(true);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Logout functionality
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "before logout" + ParseUser.getCurrentUser().getUsername());
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });


        // Functionality to save settings
        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save profile picture
                if (photoFile != null) {
                    photofileParse = new ParseFile(photoFile);

                    photofileParse.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseUser.getCurrentUser().put("profilePic", photofileParse);
                                saveVisibility();
                                // Save settings
                                try {
                                    ParseUser.getCurrentUser().save();
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                }

                                Toast.makeText(getBaseContext(),"Saved!", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(getBaseContext(),MainActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                } else {
                    saveVisibility();
                    try {
                        ParseUser.getCurrentUser().save();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    Toast.makeText(getBaseContext(),"Saved!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(i);

                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.imageViewSettingsProfile);
                ivPreview.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider.LimeLite", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);

        return file;
    }

    private void saveVisibility() {
        if (switchVisibility.isChecked()) {
            ParseUser.getCurrentUser().put("visible", true);
        } else{
            ParseUser.getCurrentUser().put("visible", false);
        }
    }
}

