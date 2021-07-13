package com.djh.sever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ServerThread extends Thread {
    private Socket client = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String name;
    private ConcurrentHashMap<String, ServerThread> clientThread = null;

    public ServerThread(Socket socket, ConcurrentHashMap<String, ServerThread> clientThread) throws IOException {
        try {
            this.client = socket;
            this.clientThread = clientThread;
            this.in = new BufferedReader(new InputStreamReader(
                    this.client.getInputStream()));
            this.out = new PrintWriter(this.client.getOutputStream(), true);
        } catch (Exception e) {

        }

    }

    @Override
    public void run() {
        try {
            login();
            talk();
        } catch (Exception e) {
        } finally {
            //关闭资源
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void login() {
        String info = null;
        try {
            while (true) {
                info = in.readLine();
                System.out.println("服务器端接收：" + "{'from_client':'" + client.getInetAddress().getHostName() +
                        "','data':'" + info + "'}");
//                if (info.equals("/quit")) {
//                    out.println("you just quit");
//                    client.close();
//                    break;
//                }
                if (clientThread.containsKey(info.substring(7, info.length()))) {
                    out.println("Name exist, please choose another name.");
                } else {
                    out.println("You have logined");
                    name = info.substring(7, info.length());
                    clientThread.put(name, this);
                    broadcast(name + " has logined", false);
                    break;
                }
            }

        } catch (Exception e) {
        }
    }

    public void talk() throws IOException {
        String info;
        try {
            while (true) {
                info = in.readLine();
//                if (info.equals("/quit")) {
//                    if (name != null)
//                        clientThread.remove(name);
//                    System.out.println(name + "quit");
//                    broadcast(name + " has quit");
////                    Enumeration<String> ee = clientThread.keys();
////                    String usr = "";
////                    System.out.println("当前在线用户：");
////                    while (ee.hasMoreElements()) {
////                        usr += ee.nextElement() + "\n";
////                    }
////                    System.out.println(usr);
//
////                    System.out.println();
//                    out.println("you has quit.");
//                    break;
//                }
                if (isQuit(info)) {
                    break;
                }
                if (isPrivate(info)) {
                    continue;
                }
                out.println("你说：" + info);
                broadcast(info, true);
                System.out.println("服务器端接收：" + "{'from_client':'" + client.getInetAddress().getHostName() +
                        "','data':'" + info + "'}");
            }
        } catch (Exception e) {
        }
    }


    public void broadcast(String message, boolean isTalking) {
        if (isTalking) {
            for (ConcurrentHashMap.Entry<String, ServerThread> entry : clientThread.entrySet()) {
                if (!name.equals(entry.getKey()))
                    entry.getValue().sendMessage(name + "说：" + message);
            }
        } else {
            for (ConcurrentHashMap.Entry<String, ServerThread> entry : clientThread.entrySet()) {
                if (!name.equals(entry.getKey()))
                    entry.getValue().sendMessage(message);
            }
        }

    }

    public boolean isQuit(String info) {
        if (info.equals("/quit")) {
            if (name != null)
                clientThread.remove(name);
            System.out.println(name + "quit");
            broadcast(name + " has quit", false);
            out.println("you has quit.");
            return true;
//            break;
        }
        return false;
    }

    public boolean isPrivate(String info) {
        if ("/to ".equals(info.substring(0, 4))) {
            String infoSpilt[] = info.split("\\s+");
            String nameFromInfo = infoSpilt[1];
            if (!clientThread.containsKey(nameFromInfo)) {
                sendMessage("user_name is not online.");
                return true;
            } else if (nameFromInfo.equals(name)) {
                sendMessage("Stop talking to yourself!");
                return true;
            }
            StringBuffer temp = new StringBuffer();
            for (int i = 2; i < infoSpilt.length; i++) {
                temp.append(" " + infoSpilt[i]);
            }
            sendMessage(nameFromInfo, name + " 对你说：" + temp.toString());
            sendMessage(name, "你对 " + nameFromInfo + "说：" + temp.toString());
            return true;
        } else {
            return false;
        }
    }


    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendMessage(String name, String message) {
        clientThread.get(name).out.println(message);
    }


}