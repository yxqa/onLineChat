package com.AnJiahao.qqclient.service;

import com.AnJiahao.common.Message;
import com.AnJiahao.common.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.SQLOutput;

public class ClientConnectServerThread extends Thread{

    //必须要持有Socket用来保持连接
    private Socket socket;
    public ClientConnectServerThread(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //因为Thread需要在后台和服务器通讯，所以要一直启动
        while (true){
            System.out.println("客户端线程，等待读取从服务端发送的信息！");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message ms =(Message) ois.readObject();

                if (ms.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    String[] onlineUser = ms.getContent().split(" ");
                    System.out.println("----------在线用户列表如下---------");
                    for (int i = 0 ;i <onlineUser.length;i++){
                        System.out.println("用户名：" + onlineUser[i]);
                    }
                }else if(ms.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    //显示信息
                    System.out.println("\n"+"用户"+ ms.getSender() +"给你发送了一条消息：\n"+ ms.getContent());
                } else if (ms.getMesType().equals(MessageType.MESSAGE_COMM_ALL_MES)) {
                    //显示信息
                    System.out.println("\n" + ms.getSender() + "对大家说： " + ms.getContent());

                } else if (ms.getMesType().equals(MessageType.MESSAGE_COMM_FILE_MES)) {
                    //去除Message的文件字节数组，通过文件输出流写到磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(ms.getDest());
                    fileOutputStream.write(ms.getFileBytes());
                    fileOutputStream.close();

                    //文件信息
                    System.out.println("\n已接受来自用户 " + ms.getSender() + " 的文件");
                    System.out.println("该文件存放于本机的：" + ms.getDest() + " 下");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
