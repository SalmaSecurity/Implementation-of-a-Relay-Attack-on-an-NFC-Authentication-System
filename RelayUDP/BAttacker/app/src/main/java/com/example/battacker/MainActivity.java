package com.example.battacker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class MainActivity extends AppCompatActivity {
    private EditText IPinput2;
    private Button butn;

    private TextView textVie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IPinput2= findViewById(R.id.IPinput2);
        butn= findViewById(R.id.butn);
        textVie=findViewById(R.id.textVie);


        Log.d("TAG", "here");        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(3030);
        } catch (SocketException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to create DatagramSocket", Toast.LENGTH_SHORT).show();
            return;
        }

        DatagramSocket finalDs = ds;

        DatagramSocket finalDs1 = ds;
        DatagramSocket finalDs3 = ds;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    byte buffer[] = new byte[1024];
                    DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                    try {
                        finalDs3.receive(dp);
                        String str="";
                        str = new String(dp.getData(), 0, dp.getLength());
                        String finalStr = str;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textVie.setText(finalStr);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        DatagramSocket finalDs2 = ds;
        butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = IPinput2.getText().toString();
                MyThread t = new MyThread(finalDs2, 5555, "10.1.35.52", message);
                t.start();
            }
        });
    }

}