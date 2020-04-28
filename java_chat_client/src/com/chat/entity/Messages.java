package com.chat.entity;

import java.util.Date;

public class Messages {
	private String content;
	private Date date;
	private User user;
	private User targetUser;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(User targetUser) {
		this.targetUser = targetUser;
	}
	public Messages() {
		super();
	}
	@Override
	public String toString() {
		return "Messages [content=" + content + ", date=" + date + ", user=" + user + ", targetUser=" + targetUser
				+ "]";
	}
	
	
}
