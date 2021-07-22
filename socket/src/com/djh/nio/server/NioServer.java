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
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author duan
 * @create 2021-07-18 9:27
 */
public class NioServer {
    //    private ByteBuffer buffer;
    private static final String LOGIN_S = "(^/login)(\\s+)(.*)";
    private static final Pattern LOGIN_P = Pattern.compile(LOGIN_S);
    private static final String PRIVATE_S = "(^/to)(\\s+)(\\S+\\b)(\\s+)(.*)";
    private static final Pattern PRIVATE_P = Pattern.compile(PRIVATE_S);
    private static final String PRESET_S = "(^//)(\\S+\\b)(\\s*)(\\S*)";
    private static final Pattern PRESET_P = Pattern.compile(PRESET_S);

    private static final ConcurrentHashMap<String, SelectionKey> hashmap = new ConcurrentHashMap<>();


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
                    iterator.remove();
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
//                        System.out.println(socketChannel.socket().getInetAddress().getHostName()+"接入");
                    }
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        buffer.clear();
                        String info = "";
                        try {
                            if (socketChannel.read(buffer) > 0) {
                                buffer.flip();
                                info = StandardCharsets.UTF_8.decode(buffer).toString();
                                buffer.clear();
                            }
                            info = info.replaceAll("\n", "");
                            talk(info, hashmap, key);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                            socketChannel.close();
//                            continue;
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String message, SelectionKey key) throws IOException {
//        buffer.clear();
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.write(ByteBuffer.wrap((message + "\n").getBytes(StandardCharsets.UTF_8)));

    }

    public static void broadcast(String message, ConcurrentHashMap<String, SelectionKey> hashMap, SelectionKey key, String otherName) throws IOException {
        String name = (String) key.attachment();
        for (ConcurrentHashMap.Entry<String, SelectionKey> entry : hashMap.entrySet()) {
            if (!name.equals(entry.getKey())) {
                if (!otherName.equals(entry.getKey())) {
                    sendMessage(message, entry.getValue());
                }
            }
        }
    }

    public static void login(String name, ConcurrentHashMap<String, SelectionKey> hashMap, SelectionKey key) throws IOException {
        SelectionKey temp = hashMap.putIfAbsent(name, key);
        if (temp == null) {
            key.attach(name);
            sendMessage("You have logined", key);
            broadcast(name + " has logined", hashMap, key, "");

        } else {
            sendMessage("Name exist, please choose another name.", key);
        }
    }

    public static void talk(String info, ConcurrentHashMap<String, SelectionKey> hashMap, SelectionKey key) throws IOException {
        Matcher loginMatch = LOGIN_P.matcher(info);
        Matcher privateMatch = PRIVATE_P.matcher(info);
        Matcher preSetMatch = PRESET_P.matcher(info);
        String name = (String) key.attachment();
        if (loginMatch.find()) {
            if (name == null) {
                login(loginMatch.group(3), hashMap, key);
                return;
            } else {
                sendMessage(info, key);
                return;
            }
        }
        if (info.equals("/quit")) {
            if (name != null) {
//                hashMap.remove(name, key);
                System.out.println(name + "quit");
                broadcast(name + " has quit", hashMap, key, "");
                sendMessage("you have quit.", key);
                hashMap.remove(name, key);
                key.channel().close();
                return;
            }
        }
        if (privateMatch.find()) {
            if (!hashMap.containsKey(privateMatch.group(3))) {
                sendMessage("user_name is not online.", key);
                return;
            } else if (name.equals(privateMatch.group(3))) {
                sendMessage("Stop talking to yourself!", key);
                return;
            }
            SelectionKey keyFromInfo = hashMap.get(privateMatch.group(3));
            sendMessage(name + " 对你说：" + privateMatch.group(5), keyFromInfo);
            sendMessage("你对 " + privateMatch.group(3) + "说：" + privateMatch.group(5), key);
            return;
        }
        if (preSetMatch.find()) {
            String preSet = preSetMatch.group(2);
            if ("hi".equals(preSet)) {
                String nameFromInfo = preSetMatch.group(4);
                if (nameFromInfo.equals("")) {
                    sendMessage("你向大家打招呼，“Hi，大家好！我来咯~”", key);
                    broadcast(name + "向大家打招呼，“Hi，大家好！我来咯~”", hashMap, key, "");
                    return;
                } else {
                    if (!hashMap.containsKey(nameFromInfo)) {
                        sendMessage("user_name is not online.", key);
                        return;
                    } else if (name.equals(nameFromInfo)) {
                        sendMessage("You can't say hi to yourself!! Please stop talking to yourself!!", key);
                        return;
                    } else {
                        SelectionKey temp = hashMap.get(nameFromInfo);
                        sendMessage("你向" + nameFromInfo + "打招呼：“Hi，你好啊~~”", key);
                        sendMessage(name + "向你打招呼：“Hi，你好啊~”", temp);
                        broadcast(name + "向" + nameFromInfo + "打招呼：“Hi，你好啊~”", hashMap, key, nameFromInfo);
                        return;
                    }
                }
            } else if ("smile".equals(preSet)) {
                String nameFromInfo = preSetMatch.group(4);
                if (nameFromInfo.equals("")) {
                    sendMessage("你向大家打招呼，脸上露出天真无邪的笑容", key);
                    broadcast("大家好，我是" + name + "（脸上带着天真无邪的笑容，这家伙不像是好人，大家不要相信他！）", hashMap, key, "");
                    return;
                } else {
                    if (!hashMap.containsKey(nameFromInfo)) {
                        sendMessage("user_name is not online.", key);
                        return;
                    } else if (name.equals(nameFromInfo)) {
                        sendMessage("You can't smile to yourself!! Please stop talking to yourself!!", key);
                        return;
                    } else {
                        SelectionKey temp = hashMap.get(nameFromInfo);
                        sendMessage("你向" + nameFromInfo + "露出了天真无邪的笑容。", key);
                        sendMessage(name + "向你露出了天真无邪的笑容。", temp);
                        broadcast(name + "向" + nameFromInfo + "露出了天真无邪的笑容，大家快来围观！", hashMap, key, nameFromInfo);
                        return;
                    }
                }
            }
        }
        if (info.equals("/who")) {
            StringBuilder usr = new StringBuilder("用户名：\n");
            for (String temp : hashMap.keySet()) {
                usr.append(temp).append("\n");
            }
            usr.append("Total online user: ").append(hashMap.size());
            sendMessage(usr.toString(), key);
            return;
        }
        if ("Please input invalid message!".equals(info)) {
            sendMessage(info, key);
            return;
        }
        sendMessage("你说：" + info, key);
        broadcast(name + "说：" + info, hashMap, key, "");
    }
}
