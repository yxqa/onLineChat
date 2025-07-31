package com.AnJiahao.qqservice.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 该类用于管理和客户端通信的线程
 */

public class ManageClientThread {
    private static HashMap<String,ServiceConnectClientThread> hm = new HashMap<>();

    public static HashMap<String, ServiceConnectClientThread> getHm() {
        return hm;
    }

    //添加线程到hm
    public static void addClientThread(String userId,ServiceConnectClientThread serviceConnectClientThread){
        hm.put(userId,serviceConnectClientThread);

    }

    //根据userId返回线程
    public static ServiceConnectClientThread getClientThread(String userId){
        return hm.get(userId);
    }

    //从集合中删除对象
    public static void removeClientThread(String userId){
        hm.remove(userId);
    }

    //返回在线用户列表
    public static String getOnlineUser(){
        //遍历HashMap,使用迭代器
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()){
            onlineUserList += iterator.next().toString() + " ";
        }
        return onlineUserList;

    }
}
