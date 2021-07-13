package com.djh.sever;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author duan
 * @create 2021-07-13 14:31
 */
public class SocketServer {
    private static ConcurrentHashMap<String, ServerThread> clientThread = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try {
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            ServerSocket serverSocket = new ServerSocket(1234, 100, addr);
            Socket client = null;
            System.out.println("服务器启动");
            while (true) {
                client = serverSocket.accept();
                ServerThread serverThread = new ServerThread(client, clientThread);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
