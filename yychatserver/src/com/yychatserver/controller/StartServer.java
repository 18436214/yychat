package com.yychatserver.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.yychat.model.Message;
import com.yychat.model.User;

public class StartServer {
	public static HashMap hmSocket=new HashMap<String,Socket>();
	
	ServerSocket ss;
	Socket s;
	String userName;
	String passWord;
	public StartServer() {
		try {
			ss=new ServerSocket(3456);
			System.out.println("服务器已经启动，监听3456端口");
			while(true) {
				s=ss.accept();
				System.out.println("连接成功:"+s);
				
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				User user=(User)ois.readObject();
				userName=user.getUserName();
				passWord=user.getPassWord();
				System.out.println(userName);
				System.out.println(passWord);
				
				Message mess=new Message();
				mess.setSender("Server");
				mess.setReceiver("userName");
				if(passWord.equals("123456")) {
					mess.setMessageType(Message.message_LoginSuccess);
				}else {
					mess.setMessageType(Message.message_LoginFailure);
				}
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(mess);
				if(passWord.equals("123456")) {
					hmSocket.put(userName, s);
					new ServerReceiverThread(s).start();
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
