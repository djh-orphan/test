package com.djh.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final ArrayList<String> message;

    public Client(String IP) throws IOException {
        try {
            InetAddress addr = InetAddress.getByName(IP);
            this.client = new Socket("127.0.0.1", 1234, addr, 4550);
            this.output =
                    new PrintWriter(this.client.getOutputStream(), true);
            this.input =
                    new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.clientThread = new ClientThread(this.client, this.output, this.input, this.name);
            this.message = new ArrayList<>(100);
        } catch (BindException e) {
            System.out.println(e.getMessage());
            System.out.println("请检查客户端socket使用的源IP地址和本地端口是否已经被其他客户端使用");
            throw e;
        } catch (ConnectException e) {
            System.out.println(e.getMessage());
            System.out.println("请检查客户端socket中目的IP地址是否正确");
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
                        message.add(commandLine);
                        System.out.println("you just quit");
                        isLogin = false;
                        client.close();
                        break;
                    }
                    System.out.println("Invalid command");
                    continue;
                }
                output.println(commandLine);
                message.add(commandLine);
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
        } catch (SocketException e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("服务器端的连接断开,请重试");
            isLogin = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void talk() {
        try {
            clientThread.start();
            String pattren = "(^/history)(\\s*)([0-9]*)(\\s*)([0-9]*)(.*)";
            Pattern regex = Pattern.compile(pattren);
//            Matcher matcher=regex.matcher()
            while (clientThread.isRunning) {
                String commandLine = sc.nextLine();
                Matcher matcher = regex.matcher(commandLine);
                if (!clientThread.isRunning) {
                    message.clear();
                    break;
                }
                if (commandLine.length() == 0) {
                    output.println("Please input invalid message!");
                    message.add(commandLine);
                } else if (matcher.find()) {
                    if (matcher.group(6).equals("")) {
                        if (matcher.group(5).equals("")) {
                            if (matcher.group(3).equals("")) {
                                StringBuilder history = new StringBuilder();
                                message.add(commandLine);
                                if (message.size() >= 50) {
                                    for (int i = 0; i < 50; i++) {
                                        history.append(i + 1).append("\t").append(message.get(i)).append("\n");
                                    }
                                    System.out.println(history.toString());
                                } else {
                                    for (int i = 0; i < message.size(); i++) {
                                        history.append(i + 1).append("\t").append(message.get(i)).append("\n");
                                    }
                                    System.out.println(history.toString());
                                }
                            } else {
                                System.out.println("Invalid command");
                                continue;
                            }
                        } else {
                            try {
                                int startIndex = Integer.parseInt(matcher.group(3));
                                int maxCount = Integer.parseInt(matcher.group(5));
                                message.add(commandLine);
                                StringBuilder history = new StringBuilder();
                                if (startIndex < message.size()) {
                                    if (startIndex + maxCount < message.size()) {
                                        for (int i = startIndex - 1; i < startIndex + maxCount; i++) {
                                            history.append(i + 1).append("\t").append(message.get(i)).append("\n");
                                        }
                                        System.out.println(history.toString());
                                    } else {
                                        for (int i = startIndex - 1; i < message.size(); i++) {
                                            history.append(i + 1).append("\t").append(message.get(i)).append("\n");
                                        }
                                        System.out.println(history.toString());
                                    }
                                } else {
                                    System.out.println("你输入的startIndex超出索引范围，请重新输入");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid command");
                            }

                        }
                    } else {
                        System.out.println("Invalid command");
                    }
                } else {
                    output.println(commandLine);
                    message.add(commandLine);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
//            throw e;
        }
    }

}
