package com.chat.data;

import java.util.LinkedList;
import java.util.List;

import com.chat.entity.Messages;
import com.chat.entity.User;

public class ServerData {
	public static List<Messages> mList = new LinkedList<Messages>();
	public static List<User> onlineUsers = new LinkedList<User>();
}
