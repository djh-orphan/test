package com.djh.client;

/**
 * @author duan
 * @create
 */
public class ClientTest {
    public static void main(String[] args) {
        try {
            Client client = new Client("127.0.0.7");
            client.login();
            if (client.isLogin)
                client.talk();
        } catch (Exception e) {
            return;
        }
    }


}
