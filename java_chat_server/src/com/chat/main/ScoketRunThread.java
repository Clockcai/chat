package com.chat.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.chat.data.ServerData;
import com.chat.entity.Messages;
import com.chat.entity.User;
import com.chat.util.JsonUtils;

public class ScoketRunThread extends Thread {

	private Socket socket;

	public ScoketRunThread(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		OutputStream os = null;
		OutputStreamWriter osw = null;
		try {

			is = socket.getInputStream();
			isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os, "utf-8");
			// 先接收开始传过来的User对象
			String userJson = br.readLine();
			User user = JsonUtils.jsonToPojo(userJson, User.class);
			user.setOsw(osw);
//			System.out.println(user);
			// 将添加过osw的User对象添加到在线用户列表中
			ServerData.onlineUsers.add(user);

			while (true) {
				// 接受聊天类型的参数
				String chatType = br.readLine();
				switch (chatType) {
				case "1":
					for (User u : ServerData.onlineUsers) {
						String name = u.getName();
						osw.write(name + "\n");
						osw.flush();
					}
					while (true) {
						// 此方法不可以，因为onlineUser中包含osw对象，不可转换
//						String usersJson = JsonUtils.objectToJson(ServerData.onlineUsers);
//						osw.write(usersJson + "\n");
//						osw.flush();
						String msgJson1 = br.readLine();
						Messages msg1 = JsonUtils.jsonToPojo(msgJson1, Messages.class);
						if ("exit".equals(msg1.getContent())) {
							break;
						}
						// 将msg存入消息队列中，每存进一个就把消息线程唤醒
						ServerData.mList.add(msg1);
						synchronized (ServerData.mList) {
							ServerData.mList.notify();
						}
					}

					break;
				case "2":
					while (true) {
						String msgJson2 = br.readLine();
						System.out.println(msgJson2);
						Messages msg2 = JsonUtils.jsonToPojo(msgJson2, Messages.class);
						System.out.println(msg2);
						if ("exit".equals(msg2.getContent())) {
							break;
						}
						// 将msg存入消息队列中，每存进一个就把消息线程唤醒
						ServerData.mList.add(msg2);
						synchronized (ServerData.mList) {
							ServerData.mList.notify();
						}
					}
					break;
				default:
					break;
				}
				// 读取msg并转为对象

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
