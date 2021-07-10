package com.djh.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author duan
 * @create 2021-07-10 16:43
 */
public class Client {
   public static void main(String[] args) throws IOException {
      InetAddress addr = InetAddress.getByName("127.0.0.2");
      Socket client = new Socket("127.0.0.3", 1234, addr, 4550);
      PrintWriter output =
              new PrintWriter(client.getOutputStream(), true);
      Scanner sc = new Scanner(System.in);
      String words;
//        while (!sc.hasNext("e")){
      while (true) {
         words = sc.nextLine();
         output.println(words);
         System.out.println("客户端写出了：" + words);
         if (!sc.hasNext("exit")) {
            continue;
         } else {
            break;
         }
      }
      sc.close();
      client.close();


   }
}
