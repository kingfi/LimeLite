package com.example.limelite.fragments;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.limelite.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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

    private void loadMap(GoogleMap map) {
        gmap = map;

        if (gmap != null) {
            //Map is ready
            Log.i(TAG, "Map is Ready");
//            MapFragmentFragmentPermissionsDispatcher.getMyLocationWithPermissionCheck(this);
            MapFragmentPermissionsDispatcher.getMyLocationWithPermissionCheck(this);

        }
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

//                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        ParseGeoPoint geoPoint = receivedUser.getParseGeoPoint("location");
                        LatLng latLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());

                        gmap.addMarker(new MarkerOptions().position(latLng).title(receivedUser.getUsername()).draggable(true));
                        Log.i(TAG, receivedUser.getUsername() + " Location: " + receivedUser.get("location").toString());
                    }
                }
            }
        });

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

}