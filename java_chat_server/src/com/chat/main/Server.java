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
			// ������Ϣת������
			new TransMessageThread().start();
			while(true) {
				System.out.println("�������ȴ�����...");
				Socket socket = server.accept();
				System.out.println("��"+ i+"���ͻ������ӳɹ�");
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
