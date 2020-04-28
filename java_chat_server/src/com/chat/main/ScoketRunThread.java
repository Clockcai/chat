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
			// �Ƚ��տ�ʼ��������User����
			String userJson = br.readLine();
			User user = JsonUtils.jsonToPojo(userJson, User.class);
			user.setOsw(osw);
//			System.out.println(user);
			// ����ӹ�osw��User������ӵ������û��б���
			ServerData.onlineUsers.add(user);

			while (true) {
				// �����������͵Ĳ���
				String chatType = br.readLine();
				switch (chatType) {
				case "1":
					for (User u : ServerData.onlineUsers) {
						String name = u.getName();
						osw.write(name + "\n");
						osw.flush();
					}
					while (true) {
						// �˷��������ԣ���ΪonlineUser�а���osw���󣬲���ת��
//						String usersJson = JsonUtils.objectToJson(ServerData.onlineUsers);
//						osw.write(usersJson + "\n");
//						osw.flush();
						String msgJson1 = br.readLine();
						Messages msg1 = JsonUtils.jsonToPojo(msgJson1, Messages.class);
						if ("exit".equals(msg1.getContent())) {
							break;
						}
						// ��msg������Ϣ�����У�ÿ���һ���Ͱ���Ϣ�̻߳���
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
						// ��msg������Ϣ�����У�ÿ���һ���Ͱ���Ϣ�̻߳���
						ServerData.mList.add(msg2);
						synchronized (ServerData.mList) {
							ServerData.mList.notify();
						}
					}
					break;
				default:
					break;
				}
				// ��ȡmsg��תΪ����

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
