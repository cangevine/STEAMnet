package org.friendscentral.steamnet;


public class Spark {
	String id;
	char spark_type;
	char content_type;
	String content;
	String createdAt;
	String updatedAt;
	
	String[] tags;
	String user;
	
	public Spark(char s, String u) {
		spark_type = s;
		
		//No idea how to do user stuff
		user = u;
	}
	
	public void setContentType(char c) {
		content_type = c;
	}
	
	public void setContent(String c, String[] t) {
		content = c;
		tags = t;
	}
	
	public char getSparkType() {
		return spark_type;
	}
	
	public char getContentType() {
		return content_type;
	}
	
	public String getContent() {
		return content;
	}
	
	public String[] getTags() {
		return tags;
	}
	
	public String getUser() {
		return user;
	}
}
