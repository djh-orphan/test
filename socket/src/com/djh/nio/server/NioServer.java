package com.djh.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author duan
 * @create 2021-07-18 9:27
 */
public class NioServer {
    //    private ByteBuffer buffer;
    public static void main(String[] args) throws IOException {
        Selector selector;
        ServerSocketChannel serverSocketChannel;
        try {
            selector = Selector.open();
            InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 1234);
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(socketAddress, 100);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器启动");
            while (true) {
                int n = selector.select();
                if (n == 0) {
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
//                    iterator.remove();
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
//                        System.out.println(socketChannel.socket().getInetAddress().getHostName()+"接入");
                    }
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
//                        ByteBuffer buffer=(ByteBuffer) key.attachment();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        buffer.clear();
//                        int count;
                        String info = "";
                        if (socketChannel.read(buffer) > 0) {
                            buffer.flip();
                            info = StandardCharsets.UTF_8.newDecoder().decode(buffer).toString();
                            buffer.clear();
//                            buffer.put("hbjh")
                        }
                        talk(info, socketChannel);
                    }
                    iterator.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String message, SocketChannel socketChannel) throws IOException {
//        buffer.clear();
        socketChannel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));

    }

    public static void talk(String info, SocketChannel socketChannel) throws IOException {
        sendMessage(info, socketChannel);
    }
}
