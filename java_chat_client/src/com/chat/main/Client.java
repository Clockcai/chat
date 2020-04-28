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
			System.out.print("请输入聊天昵称");
			String name = sc.nextLine();
			// 生成唯一的用户的User对象
			User user = new User();
			user.setName(name);
			user.setId((100000 + ((int) Math.random() * 900000)) + "");
			osw.write(JsonUtils.objectToJson(user) + "\n");
			osw.flush();
			while (true) {
				System.out.println("请输入聊天方式");
				System.out.println("1、私聊");
				System.out.println("2、群聊");
				System.out.println(">");
				// 切换聊天模式
				String chatType = sc.nextLine();
				// 将选择类型发送给服务器
				osw.write(chatType + "\n");
				osw.flush();
				// 由于传入osw对象的User对象或者Messages对象无法反序列化成对应的实体类
				// 因此先将User发送出去，到接收方再进行添加osw参数
				switch (chatType) {
				case "1":
					System.out.println("请输入要收信人姓名");
					String reciveName = sc.nextLine();
					User targetUser = new User();
					targetUser.setName(reciveName);
					while (true) {
//						String usersJson = br.readLine();
//						List<User> onlineUsers = JsonUtils.jsonToList(usersJson, User.class);
//						System.out.println("在线用户有");
//						for (User u : onlineUsers) {
//							System.out.println(u.getName());
//						}
						// 将发送信息打包成Messages对象
						System.out.println("请输入要发送的内容");
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
						// 讲发送信息打包成Messages对象
						Messages msg2 = new Messages();
						msg2.setContent(content2);
						msg2.setDate(new Date());
						msg2.setUser(user);
						// 将msg对象打包成字符串对象
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
