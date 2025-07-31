package com.AnJiahao.qqclient.service;

import com.AnJiahao.common.Message;
import com.AnJiahao.common.MessageType;
import com.AnJiahao.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 完成用户登录验证和用户注册等功能
 */


public class UserClientService {

    private User user = new User();
    private Socket socket;

    /*
    根据userId和pwd到服务器验证用户是否合法
     */
    public boolean checkUser(String userId, String pwd) throws Exception {
        //创建User对象
        user.setUserId(userId);
        user.setPwd(pwd);

        //连接到服务端，发送对象,创建socket对象。
        socket = new Socket(InetAddress.getByName("127.0.0.1"),9999);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        //发送对象
        oos.writeObject(user);

        //读取从服务器回复的Message对象
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Message o = (Message) ois.readObject();

        if (o.getMesType().equals(MessageType.MESSAGE_SUCCEED)){//验证成功

            //创建一个进程和服务端保持通信-->创建一个类ClientConnectServerThread
            ClientConnectServerThread ccst = new ClientConnectServerThread(socket);
            ccst.start();

            //将线程放入到一个集合中进行处理
            String curr = user.getUserId();
            ManageClientConnectServerThread.addClinetConnectServer(curr, ccst);
            System.out.println("用户："+curr+"线程已经注册！");

        }else {
            //登录失败，不启动与服务器通信的线程
            socket.close();
            return false;
        }
        return true;
    }

    /*
    向服务端请求在线用户列表
     */
    public void onlineFriendList()  {

        //向服务器端发送对应的MessageType类型
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getUserId());

        //输出流,发送给服务器
//        ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClinetConnectServer(user.getUserId());
//        Socket socket1 = clientConnectServerThread.getSocket();
//        ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    //得到当前线程的Socket对应的ObjectOutputStreaam(输出流)对象
                    //发送
                    ManageClientConnectServerThread.getClinetConnectServer(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    退出客户端，并给服务端发送一个退出系统的message对象
     */
    public void exitOut(){
        Message ms = new Message();
        ms.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        ms.setSender(user.getUserId());

        //定义输出流发送消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(ms);

            System.out.println(user.getUserId()+"  退出了系统！");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
