package com.example.cattacker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class MainActivitySec extends AppCompatActivity {

    private EditText IPinputt,inputt;
    private Button buttn;

    private TextView textVie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sec);
        IPinputt= findViewById(R.id.IPinputt);
        inputt= findViewById(R.id.inputt);
        buttn= findViewById(R.id.buttn);
        textVie=findViewById(R.id.textVie);

        System.out.println("Phone B");

        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(9090);
        } catch (SocketException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to create DatagramSocket", Toast.LENGTH_SHORT).show();
            return;
        }

        DatagramSocket finalDs = ds;
        buttn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String ip=IPinputt.getText().toString();


                MyThread t = new MyThread(finalDs, 8080, "127.0.0.1","test"); // Replace with the IP address of Phone C

                t.start();

            }
        });

    }
}