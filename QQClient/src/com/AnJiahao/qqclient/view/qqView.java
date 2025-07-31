package com.AnJiahao.qqclient.view;

import com.AnJiahao.qqclient.service.FileClientService;
import com.AnJiahao.qqclient.service.MessageClientService;
import com.AnJiahao.qqclient.service.UserClientService;

import java.io.IOException;
import java.util.Scanner;

public class qqView {

    private boolean loop = true;
    private int key;
    private UserClientService userClientService = new UserClientService();//用于登录服务器和注册用户
    private MessageClientService messageClientService = new MessageClientService();//对象用户发送消息
    private FileClientService fileClientService = new FileClientService();//用于发送文件

    public static void main(String[] args) throws Exception {

        new qqView().mainMenu();
    }

    public void mainMenu() throws Exception {
        while (loop){
            System.out.println("===============登录界面=============");
            System.out.println("================1 登录============");
            System.out.println("================9 退出=============");

            System.out.print("请选择选项：");
            Scanner scanner = new Scanner(System.in);
            key = scanner.nextInt();
            switch (key){
                case 1:
                    //完成登录界面编写
                    System.out.print("输入用户名：");
                    String userId = scanner.next();
                    System.out.print("输入密码：");
                    String pwd = scanner.next();

                    //服务端验证用户名密码是否存在

                    if (userClientService.checkUser(userId,pwd)){
                        /*
                        这里出现过重大错误，
                        源代码为：new qqView.Meun(userId)
                        这会直接导致在二级菜单中进行的操作出现指针指空(null),而无法进行菜单中的任意操作
                        因此不能通过new来创建二级菜单，应该直接使用Meun()。
                         */
                        Meun(userId);
                    }else {
                        //登录失败
                        /*-----*/
                        System.out.println("登录失败");
                    }

                    break;
                case 9:
                    loop = false;
                    break;
                default:
                    System.out.println("输入有误，请重新输入！");
                    break;
            }
        }
    }

    public boolean Meun(String userId) throws IOException {
        int Num;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("===============二级菜单用户("+ userId +")=============");
            System.out.println("===============1 显示在线用户列表=============");
            System.out.println("===============2 群发消息=============");
            System.out.println("===============3 私聊消息=============");
            System.out.println("===============4 发送文件=============");
            System.out.println("===============9 退出系统=============");
            System.out.print("输入你的选择：");
            Num = scanner.nextInt();
            switch (Num) {
                case 1:
                    /*
                      显示用户在线列表
                     */
                    userClientService.onlineFriendList();

                    break;
                case 2:
                    /*
                      群发消息
                     */
                    System.out.println("请输入需要群发的消息:");
                    String cont = scanner.next();
                    messageClientService.sendMessageToAll(cont,userId);
                    break;
                case 3:
                    /*
                      私聊消息
                     */
                    System.out.print("选择私聊对象：");
                    String getter = scanner.next();
                    System.out.println("请输入聊天内容：");
                    String content = scanner.next();
                    messageClientService.sendMessageToOne(content,userId,getter);
                    break;
                case 4:
                    /*
                      发送文件
                     */
                    System.out.print("选择文件接收的用户：");
                    String gets = scanner.next();
                    System.out.print("选择文件完整路径(形式 d:\\xx.jpg):");
                    String src = scanner.next();
                    System.out.print("选择对方接收路径(形式 d:\\yy.jpg)");
                    String dest = scanner.next();
                    fileClientService.sendFileToOne(userId, gets, src, dest);
                    break;
                case 9:
                    /*
                      退出系统,并结束客户端
                     */
                    userClientService.exitOut();
                    return false;
            }
        }

    }
}
