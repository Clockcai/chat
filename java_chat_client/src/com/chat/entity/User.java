package com.chat.entity;

import java.io.OutputStreamWriter;

public class User {
	private String id;
	private String name;
	private OutputStreamWriter osw;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public OutputStreamWriter getOsw() {
		return osw;
	}
	public void setOsw(OutputStreamWriter osw) {
		this.osw = osw;
	}
	public User() {
		super();
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", osw=" + osw + "]";
	}
	
	
}
