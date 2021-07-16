package com.djh.sever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ServerThread extends Thread {
    private Socket client = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String name;
    private ConcurrentHashMap<String, ServerThread> clientThread = null;
    private ArrayList<String> message = null;

    public ServerThread(Socket socket, ConcurrentHashMap<String, ServerThread> clientThread) throws IOException {
        try {
            this.client = socket;
            this.clientThread = clientThread;
            this.in = new BufferedReader(new InputStreamReader(
                    this.client.getInputStream()));
            this.out = new PrintWriter(this.client.getOutputStream(), true);
            this.message = new ArrayList<>();
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
                    message.add(info);
                    name = info.substring(7, info.length());
                    clientThread.put(name, this);
                    broadcast(name + " has logined", "");
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

                if (isQuit(info)) {
                    break;
                }
                if (isNull(info)) {
                    continue;
                }
                if (isPrivate(info)) {
                    continue;
                }
                if (isWho(info)) {
                    continue;
                }
                if (isPreSet(info)) {
                    continue;
                }
                if (isHistory(info)) {
                    continue;
                }
                out.println("你说：" + info);
                message.add(info);
                broadcast(name + "说：" + info, "");
//                broadcast(info, true);
                System.out.println("服务器端接收：" + "{'from_client':'" + client.getInetAddress().getHostName() +
                        "','data':'" + info + "'}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if ("Connection reset".equals(e.getMessage())) {
                if (name != null) {
                    clientThread.remove(name);
                    return;
                }
            }
        }
    }


    private void broadcast(String message, String names) {
        for (ConcurrentHashMap.Entry<String, ServerThread> entry : clientThread.entrySet()) {
            if (!name.equals(entry.getKey())) {
                if (!names.equals(entry.getKey())) {
                    entry.getValue().sendMessage(message);
                }
            }
//            if ((!names.equals(entry.getKey()))&&(!name.equals(entry.getKey())))
//                entry.getValue().sendMessage(message);
        }
    }

    private boolean isQuit(String info) {
        if (info.equals("/quit")) {
            if (name != null)
                clientThread.remove(name);
            message.clear();
            System.out.println(name + "quit");
            broadcast(name + " has quit", "");
            sendMessage("you has quit.");
            return true;
        }
        return false;
    }

    private boolean isNull(String info) {
        if ("Please input invalid message!".equals(info)) {
            sendMessage(info);
            return true;
        }
        return false;
    }

    private boolean isPrivate(String info) {
        String infoSpilt[] = info.split("\\s+");
        if ("/to".equals(infoSpilt[0])) {
            String nameFromInfo = infoSpilt[1];
            if (!clientThread.containsKey(nameFromInfo)) {
                sendMessage("user_name is not online.");
                message.add(info);
                return true;
            } else if (nameFromInfo.equals(name)) {
                sendMessage("Stop talking to yourself!");
                message.add(info);
                return true;
            }
            StringBuffer temp = new StringBuffer();
            for (int i = 2; i < infoSpilt.length; i++) {
                temp.append(" " + infoSpilt[i]);
            }
            sendMessage(nameFromInfo, name + " 对你说：" + temp.toString());
            sendMessage(name, "你对 " + nameFromInfo + "说：" + temp.toString());
            message.add(info);
            return true;
        } else {
            return false;
        }
    }

    private boolean isWho(String info) {
        String infoSpilt[] = info.split("\\s+");
        if ("/who".equals(infoSpilt[0])) {
            String usr = "用户名\n";
            for (String temp : clientThread.keySet()) {
                usr += temp + " \n";
            }
            usr += "Total online user: " + clientThread.size();
            sendMessage(usr);
            message.add(info);
            return true;
        } else {
            return false;
        }
    }

    private boolean isPreSet(String info) {
        String infoSpilt[] = info.split("\\s+");
        if (infoSpilt[0].length() >= 2) {
            if ("//".equals(infoSpilt[0].substring(0, 2))) {
                String infoPreSet = infoSpilt[0];
                if ("//hi".equals(infoPreSet)) {
                    if (infoSpilt.length >= 2) {
                        String nameFromInfo = infoSpilt[infoSpilt.length - 1];
                        if (!clientThread.containsKey(nameFromInfo)) {
                            sendMessage("user_name is not online.");
                            message.add(info);
                            return true;
                        } else if (name.equals(nameFromInfo)) {
                            sendMessage("You can't say hi to yourself!! Please stop talking to yourself!!");
                            message.add(info);
                            return true;
                        } else {
                            sendMessage("你向" + nameFromInfo + "打招呼：“Hi，你好啊~~”");
                            sendMessage(nameFromInfo, name + "向你打招呼：“Hi，你好啊~”");
                            broadcast(name + "向" + nameFromInfo + "打招呼：“Hi，你好啊~”", nameFromInfo);
                            message.add(info);
                            return true;
                        }
                    } else {
                        sendMessage("你向大家打招呼，“Hi，大家好！我来咯~”");
                        broadcast(name + "向大家打招呼，“Hi，大家好！我来咯~”", "");
                        message.add(info);
                        return true;
                    }
                } else if ("//smile".equals(infoPreSet)) {
                    if (infoSpilt.length >= 2) {
                        String nameFromInfo = infoSpilt[infoSpilt.length - 1];
                        if (!clientThread.containsKey(nameFromInfo)) {
                            sendMessage("user_name is not online.");
                            message.add(info);
                            return true;
                        } else if (name.equals(nameFromInfo)) {
                            sendMessage("You can't smile to yourself!! Please stop talking to yourself!!");
                            message.add(info);
                            return true;
                        } else {
                            sendMessage(name, "你向" + nameFromInfo + "露出了天真无邪的笑容。");
                            sendMessage(nameFromInfo, name + "向你露出了天真无邪的笑容。");
                            broadcast(name + "向" + nameFromInfo + "露出了天真无邪的笑容，大家快来围观！", nameFromInfo);
                            message.add(info);
                            return true;
                        }

                    } else {
                        sendMessage("你向大家打招呼，脸上露出天真无邪的笑容");
                        broadcast("大家好，我是" + name + "（脸上带着天真无邪的笑容，这家伙不像是好人，大家不要相信他！）", "");
                        message.add(info);
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isHistory(String info) {
        String infoSpilt[] = info.split("\\s+");
        if ("/history".equals(infoSpilt[0])) {
            String history = "";
            if (infoSpilt.length >= 2) {
                int startIndex = Integer.parseInt(infoSpilt[1]);
                int maxCount = Integer.parseInt(infoSpilt[2]);
                message.add(info);
                if (startIndex < message.size()) {
                    if (startIndex + maxCount < message.size()) {
                        for (int i = startIndex - 1; i < startIndex + maxCount; i++) {
                            history += i + 1 + "\t" + message.get(i) + "\n";
                        }
                        sendMessage(history);
                        return true;
                    } else {
                        for (int i = startIndex - 1; i < message.size(); i++) {
                            history += i + 1 + "\t" + message.get(i) + "\n";
                        }
                        sendMessage(history);
                        return true;
                    }

                } else {
                    sendMessage("你输入的startIndex超出索引范围，请重新输入");
                    return true;
                }
            } else {
                message.add(info);
                if (message.size() >= 50) {
                    for (int i = 0; i < 50; i++) {
                        history += i + 1 + "\t" + message.get(i) + "\n";
                    }
                    sendMessage(history);
                    return true;
                } else {
                    for (int i = 0; i < message.size(); i++) {
                        history += i + 1 + "\t" + message.get(i) + "\n";
                    }
                    sendMessage(history);
                    return true;
                }
            }
        } else {
            return false;
        }

    }

    private void sendMessage(String message) {
        out.println(message);
    }

    private void sendMessage(String name, String message) {
        clientThread.get(name).out.println(message);
    }

}