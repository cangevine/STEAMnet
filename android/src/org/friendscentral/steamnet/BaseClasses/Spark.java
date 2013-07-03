package org.friendscentral.steamnet.BaseClasses;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

/**
 * 
 * @author sambeckley 
 * 
 */

@SuppressWarnings("serial")
public class Spark extends Jawn implements Serializable{
	int id;
	char sparkType;
	char contentType;
	Comment[] comments;
	String content;
	String[] createdAts;
	String firstCreatedAt;
	int[] userIds;
	String[] tags;
	String firstUser;
	
	/*
	 * Don't need updated_at since they should never be edited, but perhaps we need a list of "uploaded at" dates
	 * Should Sparks have a name/caption type field?
	 * 
	 * Just setting everything in the creator, since after we grab it you should never need to edit it
	 */
	
	// TODO standardize spark constructors
	
	/**
	 * @param (int, char, char, String, String[], String, int[], String, Comment[], String[])
	 * @param i - int - id
	 * @param st - char - Spark Type
	 * @param ct - char - Content Type
	 * @param c - String - Content
	 * @param ca - String[] - CreatedAts
	 * @param fca - String - First Created At
	 * @param u - int[] - userIds
	 * @param fu - String - First User
	 * @param coms - Comment[]
	 * @param t - String[] - Tags
	 */
	public Spark(int i, char st, char ct, String c, String[] ca, String fca, int[] u, String fu, Comment[] coms, String[] t){
		//USED BY GETXJAWNS
		id = i;
		sparkType = st;
		contentType = ct;
		content = c;
		firstCreatedAt = fca;
		createdAts = ca;
		userIds = u;
		firstUser = fu;
		comments = coms;
		tags = t;
	}
	/**
	 * @param (id, char, char, String, String)
	 * @param int - id
	 * @param char - sparkType
	 * @param char - contentType
	 * @param String - content
	 * @param String[] - createdAts
	 * @param String - firstCreatedAt
	 * @param int[] - User IDs
	 * @param String - firstUser
	 */
	public Spark(int i, char st, char ct, String c, String[] ca, String fca, int[] u, String fu){
		//USED BY GETSPARK
		id = i;
		sparkType = st;
		contentType = ct;
		content = c;
		firstCreatedAt = fca;
		createdAts = ca;
		userIds = u;
		firstUser = fu;
	}
	/**
	 * @param (id, char, char, String, String[], int[], String)
	 * @param int - id
	 * @param char - sparkType
	 * @param char - contentType
	 * @param String - content
	 * @param String[] - createdAts
	 * @param int[] - User IDs
	 * @param String - firstUser
	 */
	public Spark(int i, char st, char ct, String c, String[] ca, int[] u, String fu){
		//USED BY POSTSPARK AND GETXSPARKS
		id = i;
		sparkType = st;
		contentType = ct;
		content = c;
		createdAts = ca;
		userIds = u;
		firstUser = fu;
	}
	/**
	 * @param char - sparkType
	 * @param char - contentType
	 * @param String - content
	 */
	public Spark(char st, char ct, String c){
		//USED BY MAINACTIVITY
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
	
	public Spark getSelfSpark(){
		return this;
	}
	
	public Idea getSelfIdea(){
		return null;
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
		if(createdAts[0] != null){
			return createdAts[0];
		} else {
			return null;
		}
	}
	
	public char getType(){
		return "S".charAt(0);
	}
	
	public String[] getTags() {
		return tags;
	}
	
	/**
	 * 
	 * @return The first User - Presumably the first person to Spark the Spark
	 */
	public String getUser(){
		return firstUser;
	}
	
	/**
	 * 
	 * @return The entire Users[]
	 */
	public int[] getUsers(){
		return userIds;
	}
	
	public String getDate(){
		return createdAts[0];
	}
	
	public String toString(){
		return content;
	}
	
	public void setComments(Comment[] c) {
		comments = c;
	}
	
	public Comment[] getComments() {
		return comments;
	}
	
	public void addComment(Comment c){
		Log.v("ADDCOMMENT", "Adding comment");
		// TODO make this work
		ArrayList<Comment> tmp = new ArrayList<Comment>();
		for(int i = 0; i < comments.length; i++){
			tmp.add(comments[i]);
		}
		tmp.add(c);
		Comment[] newComments = new Comment[tmp.size()];
		for(int k = 0; k < tmp.size();k++){
			newComments[k] = tmp.get(k);
		}
		comments = newComments;
	}
}
