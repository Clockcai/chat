package com.chat.main;

import java.io.IOException;

import com.chat.data.ServerData;
import com.chat.entity.Messages;
import com.chat.entity.User;
import com.chat.util.JsonUtils;

public class TransMessageThread extends Thread {
	@Override
	public void run() {

		while (true) {
			if (ServerData.mList.size() > 0) {
				Messages msg = ServerData.mList.remove(0);
				if (msg.getTargetUser() != null) {
					for(User user:ServerData.onlineUsers) {
						if(msg.getTargetUser().getName().equals(user.getName())) {
							try {
								user.getOsw().write(JsonUtils.objectToJson(msg) + "\n");
								user.getOsw().flush();
							} catch (IOException e) {
								// �������ʧ����˵�����û������ߣ���������û���ɾ�����û�
								ServerData.onlineUsers.remove(user);
							}
							
						}
					}
				} else {
					for (User user : ServerData.onlineUsers) {
						if (msg.getUser().getName().equals(user.getName())) {
							continue;
						} else {
							try {
								// ����Ӧ����Ϣ���͸�Ⱥ�ĵ�������Ա
								user.getOsw().write(JsonUtils.objectToJson(msg) + "\n");
								user.getOsw().flush();
							} catch (IOException e) {
								// �������ʧ����˵�����û������ߣ���������û���ɾ�����û�
								ServerData.onlineUsers.remove(user);
							}
						}

					}
				}
			} else {
				// �����Ϣ���еĳ���Ϊ������еȴ�
				synchronized (ServerData.mList) {
					try {
						ServerData.mList.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
}
