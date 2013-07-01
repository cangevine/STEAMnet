package org.friendscentral.steamnet.BaseClasses;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Comment implements Serializable {
	int userId;
	String content;
	
	public Comment(int u, String c) {
		userId = u;
		content = c;
	}
	
	public void setUser(int u) {
		userId = u;
	}
	
	public int getUser() {
		return userId;
	}
	
	public void setContent(String c) {
		content = c;
	}
	
	public String getContent() {
		return content;
	}
}
