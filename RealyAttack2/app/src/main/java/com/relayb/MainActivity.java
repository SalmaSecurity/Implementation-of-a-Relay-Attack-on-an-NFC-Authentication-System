package com.relayb;

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

public class MainActivity extends Activity implements
        CreateNdefMessageCallback, OnNdefPushCompleteCallback{
        TextView textInfo;
        TextView textView1;
        EditText textOut;
        NfcAdapter nfcAdapter;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                textView1 = (TextView)findViewById(R.id.textView1);
                textInfo = (TextView)findViewById(R.id.textInfo);
                textOut = (EditText)findViewById(R.id.textOut);
                nfcAdapter = NfcAdapter.getDefaultAdapter(this);
                if(nfcAdapter==null){
                        Toast.makeText(MainActivity.this,
                                "nfcAdapter==null, no NFC adapter exists",
                                Toast.LENGTH_LONG).show();
                }else{
                        Toast.makeText(MainActivity.this,
                                "Set Callback(s)",
                                Toast.LENGTH_LONG).show();
                        nfcAdapter.setNdefPushMessageCallback(this, this);
                        nfcAdapter.setOnNdefPushCompleteCallback(this, this);
                }
        }
        @Override
        protected void onResume() {
                super.onResume();
                Intent intent = getIntent();
                String action = intent.getAction();
                if(action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)){
                        Parcelable[] parcelables =
                                intent.getParcelableArrayExtra(
                                        NfcAdapter.EXTRA_NDEF_MESSAGES);
                        NdefMessage inNdefMessage = (NdefMessage)parcelables[0];
                        NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
                        NdefRecord NdefRecord_0 = inNdefRecords[0];
                        String inMsg = new String(NdefRecord_0.getPayload());
                        textInfo.setText(inMsg);
                        Toast.makeText(MainActivity.this, "Message Received: " + inMsg, Toast.LENGTH_SHORT).show();
                }
        }
        @Override
        protected void onNewIntent(Intent intent) {
                setIntent(intent);
        }
        @Override
        public void onNdefPushComplete(NfcEvent event) {
                final String eventString = "onNdefPushComplete\n" + event.toString();
                runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                                Toast.makeText(getApplicationContext(),
                                        eventString,
                                        Toast.LENGTH_LONG).show();
                        }
                });
        }

        @Override
        public NdefMessage createNdefMessage(NfcEvent event) {

                String stringOut = textOut.getText().toString();
                byte[] bytesOut = stringOut.getBytes();

                NdefRecord ndefRecordOut = new NdefRecord(
                        NdefRecord.TNF_MIME_MEDIA,
                        "text/plain".getBytes(),
                        new byte[] {},
                        bytesOut);
                NdefMessage ndefMessageout = new NdefMessage(ndefRecordOut);
                return ndefMessageout;
        }

}