package com.example.limelite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limelite.fragments.MapFragment;
import com.example.limelite.fragments.NFCFragment;
import com.example.limelite.fragments.ProfileFragment;
import com.example.limelite.helpers.NfcHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity  {

    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private NfcHelper nfcHelper;
    private Handler handler;
    private BluetoothAdapter btAdapter;
    private ProgressDialog waitingDialog;
    private TextView textViewNFC;
    private Button buttonNFC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        textViewNFC = findViewById(R.id.textViewNFC);
//        buttonNFC = findViewById(R.id.buttonNFC);

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

//        btAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        if (btAdapter == null) {
//            Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_SHORT).show();
//
//            finish();
//            return;
//        }
//
//        nfcHelper = new NfcHelper(this);
//
//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//
//                Toast.makeText(getApplicationContext(), "Beam sent!", Toast.LENGTH_LONG).show();
//
//            }
//        };
//
//        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        nfcAdapter.setNdefPushMessageCallback(this, this);
//        nfcAdapter.setOnNdefPushCompleteCallback(this, this);

//        buttonNFC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBtInviteNfcClick(view);
//            }
//        });
//
//        if (hasNfc()) {
//            Toast.makeText(this, "NFC is available.", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "NFC is not available on this device. This application may not work correctly.",
//                    Toast.LENGTH_LONG).show();
//        }
    }


//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//    }
//
//    public boolean hasNfc() {
//        boolean hasFeature = getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
//        boolean isEnabled = NfcAdapter.getDefaultAdapter(this).isEnabled();
//
//        return hasFeature && isEnabled;
//    }
//
//    @Override
//    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
//        ParseUser user = ParseUser.getCurrentUser();
//
//        Log.i("NFC", user.getUsername());
//        return nfcHelper.createExternalTypeNdefMessage("limelite user", objectToBytes(user));
//    }
//
//    private byte[] objectToBytes(Object obj) {
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        ObjectOutput objOutput = null;
//
//        try {
//            objOutput = new ObjectOutputStream(byteStream);
//            objOutput.writeObject(obj);
//
//            objOutput.close();
//            byteStream.close();
//
//            Log.i("NFC", "objectToBytesWorked");
//
//        } catch (Exception e) {
//            Log.e("objectToBytes", e.getMessage());
//        }
//
//        return byteStream.toByteArray();
//    }
//
//    @Override
//    public void onNdefPushComplete(NfcEvent nfcEvent) {
//        handler.obtainMessage(1).sendToTarget();
//        Log.i("NFC", "ndefPushComplete");
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        handleIntent(getIntent());
//        Log.i("NFC", "intent handled");
//
//    }
//
//    private void handleIntent(Intent intent) {
//
//        Log.i("NFC", "starting to handle intent");
//
//        if (!nfcHelper.isNfcIntent(intent)) {
//            Log.i("NFC", "shit failed ngl");
//
//            return;
//        }
//
//        if (waitingDialog != null) {
//            Log.i("NFC", "waiting dialog to be dismissed");
//
//            waitingDialog.dismiss();
//        }
//
//        NdefMessage ndefMessage = nfcHelper.getNdefMessageFromIntent(intent);
//        NdefRecord ndefRecord = nfcHelper.getFirstNdefRecord(ndefMessage);
//
//        ParseUser user = (ParseUser) bytesToObject(ndefRecord.getPayload());
//        Log.i("NFC", "User received is: " + user.getUsername());
//
//
//        textViewNFC.setText(user.getUsername());
//
//    }
//
//    private Object bytesToObject(byte[] bytes) {
//        Log.i("NFC", "turning bytes to object");
//
//        Object o = null;
//        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//        ObjectInput in = null;
//
//        try {
//            in = new ObjectInputStream(bis);
//
//            o = in.readObject();
//
//            bis.close();
//            Log.i("NFC", "bytesToObjectWorked");
//
//        } catch (Exception e) {
//            Log.e("bytesToObject", e.getMessage());
//        }
//        return o;
//    }

//    public void onBtInviteNfcClick(View view) {
//        waitingDialog = new ProgressDialog(this);
//        waitingDialog.setMessage("Waiting for opponent...");
//        waitingDialog.setCancelable(true);
//
//        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        nfcAdapter.setNdefPushMessageCallback(this, this);
//        nfcAdapter.setOnNdefPushCompleteCallback(this, this);
//
//        waitingDialog.show();
//    }


}