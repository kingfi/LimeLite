package com.example.limelite.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limelite.R;
import com.example.limelite.fragments.MapFragment;
import com.example.limelite.fragments.NFCFragment;
import com.example.limelite.fragments.ProfileFragment;
import com.example.limelite.helpers.NFCUtils;
import com.example.limelite.models.Relationships;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class MainActivity extends AppCompatActivity  {

    public static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    private NfcAdapter _nfcAdapter;
    private PendingIntent _pendingIntent;
    private IntentFilter[] _intentFilters;
    private final String _MIME_TYPE = "text/plain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set up bottom navigation view
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.action_nfc:
                        fragment = new NFCFragment();
                        break;
                    case R.id.action_map:
                        fragment = new MapFragment();
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_profile);

        nfcInit();


    }

    // Initialize NFC adapter and check for NFC functionality in partner device
    private void nfcInit() {
        _nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (_nfcAdapter == null)
        {
            Toast.makeText(this, "This device does not support NFC.", Toast.LENGTH_LONG).show();
            return;
        }

        if (_nfcAdapter.isEnabled())
        {
            _pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

            IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            try
            {
                ndefDetected.addDataType(_MIME_TYPE);
            } catch (IntentFilter.MalformedMimeTypeException e)
            {
                Log.e(this.toString(), e.getMessage());
            }

            _intentFilters = new IntentFilter[] { ndefDetected };
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        _enableNdefExchangeMode();
    }

    // Set up adapters to prepare for information exchange (Sending object id of current user)
    private void _enableNdefExchangeMode()
    {
        String msg = ParseUser.getCurrentUser().getObjectId();
        String stringMessage = msg;

        NdefMessage message = NFCUtils.getNewMessage(_MIME_TYPE, stringMessage.getBytes());

        _nfcAdapter.setNdefPushMessage(message, this);
        _nfcAdapter.enableForegroundDispatch(this, _pendingIntent, _intentFilters, null);
    }

    // new Intent on receiving of NFC message
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        // When NFC tag from other device is detected, process information
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
        {
            List<String> msgs = NFCUtils.getStringsFromNfcIntent(intent);

            Toast.makeText(this, "Message received : "+msgs.get(0), Toast.LENGTH_LONG).show();

            // Query for ParseUser associated with objectId received via NFC
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", msgs.get(0));
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        // The query was successful.

                        //Create new relationship
                        Relationships relationship = new Relationships();
                        relationship.setRequestor(objects.get(0));
                        relationship.setRequestee(ParseUser.getCurrentUser());
                        relationship.setStatus(1);

                        try {
                            relationship.save();
                            Log.i(TAG, "Relationship saved");
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }

                        Intent i = new Intent(getBaseContext(), FriendActivity.class);
                        i.putExtra(ParseUser.class.getSimpleName(), Parcels.wrap(objects.get(0)));

                        startActivity(i);
                    } else {
                        // Something went wrong.
                        Log.i(TAG, "Error in processing new friend");
                    }
                }
            });




        }
    }

}