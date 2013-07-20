package org.friendscentral.steamnet.BaseClasses;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Comment implements Serializable {
	int userId;
	String content;
	String username;
	
	public Comment(int u, String c, String un) {
		userId = u;
		content = c;
		username = un;
	}
	
	public void setUserId(int u) {
		userId = u;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUsername(String u) {
		username = u;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setContent(String c) {
		content = c;
	}
	
	public String getContent() {
		return content;
	}
}
