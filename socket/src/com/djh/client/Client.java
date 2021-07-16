package com.djh.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author duan
 * @create 2021-07-10 16:43
 */
public class Client {
    private Socket client;
    private PrintWriter output;
    private BufferedReader input;
    private final Scanner sc = new Scanner(System.in);
    private ClientThread clientThread;
    private String name;
    public boolean isLogin = true;

    public Client(String IP) throws IOException {
        try {
            InetAddress addr = InetAddress.getByName(IP);
            this.client = new Socket("127.0.0.1", 1234, addr, 4550);
            this.output =
                    new PrintWriter(this.client.getOutputStream(), true);
            this.input =
                    new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.clientThread = new ClientThread(this.client, this.output, this.input, this.name);
        } catch (Exception e) {
            throw e;
        }

    }


    public void login() throws IOException {
        try {
            System.out.println("Please login");
            String tips1 = "Name exist, please choose another name.";
            String in = null;
            while (true) {
                String commandLine = sc.nextLine();
                if (!commandLine.contains("/login ")) {
                    if (commandLine.contains("/quit")) {
//                        output.println("/quit");
                        System.out.println("you just quit");
                        isLogin = false;
                        client.close();
                        break;
                    }
                    System.out.println("Invalid command");
                    continue;
                }
                output.println(commandLine);
                in = input.readLine();
                if (!tips1.equals(in)) {
                    System.out.println(in);
                    name = commandLine.substring(7, commandLine.length());
                    break;
                } else {
                    System.out.println("Name exist, please choose another name.");
                }
            }
        } catch (Exception e) {

//            e.printStackTrace();

        }
    }

    public void talk() throws IOException {
        try {
            clientThread.start();
            while (clientThread.isRunning) {
                String commandLine = sc.nextLine();
                if (commandLine.length() == 0) {
                    output.println("Please input invalid message!");
                    continue;
                } else {
                    output.println(commandLine);
                }
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

}
