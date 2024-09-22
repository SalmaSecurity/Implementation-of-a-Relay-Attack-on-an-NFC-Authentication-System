package com.example.attackerc;

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
        while (true) {
            byte data[] = message.getBytes();
            DatagramPacket dp = new DatagramPacket(data, data.length, remoteAddress, port);
            try {
                ds.send(dp);
                Thread.sleep(1000); // wait for 1 second between each packet
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
