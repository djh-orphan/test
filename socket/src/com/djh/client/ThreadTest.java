package com.djh.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author duan
 * @create 2021-07-17 18:16
 */
public class ThreadTest extends Thread {
    private final Socket client;
    private final PrintWriter output;
    private final BufferedReader input;
    private String name;
    ClientThread clientThread;

    public ThreadTest(InetAddress IP, int port, String name) throws IOException {
        this.client = new Socket("127.0.0.1", 1234, IP, port);
        this.output = new PrintWriter(client.getOutputStream(), true);
        this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.name = name;
        this.clientThread = new ClientThread(client, output, input, name);
//        this.clientThread=new ClientThread(client,output,input,name);
    }

    @Override
    public void run() {
        try {
            clientThread.start();
            output.println("/login " + name);
            output.println("//hi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
