package com.djh.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author duan
 * @create 2021-07-13 8:59
 */
public class ClientThread extends Thread {
    public boolean isRunning = true;
    private Socket client = null;
    private PrintWriter output = null;
    private BufferedReader input = null;
    private String name;

    public ClientThread(Socket client, PrintWriter output, BufferedReader input, String name) {
        this.client = client;
        this.input = input;
        this.output = output;
        this.name = name;
    }

    @Override
    public void run() {
        while (isRunning) {
            String message;
            try {
                message = input.readLine();
                if (message == null) {
                    client.close();
                    isRunning = false;
                    return;
                }
                if (message.equals("you has quit")) {
                    System.out.println(message);
                    client.close();
                    isRunning = false;
                    break;
                } else {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
