package com.djh.client;

import java.io.IOException;

/**
 * @author duan
 * @create 2021-07-11 12:58
 */
public class ClientTest {
    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.4");
        client.login();
        if (client.isLogin)
            client.talk();
    }



}
