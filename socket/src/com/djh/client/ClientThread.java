package com.djh.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author duan
 * @create 2021-07-13 8:59
 */
public class ClientThread extends Thread {
    public boolean isRunning = true;
    private final Socket client;
    private final PrintWriter output;
    private final BufferedReader input;
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
                    break;
                }
                if (message.equals("you have quit")) {
                    System.out.println(message);
//                    input.close();
                    client.close();
                    isRunning = false;
                    break;
                } else {
                    System.out.println(message);
                }
            } catch (SocketException e) {
                System.out.println("与服务器的连接断开");
                isRunning = false;
                return;
            } catch (IOException e) {
                e.printStackTrace();

            }

        }
    }
}
