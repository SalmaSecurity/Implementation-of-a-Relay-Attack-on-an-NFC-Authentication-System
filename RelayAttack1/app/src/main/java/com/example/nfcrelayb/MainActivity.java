package com.example.nfcrelayb;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MainActivity extends Activity implements
        CreateNdefMessageCallback, OnNdefPushCompleteCallback {

    TextView textInfo,tv,textView4;
    EditText textOut;

    NfcAdapter nfcAdapter;
    DatagramSocket udpSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textInfo = findViewById(R.id.put);
        textOut = findViewById(R.id.textOut);
        textView4 = findViewById(R.id.textView4);
        tv = findViewById(R.id.tv);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(MainActivity.this,
                    "NFC adapter not available",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this,
                    "NFC adapter available",
                    Toast.LENGTH_LONG).show();
            nfcAdapter.setNdefPushMessageCallback(this, this);
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }

        try {
            udpSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,
                    "Failed to create UDP socket",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action != null && action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            processNfcIntent(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        final String eventString = "onNdefPushComplete\n" + event.toString();
        runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                eventString,
                Toast.LENGTH_LONG).show());
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String stringOut = textOut.getText().toString();
        byte[] bytesOut = stringOut.getBytes();

        NdefRecord ndefRecordOut = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA,
                "text/plain".getBytes(),
                new byte[]{},
                bytesOut);
        NdefMessage ndefMessageOut = new NdefMessage(ndefRecordOut);
        return ndefMessageOut;
    }

    private void processNfcIntent(Intent intent) {
        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (parcelables != null && parcelables.length > 0) {
            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord ndefRecordIn = inNdefRecords[0];
            String inMsg = new String(ndefRecordIn.getPayload());
            textInfo.setText(inMsg);
            Toast.makeText(MainActivity.this, "Message Received: " + inMsg, Toast.LENGTH_SHORT).show();

            sendUdpMessage(inMsg);
        }
    }

    private void sendUdpMessage(final String message) {
        new Thread(() -> {
            try {
                InetAddress serverAddress = InetAddress.getByName("100.91.177.178");
                int serverPort = 3030;

                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                udpSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
