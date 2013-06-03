package BaseClasses;

import java.io.Serializable;

/**
 * 
 * @author sambeckley 
 * 
 */

@SuppressWarnings("serial")
public class Spark implements Serializable{
	int id;
	char sparkType;
	char contentType;
	String content;
	String createdAt;
	
	/*
	 * Need to add users/tags, but don't know how yet
	 * Don't need updated_at
	 * Should Sparks have a name/caption type field?
	 * 
	 * Just setting everything in the creator, since after we grab it you should never need to edit it
	 */
	
	/**
	 * @param (id, char, char, String, String)
	 * @param int - id
	 * @param char - sparkType
	 * @param char - contentType
	 * @param String - content
	 * @param String - createdAt
	 */
	public Spark(int i, char st, char ct, String c, String ca) {
		id = i;
		sparkType = st;
		contentType = ct;
		content = c;
		createdAt = ca;
	}
	/**
	 * (int, char, char, String)
	 * @param int - id
	 * @param char - sparkType
	 * @param char - contentType
	 * @param String - content
	 */
	public Spark(int i, char st, char ct, String c) {
		id = i;
		sparkType = st;
		contentType = ct;
		content = c;
	}
	/**
	 * 
	 * @param char - sparkType
	 * @param char - contentType
	 * @param String - content
	 */
	public Spark(char st, char ct, String c){
		sparkType = st;
		contentType = ct;
		content = c;
	}
	
	/**
	 * id = -1, sparkType/contentType/content = " "
	 */
	
	public Spark(){
		id = -1;
		sparkType = " ".charAt(0);
		contentType = " ".charAt(0);
		content = " ";
	}
	
	public int getId() {
		return id;
	}
	
	public char getSparkType() {
		return sparkType;
	}
	
	public String getSparkTypeString(){
		return String.valueOf(sparkType);
	}
	
	public char getContentType() {
		return contentType;
	}
	
	public String getContentTypeString(){
		return String.valueOf(contentType);
	}
	
	public String getContent() {
		return content;
	}
	
	public String getCreatedAt(){
		if(createdAt != null){
			return createdAt;
		} else {
			return null;
		}
	}
}
