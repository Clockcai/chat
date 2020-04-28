package com.chat.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

import com.chat.entity.Messages;
import com.chat.entity.User;
import com.chat.util.JsonUtils;

public class Client {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		OutputStream os = null;
		OutputStreamWriter osw = null;
		Socket socket = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			socket = new Socket("127.0.0.1", 9999);
			new ReaderThread(socket).start();
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os, "utf-8");
			is = socket.getInputStream();
			isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);
			System.out.print("�����������ǳ�");
			String name = sc.nextLine();
			// ����Ψһ���û���User����
			User user = new User();
			user.setName(name);
			user.setId((100000 + ((int) Math.random() * 900000)) + "");
			osw.write(JsonUtils.objectToJson(user) + "\n");
			osw.flush();
			while (true) {
				System.out.println("���������췽ʽ");
				System.out.println("1��˽��");
				System.out.println("2��Ⱥ��");
				System.out.println(">");
				// �л�����ģʽ
				String chatType = sc.nextLine();
				// ��ѡ�����ͷ��͸�������
				osw.write(chatType + "\n");
				osw.flush();
				// ���ڴ���osw�����User�������Messages�����޷������л��ɶ�Ӧ��ʵ����
				// ����Ƚ�User���ͳ�ȥ�������շ��ٽ������osw����
				switch (chatType) {
				case "1":
					System.out.println("������Ҫ����������");
					String reciveName = sc.nextLine();
					User targetUser = new User();
					targetUser.setName(reciveName);
					while (true) {
//						String usersJson = br.readLine();
//						List<User> onlineUsers = JsonUtils.jsonToList(usersJson, User.class);
//						System.out.println("�����û���");
//						for (User u : onlineUsers) {
//							System.out.println(u.getName());
//						}
						// ��������Ϣ�����Messages����
						System.out.println("������Ҫ���͵�����");
						String content1 = sc.nextLine();
						Messages msg1 = new Messages();
						msg1.setContent(content1);
						msg1.setDate(new Date());
						msg1.setUser(user);
						msg1.setTargetUser(targetUser);
						String msgJson1 = JsonUtils.objectToJson(msg1);
						osw.write(msgJson1 + "\n");
						osw.flush();
						if ("exit".equals(msg1.getContent())) {
							break;
						}
					}

					break;
				case "2":
					while (true) {
						System.out.println(">");
						String content2 = sc.nextLine();
						// ��������Ϣ�����Messages����
						Messages msg2 = new Messages();
						msg2.setContent(content2);
						msg2.setDate(new Date());
						msg2.setUser(user);
						// ��msg���������ַ�������
						String msgJson2 = JsonUtils.objectToJson(msg2);
						osw.write(msgJson2 + "\n");
						osw.flush();
						if ("exit".equals(msg2.getContent())) {
							break;
						}
					}
					break;
				default:
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					osw.close();
					os.close();
					br.close();
					isr.close();
					is.close();
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
