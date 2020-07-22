package com.example.limelite.fragments;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limelite.MainActivity;
import com.example.limelite.R;
import com.example.limelite.helpers.NfcHelper;
import com.parse.ParseUser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;


public class NFCFragment extends Fragment  {

//    private NfcHelper nfcHelper;
//    private Handler handler;
//    private BluetoothAdapter btAdapter;
    private ProgressDialog waitingDialog;
    private TextView textViewNFC;
    private Button buttonNFC;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        btAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        if (btAdapter == null) {
//            Toast.makeText(getContext(), "Bluetooth not available", Toast.LENGTH_SHORT).show();
//
//            getActivity().finish();
//            return;
//        }
//
//        nfcHelper = new NfcHelper(getActivity());
//
//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//
//                Toast.makeText(getContext(), "Beam sent!", Toast.LENGTH_LONG).show();
//
//            }
//        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_n_f_c, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewNFC = view.findViewById(R.id.textViewNFC);
//        buttonNFC = view.findViewById(R.id.buttonNFC);
//        buttonNFC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBtInviteNfcClick(view);
//            }
//        });
//
//        if (hasNfc()) {
//            Toast.makeText(getContext(), "NFC is available.", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(getContext(), "NFC is not available on this device. This application may not work correctly.",
//                    Toast.LENGTH_LONG).show();
//        }
//
    }
//
//    public boolean hasNfc() {
//        boolean hasFeature = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
//        boolean isEnabled = NfcAdapter.getDefaultAdapter(getContext()).isEnabled();
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
//        handleIntent(getActivity().getIntent());
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
//
//    public void onBtInviteNfcClick(View view) {
//        waitingDialog = new ProgressDialog(getContext());
//        waitingDialog.setMessage("Waiting for opponent...");
//        waitingDialog.setCancelable(true);
//
//        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getContext());
//        nfcAdapter.setNdefPushMessageCallback(MainActivity.cal, getActivity());
//        nfcAdapter.setOnNdefPushCompleteCallback(this, getActivity());
//
//        waitingDialog.show();
//    }
}