package com.example.limelite.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.limelite.R;
import com.example.limelite.models.Relationships;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

@RuntimePermissions
public class MapFragment extends Fragment {

    public static final String TAG = "MapFragment";

    private SupportMapFragment mapFragment;
    private GoogleMap gmap;
    private ParseUser user;
    private Location userLocation;
    private ArrayList<Relationships> relationsList;
    private ArrayList<String> friendsList;
    private ArrayList<String> pendingFriends;
    private ArrayList<String> requestingFriends;
    private static Location mCurrentLocation;
    private final static String KEY_LOCATION = "location";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        friendsList = new ArrayList<>();
        pendingFriends = new ArrayList<>();
        requestingFriends = new ArrayList<>();
        relationsList = new ArrayList<>();

        // Query relationships and populate friends lists
        try {
            Log.i(TAG, "AB to QUERY");
            queryRelationships();
            Log.i(TAG, "QUERY DONE");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Set User
        user = ParseUser.getCurrentUser();

        Log.i(TAG, "View Created");
        if (savedInstanceState != null && savedInstanceState.keySet().contains(KEY_LOCATION)) {
            // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
            // is not null.
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.nearbyMap));
        if (mapFragment != null) {
            Log.i(TAG, "Map is not null");
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    Log.i(TAG, "Load map");
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(getContext(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }


    }

    private void loadMap(final GoogleMap map) {
        gmap = map;

        if (gmap != null) {
            //Map is ready
            Log.i(TAG, "Map is Ready");
            MapFragmentPermissionsDispatcher.getMyLocationWithPermissionCheck(this);
        }

        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i(TAG, "CLICKED");
                // Show popup of user's name and profile

                // inflate map_user_item
                View messageView = LayoutInflater.from(getContext()).inflate(R.layout.map_user_item, null);
                // Create alert dialog builder
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                // get User associated with Marker
                final ParseUser clickedUser = (ParseUser) marker.getTag();


                // Load data into view
                TextView usernameView = messageView.findViewById(R.id.map_item_username);
                ImageView profileImage = messageView.findViewById(R.id.map_item_profile);
                TextView textViewStatus = messageView.findViewById(R.id.textViewStatus);

                usernameView.setText(clickedUser.getUsername());
                ParseFile profile = clickedUser.getParseFile("profilePic");
                if (profile != null) {
                    Log.i(TAG, profile.getUrl());
                    Glide.with(getContext()).load(profile.getUrl()).into(profileImage);
                    Log.i(TAG, "Profile Set");
                }

                // set message_item.xml to AlertDialog builder
                alertDialogBuilder.setView(messageView);

                // Create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();

                Log.i(TAG, "Friends: " + friendsList.toString());
                Log.i(TAG, "Pending: " + pendingFriends.toString());
                Log.i(TAG, "Requesting: " + requestingFriends.toString());

                Log.i(TAG, "Clicked User: " + clickedUser.toString());


                if (friendsList.contains(clickedUser.getObjectId())) {
                    Log.i(TAG, "FRIEND");

                    // Alert box for someone who is already friended
                    textViewStatus.setText("You and " + clickedUser.getUsername() + " are friends!");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {dialogInterface.cancel();}
                            });
                } else if (pendingFriends.contains(clickedUser.getObjectId())) {
                    Log.i(TAG, "PENDING");

                    // Alert box for someone who is pending
                    textViewStatus.setText("Waiting for " + clickedUser.getUsername() + " to accept");

                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { dialogInterface.cancel(); }
                    });
                } else if (requestingFriends.contains(clickedUser.getObjectId())) {
                    Log.i(TAG, "REQUESTING");

                    // Alert box for someone who has sent you a friend request

                    // Reject request
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "REJECT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for (Relationships relation: relationsList) {
                                if (relation.getRequestor().getObjectId() == clickedUser.getObjectId()) {
                                    relation.setStatus(2);
                                    try {
                                        relation.save();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            requestingFriends.remove(clickedUser.getObjectId());
                        }
                    });

                    //
                    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { dialogInterface.cancel(); }
                    });

                    //Accept Request
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ACCEPT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.i(TAG, "Clicked ACCEPT");
                            for (Relationships relation: relationsList) {

                                // Add Friend
                                if (relation.getRequestor().getObjectId().equals(clickedUser.getObjectId())) {
                                    relation.setStatus(1);
                                    try {
                                        relation.save();
                                        Log.i(TAG, "ACCEPTED");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                                // Update lists
                                requestingFriends.remove(clickedUser.getObjectId());
                                friendsList.add(clickedUser.getObjectId());
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "UNKNOWN");

                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "SEND REQUEST", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Relationships relationship = new Relationships();
                            relationship.setStatus(0);
                            relationship.setRequestor(user);
                            relationship.setRequestee(clickedUser);
                            try {
                                relationship.save();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            //Update lists
                            pendingFriends.add(clickedUser.getObjectId());
                        }
                    });

                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });


                }

                alertDialog.show();


                return false;
            }
        });

    }

    // Google Maps, retrieve location
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // If this is in a fragment: do SomeClassFragmentPermissionsDispatcher
        MapFragmentPermissionsDispatcher.getMyLocationWithPermissionCheck(this);
    }

    @SuppressWarnings({"MissingPermission"})
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void getMyLocation() {
        gmap.setMyLocationEnabled(true);
        // Access users current location
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(getContext());
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.i(TAG, "Location: " + location.toString());
                            userLocation = location;

                            // On success to get current location: Save current location in Parse

                            ParseGeoPoint geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

                            user.put("location", geoPoint);
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Log.i(TAG, "Location uploaded to parse");
                                }
                            });

                            // Move map camera position to current location
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(location.getLatitude(), location.getLongitude()), 30F);

                            gmap.animateCamera(cameraUpdate);

                            getNearbyUsers(location);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    private void getNearbyUsers(final Location location) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("visible", true);
        query.whereNotEqualTo("objectId", user.getObjectId());


        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    for (ParseUser receivedUser : users) {

                        try{
                            receivedUser.fetch();
                        } catch(ParseException ex) {
                            Log.e(TAG, String.valueOf(ex));
                        }

                        ParseGeoPoint geoPoint = receivedUser.getParseGeoPoint("location");
                        if (geoPoint == null) {
                            continue;
                        }
                        LatLng latLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());

                        Double dist = distance(userLocation.getLatitude(), userLocation.getLongitude(), geoPoint.getLatitude(), geoPoint.getLongitude());

                        // Check if distance is longer than 100 yards
                        if (dist >= 0.0568182) {
                            continue;
                        }

                        ParseFile parseFile = receivedUser.getParseFile("profilePic");

                        Marker marker = gmap.addMarker(new MarkerOptions().position(latLng).title(receivedUser.getUsername()));
                        marker.setTag(receivedUser);

                        try {
                            File file = parseFile.getFile();
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                                    bitmap, 100, 100, false);

                            //rotate bitmap image
                            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
                            Integer orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                            Matrix matrix = new Matrix();
                            if (orientation == 6) {
                                matrix.postRotate(90F);
                            } else if (orientation == 3) {
                                matrix.postRotate(180F);
                            } else if (orientation == 8) {
                                matrix.postRotate(270F);
                            }
                            resizedBitmap = Bitmap.createBitmap(resizedBitmap,0,0, resizedBitmap.getWidth(), resizedBitmap.getHeight(), matrix, true);
                            marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
                        } catch (ParseException | IOException ex) {
                            ex.printStackTrace();
                        }

                        Log.i(TAG, receivedUser.getUsername() + " Location: " + receivedUser.get("location").toString());
                    }
                }
            }
        });

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
            if (relation.getStatus() == 1){
                if (relation.getRequestee().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                    friendsList.add(relation.getRequestor().getObjectId());
                    Log.i(TAG, "FRIEND ADDED: " + relation.getRequestor().fetch().getUsername() + " " + relation.getRequestor().toString());

                }else{
                    friendsList.add(relation.getRequestee().getObjectId());
                    Log.i(TAG, "FRIEND ADDED: " + relation.getRequestee().fetch().getUsername()+ " " + relation.getRequestee().toString());
                }
            } if (relation.getStatus() == 0){
                if (relation.getRequestee().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                    requestingFriends.add(relation.getRequestor().getObjectId());

                }else{
                    pendingFriends.add(relation.getRequestee().getObjectId());
                }
            }
        }

    }

    // Function to calculate distance between two points
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            return (dist);
        }
    }


    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

}