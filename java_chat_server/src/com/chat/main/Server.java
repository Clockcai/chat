package com.chat.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			server = new ServerSocket(9999);
			int i = 1;
			// 开启消息转发进程
			new TransMessageThread().start();
			while(true) {
				System.out.println("服务器等待连接...");
				Socket socket = server.accept();
				System.out.println("第"+ i+"个客户端连接成功");
				new ScoketRunThread(socket).start();
				i++;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(server!=null) {
					server.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
