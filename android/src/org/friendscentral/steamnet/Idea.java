package org.friendscentral.steamnet;

public class Idea extends Jawn {
	String id;
	String title;
	String content;
	String createdAt;
	String updatedAt;
	
	int[] sparkIds;
	
	String[] tags;
	String user;

	public Idea(int[] s, String u) {
		sparkIds = s;
		
		//No idea how to do user stuff
		user = u;
	}
	
	public void setContent(String c, String[] t) {
		content = c;
		tags = t;
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
	
	public String getDate() {
		/*
		 * Some math to see if the createdAt or updatedAt was more recent
		 */
		return createdAt;
	}
	
	public int[] getSparkArray() {
		return sparkIds;
	}
	
	@Override
	public char getType() {
		return "I".charAt(0);
	}

}
