package com.AnJiahao.qqservice.service;

import com.AnJiahao.common.Message;
import com.AnJiahao.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class sendNewToAllService implements Runnable{
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while (true) {
            System.out.println("输入需要推送的内容[输入exit表示退出推送]：");
            String news = scanner.next();

            if ("exit".equals(news)){
                break;
            }

            //构建消息
            Message ms = new Message();
            ms.setSender("服务器");
            ms.setContent(news);
            ms.setMesType(MessageType.MESSAGE_COMM_ALL_MES);
            ms.setSendTime(new java.util.Date().toString());
            System.out.println("服务器推送消息 说：" + news);

            //推送消息
            HashMap<String, ServiceConnectClientThread> hm = ManageClientThread.getHm();
            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                String onLineUser = iterator.next().toString();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(
                            hm.get(onLineUser).getSocket().getOutputStream());
                    oos.writeObject(ms);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
