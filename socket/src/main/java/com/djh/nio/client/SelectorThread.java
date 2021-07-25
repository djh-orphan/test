package com.djh.nio.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author duan
 * @create 2021-07-21 16:45
 */
public class SelectorThread extends Thread {
    private Selector selector;
    private SocketChannel socketChannel;
    public boolean isRunning = true;

    public SelectorThread(Selector selector, SocketChannel socketChannel) {
        this.selector = selector;
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                int n = selector.select();
                if (n == 0) {
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isConnectable()) {
                        while (!socketChannel.finishConnect()) {
                            continue;
                        }
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    if (key.isWritable()) {
                        socketChannel.write((ByteBuffer) key.attachment());
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        buffer.clear();
                        String info = "";
                        try {
                            if (socketChannel.read(buffer) > 0) {
                                buffer.flip();
                                info = StandardCharsets.UTF_8.decode(buffer).toString();
                                buffer.clear();
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                            isRunning = false;
                            return;
                        }
//                        info=info.replace("\n","");
                        System.out.print(info);
                        if (info.contains("you have quit.")) {
                            isRunning = false;
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
