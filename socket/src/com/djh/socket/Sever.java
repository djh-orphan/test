package com.djh.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author duan
 * @create 2021-07-10 16:26
 */
public class Sever {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(1234);
        try {
            Socket client = server.accept();
            try {
                BufferedReader input =
                        new BufferedReader(new InputStreamReader(client.getInputStream()));
                Boolean flag = true;
                while (flag) {
//            System.out.println("客户端说：");
                    String line = null;
                    line = input.readLine();
//                    System.out.println(line);
                    if (line.equals("exit")) {
                        flag = false;
                        System.out.println("客户端断开连接");
                        client.close();
                        server.close();
                        System.out.println("Tcp连接关闭");
                    } else {
                        System.out.println("客户端说：" + line);
                    }
                }
            } catch (Exception e) {
                System.out.println("结束");
            }
        } catch (Exception e) {
            System.out.println("结束");
        }


    }
}
