package com.djh.sever;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class SocketServer {
    public static void main(String[] args) {
        try {
            InetAddress addr = InetAddress.getByName("127.0.0.3");
            ServerSocket serverSocket = new ServerSocket(1234, 100, addr);//创建绑定到特定端口的服务器Socket。
            Socket socket = null;//需要接收的客户端Socket
            int count = 0;//记录客户端数量
            System.out.println("服务器启动");
            //定义一个死循环，不停的接收客户端连接
            while (true) {
                socket = serverSocket.accept();//侦听并接受到此套接字的连接
                InetAddress inetAddress = socket.getInetAddress();//获取客户端的连接
                ServerThread thread = new ServerThread(socket, inetAddress);//自己创建的线程类
                Exception e = thread.erro;
                thread.start();//启动线程
                count++;//如果正确建立连接
                System.out.println("客户端数量：" + count);//打印客户端数量
                if (e.getMessage().equals("Connection reset")) {
                    count--;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
