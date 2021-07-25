package com.djh.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author duan
 * @create 2021-07-20 22:25
 */
public class NioClient {
    private static final String LOGIN_S = "(^/login)(\\s+)(.+)";
    private static final Pattern LOGIN_P = Pattern.compile(LOGIN_S);
    private static final String HISTORY_S = "(^/history)(\\s*)([0-9]*)(\\s*)([0-9]*)(.*)";
    private static final Pattern HISTORY_P = Pattern.compile(HISTORY_S);


    private static final ArrayList<String> message = new ArrayList<>(60);

    public static void main(String[] args) {
        try {
            InetSocketAddress address = new InetSocketAddress("127.0.0.7", 4550);
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.socket().bind(address);
            socketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 1234));
            SelectorThread selectorThread = new SelectorThread(selector, socketChannel);
            selectorThread.start();


            System.out.println("Please login");
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()) {
                String commandLine = sc.nextLine();
                if (!selectorThread.isRunning) {
                    selector.close();
                    socketChannel.close();
                    return;
                }
                Matcher matcher = LOGIN_P.matcher(commandLine);
                if (matcher.find()) {
                    try {
                        socketChannel.register(selector, SelectionKey.OP_WRITE, ByteBuffer.wrap(commandLine.getBytes(StandardCharsets.UTF_8)));
                        selector.wakeup();
                        message.add(commandLine);
                        break;
                    } catch (ClosedChannelException e) {
                        e.printStackTrace();
                    }
                } else if (commandLine.equals("/quit")) {
                    System.out.println("you just quit");
                    selectorThread.isRunning = false;
                    selector.close();
                    socketChannel.close();
                    return;
                } else {
                    System.out.println("Invalid command.");
                }
            }
            while (sc.hasNextLine() && selectorThread.isRunning) {
                String commandLine = sc.nextLine();
                if (!selectorThread.isRunning) {
                    selector.close();
                    socketChannel.close();
                    return;
                }
                if (commandLine.length() == 0) {
                    message.add(commandLine);
                    commandLine = "Please input invalid message!";
                }
                Matcher historyMatch = HISTORY_P.matcher(commandLine);
                if (historyMatch.find()) {
                    if (historyMatch.group(6).equals("")) {
                        if (historyMatch.group(5).equals("")) {
                            if (historyMatch.group(3).equals("")) {
                                StringBuilder history = new StringBuilder();
                                message.add(commandLine);
                                if (message.size() >= 50) {
                                    for (int i = 0; i < 50; i++) {
                                        history.append(i + 1).append("\t").append(message.get(i)).append("\n");
                                    }
                                    System.out.println(history);
                                    continue;
                                } else {
                                    for (int i = 0; i < message.size(); i++) {
                                        history.append(i + 1).append("\t").append(message.get(i)).append("\n");
                                    }
                                    System.out.println(history);
                                    continue;
                                }
                            } else {
                                System.out.println("Invalid command");
                                continue;
                            }

                        } else {
                            try {
                                int startIndex = Integer.parseInt(historyMatch.group(3));
                                int countMax = Integer.parseInt(historyMatch.group(5));
                                message.add(commandLine);
                                StringBuilder history = new StringBuilder();
                                if (startIndex < message.size()) {
                                    if (startIndex + countMax < message.size()) {
                                        for (int i = startIndex - 1; i < startIndex + countMax; i++) {
                                            history.append(i + 1).append("\t").append(message.get(i)).append("\n");
                                        }
                                        System.out.println(history);
                                        continue;

                                    } else {
                                        for (int i = startIndex - 1; i < message.size(); i++) {
                                            history.append(i + 1).append("\t").append(message.get(i)).append("\n");
                                        }
                                        System.out.println(history);
                                        continue;
                                    }
                                } else {
                                    System.out.println("你输入的startIndex超出索引范围，请重新输入");
                                    continue;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid command.");
                            }
                        }
                    } else {
                        System.out.println("Incalid command.");
                        continue;
                    }
                }
                try {
                    socketChannel.register(selector, SelectionKey.OP_WRITE, ByteBuffer.wrap(commandLine.getBytes(StandardCharsets.UTF_8)));
                    selector.wakeup();
                    message.add(commandLine);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
