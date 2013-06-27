package org.friendscentral.steamnet.BaseClasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author sambeckley
 *
 */
@SuppressWarnings("serial")
public class Idea extends Jawn implements Serializable{
	int id;
	String description;
	String[] tags;
	int[] sparkIds;
	String firstUser;
	int[] userIds;
	String[] createdAts;
	String firstCreatedAt;
	
	/**
	 * @param (int, String, String[], int[], int[], String)
	 * @param int - id
	 * @param String - description
	 * @param String[] - tags
	 * @param int[] - spark IDs
	 * @param int[] - user IDs
	 * @param String - firstUser
	 */
	public Idea(int i, String d, String[] t, int[] s, int[] u, String fu) {
		id = i;
		description = d;
		tags = t;
		sparkIds = s;
		userIds = u;
		firstUser = fu;
	}
	
	/**
	 * @param (int, String, String[], int[], String)
	 * @param int - id
	 * @param String - description
	 * @param String[] - tags
	 * @param int[] - spark IDs
	 * @param String - firstUser
	 */
	public Idea(int i, String d, String[] t, int[] s, String fu) {
		id = i;
		description = d;
		tags = t;
		sparkIds = s;
		firstUser = fu;
	}
	
	/**
	 * @param (int, String, String, int[], int[], String)
	 * @param int - id
	 * @param String - description
	 * @param String - split up string to be String[] of tags, String should be in the form of "tag1,tag2,tag3..."
	 * @param int[] - spark IDs
	 * @param int[] - user IDs
	 * @param String - username
	 * @param String[] - created ats
	 */
	public Idea(int i, String d, String t, int[] s, int[] u, String fu, String[] ca, String fca){
		id = i;
		description = d;
		sparkIds = s;
		userIds = u;
		firstUser = fu;
		createdAts = ca;
		firstCreatedAt = fca;
		
		ArrayList<String> tagsArrayList = new ArrayList<String>();
		while(true){
			if(t.indexOf(",") == -1){
				tagsArrayList.add(t);
				break;
			} else {
				tagsArrayList.add(t.substring(0, t.indexOf(",")));
				t = t.substring((t.indexOf(",") + 1));
			}
		}
		tags = new String[tagsArrayList.size()];
		for(int q = 0; q < tagsArrayList.size(); q++){
			tags[q] = tagsArrayList.get(q);
		}
	}
	
	/**
	 * id = -1, tags/sparks = empty, ArrayLists, username = ""
	 */
	public Idea(){
		id= -1;
		description = "";
		tags = new String[0];
		sparkIds = new int[0];
		firstUser = "";
	}
	
	public Spark getSelfSpark(){
		return null;
	}
	
	public Idea getSelfIdea(){
		return this;
	}
	
	public void setDescription(String s){
		description = s;
	}
	
	public void addTag(String t){
		ArrayList<String> tmp = new ArrayList<String>();
		for(String oldTag : tags){
			tmp.add(oldTag);
		}
		tmp.add(t);
		String[] newTagArray = new String[tmp.size()];
		for(int q = 0; q < tmp.size(); q++){
			newTagArray[q] = tmp.get(q);
		}
		setTags(newTagArray);
	}
	
	public void addSpark(int s){
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		for(int oldID : sparkIds){
			tmp.add(oldID);
		}
		tmp.add(s);
		int[] newIdArray = new int[tmp.size()];
		for(int q = 0; q < tmp.size(); q++){
			newIdArray[q] = tmp.get(q);
		}
		setIds(newIdArray);
	}
	
	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String[] getTags() {
		return tags;
	}
	
	public int[] getSparkIds() {
		return sparkIds;
	}
	
	public char getType(){
		return "I".charAt(0);
	}
	
	/**
	 * 
	 * @return The ID of the first user (presumably the first person to create the idea)
	 */
	public int getUser(){
		return userIds[0];
	}
	
	public int[] getUsers(){
		return userIds;
	}
	
	public void setTags(String[] s){
		tags = s;
	}
	
	public void setIds(int[] i){
		sparkIds = i;
	}
	
	public String getCreatedAt(){
		return firstCreatedAt;
	}
	
	public String[] getCreatedAts(){
		return createdAts;
	}
}
