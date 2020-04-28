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
								// 如果发送失败则说明该用户不在线，则从在线用户中删除该用户
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
								// 将对应的消息发送给群聊的其他成员
								user.getOsw().write(JsonUtils.objectToJson(msg) + "\n");
								user.getOsw().flush();
							} catch (IOException e) {
								// 如果发送失败则说明该用户不在线，则从在线用户中删除该用户
								ServerData.onlineUsers.remove(user);
							}
						}

					}
				}
			} else {
				// 如果消息队列的长度为零则进行等待
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
