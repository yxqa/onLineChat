package com.AnJiahao.qqservice.service;

import com.AnJiahao.common.Message;
import com.AnJiahao.common.MessageType;
import com.AnJiahao.common.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器端，监听端口9999，等待客户端的连接，并保持通信
 */

public class QQServer {
    private ServerSocket ss = null;

    //用集合存放多个用户
    private static ConcurrentHashMap<String,User> validUser = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ArrayList<Message>> offLinedb = new ConcurrentHashMap<>();//存放离线消息和文件

    static {
        validUser.put("100",new User("100","123456"));
        validUser.put("101",new User("101","123456"));
        validUser.put("102",new User("102","123456"));
        validUser.put("老杨",new User("老杨","123456"));
    }


    public static void main(String[] args) {
        new QQServer();
    }

    /*
    验证用户账户密码是否存在并正确
     */
    private boolean checkUser(String userId, String pwd){

        User user = validUser.get(userId);
        if (user != null){
            if (user.getPwd().equals(pwd)){
                return true;
            }
        }
        return false;
    }

    public QQServer(){
        try {
            System.out.println("服务端在9999端口监听...");
            new Thread(new sendNewToAllService()).start();
            ss = new ServerSocket(9999);
            while (true){
                Socket socket = ss.accept();

                //得到socket关联的对象输入流(获取信息)
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User u = (User) ois.readObject();

                //得到socket关联的对象输出流(回复信息)
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                //创建Message对象，准备回复客户端
                Message ms = new Message();

                //验证
                if (checkUser(u.getUserId(), u.getPwd())){
                    ms.setMesType(MessageType.MESSAGE_SUCCEED);
                    //将MessageType消息返回客户端
                    oos.writeObject(ms);

                    //创建线程和客户端保持联系，并要持有socket对象
                    ServiceConnectClientThread serviceConnectClientThread = new ServiceConnectClientThread(socket,u.getUserId());

                    //启动线程
                    serviceConnectClientThread.start();

                    //将线程加入集合中管理
                    ManageClientThread.addClientThread(u.getUserId(),serviceConnectClientThread);

                }else {
                    System.out.println("用户Id"+u.getUserId()+"验证失败！");
                    ms.setMesType(MessageType.MESSAGE_FAIL);
                    oos.writeObject(ms);
                    socket.close();
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {

            //如果服务器退出了while，表示服务器端不再监听，关闭ServerSocket
            try {
                ss.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
