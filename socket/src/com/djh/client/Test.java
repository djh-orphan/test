package com.djh.client;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author duan
 * @create 2021-07-17 17:52
 */
public class Test {
    public static void main(String[] args) throws IOException {
        try {
            InetAddress address = InetAddress.getByName("127.0.0.4");
            for (int i = 0; i < 10; i++) {
                String name = "name" + i;
                int port = 4450 + i;
                try {
                    ThreadTest threadTest = new ThreadTest(address, port, name);
                    threadTest.start();
                    System.out.println(threadTest.getName() + "建立");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
        }
    }
}
