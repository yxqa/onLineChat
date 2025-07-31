package com.AnJiahao.qqclient.service;

import java.util.HashMap;

public class ManageClientConnectServerThread {

    //将多个线程放入hashmap，key就是用户id，value就是线程
    private static HashMap<String,ClientConnectServerThread> hm = new HashMap<>();

    //将线程加入集合
    public static void addClinetConnectServer(String userId,ClientConnectServerThread clientConnectServerThread){

        hm.put(userId,clientConnectServerThread);
    }

    //通过·userId获得对应线程
    public static ClientConnectServerThread getClinetConnectServer(String userId){
        return hm.get(userId);
    }
}
