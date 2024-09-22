package com.example.attackerc;

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

public class MainActivityB extends AppCompatActivity {

    private EditText inputtt;
    private Button butttn;
    private TextView texttVie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputtt= findViewById(R.id.inputtt);
        butttn= findViewById(R.id.butttn);
        texttVie=findViewById(R.id.texttVie);



        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(5555);
        } catch (SocketException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to create DatagramSocket", Toast.LENGTH_SHORT).show();
            return;
        }

        DatagramSocket finalDs1 = ds;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    byte buffer[] = new byte[1024];
                    DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                    try {
                        finalDs1.receive(dp);
                        String str="";
                        str = new String(dp.getData(), 0, dp.getLength());
                        String finalStr = str;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                texttVie.setText(finalStr);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        DatagramSocket finalDs = ds;
        butttn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message=inputtt.getText().toString();
                MyThread t = new MyThread(finalDs, 3030, "100.91.176.206",message); // Replace with the IP address of Phone C
                t.start();
            }
        });
    }
}