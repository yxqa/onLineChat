package com.AnJiahao.qqservice.service;

import com.AnJiahao.common.Message;
import com.AnJiahao.common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 该类的一个对象和某个客户端保持通信
 */

public class ServiceConnectClientThread extends Thread{

    private Socket socket;
    private String userId;

    public ServiceConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("服务端与客户端保持通讯,读取数据...");
                //读取数据(输入流)
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //判断ms的类型来进行对应的业务逻辑操作
                Message ms = (Message) ois.readObject();

                if (ms.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){

                    System.out.println(ms.getSender()+"在申请查看在线列表！");
                    String onlineUser = ManageClientThread.getOnlineUser();
                    Message message = new Message();
                    message.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message.setContent(onlineUser);
                    message.setGetter(ms.getSender());
                    //返回消息，定义输出流out
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                }else if (ms.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){

                    //从集合中删除客户端对应的线程
                    ManageClientThread.removeClientThread(ms.getSender());
                    System.out.println(ms.getSender()+" :退出系统!");

                    //缺一不可
                    socket.close();//关闭连接
                    break;//退出线程
                } else if (ms.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {

                    //根据ms获取getter，然后得到对应线程
                    ServiceConnectClientThread clientThread = ManageClientThread.getClientThread(ms.getGetter());


                    //得到对应socket输出流，将message对象发送给指定客户端
                    ObjectOutputStream oos = new ObjectOutputStream(clientThread.getSocket().getOutputStream());
                    oos.writeObject(ms);//转发
                }else if (ms.getMesType().equals(MessageType.MESSAGE_COMM_ALL_MES)){
                    //遍历线程集合，对每一个线程集合都发送一遍(输出流)
                    HashMap<String, ServiceConnectClientThread> hm = ManageClientThread.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    //遍历
                    while (iterator.hasNext()){
                        String onLine = iterator.next().toString();
                        if (!onLine.equals(ms.getSender())){
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get(onLine).getSocket().getOutputStream());
                            oos.writeObject(ms);
                        }
                    }
                } else if (ms.getMesType().equals(MessageType.MESSAGE_COMM_FILE_MES)) {
                   ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.
                           getClientThread(ms.getGetter()).getSocket().getOutputStream());
                   oos.writeObject(ms);

                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
