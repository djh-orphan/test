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
 * @create 2021-07-11 12:58
 */
public class ClientTest {
    public static void main(String[] args) throws IOException {
        login();
    }

    public static void login() throws IOException {
        try {
            InetAddress addr = InetAddress.getByName("192.168.0.107");
            Socket client = new Socket("192.144.210.195", 1234, addr, 4551);
            PrintWriter output =
                    new PrintWriter(client.getOutputStream(), true);
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(client.getInputStream()));

            System.out.println("Please login");
            Scanner sc = new Scanner(System.in);
            String commandLine = sc.nextLine();
            Boolean flag = true;
            while (flag) {
                if (!commandLine.substring(0, 6).equals("/login")) {
                    System.out.println(commandLine.substring(0, 6));
                    System.out.println("Invalid command");
                    commandLine = sc.nextLine();
                } else if (commandLine.substring(0, 5).equals("/quit")) {
                    client.close();
                } else {
                    System.out.println("You have logined");
                    output.println("usr_name:" + commandLine.substring(7, commandLine.length()));
                    flag = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//            if(e.getMessage()){


            System.out.println("login失败请重试");
        }
    }
}
