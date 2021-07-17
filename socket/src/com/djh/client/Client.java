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
    public boolean isLogin = true;
    private final Socket client;
    private final PrintWriter output;
    private final BufferedReader input;
    private final Scanner sc = new Scanner(System.in);
    private final ClientThread clientThread;
    private String name;

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
            System.out.println(e.getMessage());
            if ("Connection refused: connect".equals(e.getMessage())) {
                System.out.println("请检查客户端socket中目的IP地址是否正确");
            }
            if (e.getMessage().contains("Address already in use")) {
                System.out.println("请检查客户端socket使用的源IP地址和本地端口是否已经被其他客户端使用");
            }
            throw e;
        }

    }


    public void login() {
        try {
            System.out.println("Please login");
            String tips1 = "Name exist, please choose another name.";
            String in;
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
                    String[] inSpilt = commandLine.split("\\s+");
                    name = inSpilt[1];
                    break;
                } else {
                    System.out.println("Name exist, please choose another name.");
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
            if ("Connection reset".equals(e.getMessage())) {
                System.out.println("服务器端的连接断开,请重试");
                isLogin = false;
            }
        }
    }

    public void talk() {
        try {
            clientThread.start();
            while (clientThread.isRunning) {
                String commandLine = sc.nextLine();
                if (!clientThread.isRunning) {
                    break;
                }
                if (commandLine.length() == 0) {
                    output.println("Please input invalid message!");
                } else {
                    output.println(commandLine);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
//            throw e;
        }
    }

}
