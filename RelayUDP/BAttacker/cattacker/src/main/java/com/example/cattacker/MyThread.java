package com.example.cattacker;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MyThread extends Thread {
    DatagramSocket ds;
    int port;
    InetAddress remoteAddress;

    String message;

    public MyThread(DatagramSocket ds2, int p, String remoteIP,String msg) {
        ds = ds2;
        port = p;
        message=msg;
        try {
            remoteAddress = InetAddress.getByName(remoteIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Scanner sc = new Scanner(System.in);

        byte data[] = message.getBytes();

        DatagramPacket dp = new DatagramPacket(data, data.length, remoteAddress, port);
        try {
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
