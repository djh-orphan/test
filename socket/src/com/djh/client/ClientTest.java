package com.djh.client;

import java.io.IOException;

/**
 * @author duan
 * @create
 */
public class ClientTest {
    public static void main(String[] args) throws IOException {
        try {
            Client client = new Client("127.0.0.6");
            client.login();
            if (client.isLogin)
                client.talk();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if ("Connection refused: connect".equals(e.getMessage())) {
                System.out.println("请检查客户端socket中目的IP地址是否正确");
            }
            if (e.getMessage().contains("Address already in use")) {
                System.out.println("请检查客户端socket使用的源IP地址是否已经被其他客户端使用");
            }
        }
    }


}
