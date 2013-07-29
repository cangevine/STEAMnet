package org.friendscentral.steamnet.BaseClasses;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;

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
	String tagsString;
	int[] sparkIds;
	String firstUser;
	int userId;
	String[] createdAts;
	String firstCreatedAt;
	Comment[] comments;
	Bitmap[] sparkThumbs;
	Spark[] sparks;
	
	/**
	 * @param (int, String, String[], int[], int[], String)
	 * @param int - id
	 * @param String - description
	 * @param String[] - tags
	 * @param int[] - spark IDs
	 * @param int[] - user IDs
	 * @param String - firstUser
	 */
	public Idea(int i, String d, String[] t, int[] s, int u, String fu, String fca, Comment[] c) {
		id = i;
		description = d;
		tags = t;
		sparkIds = s;
		userId = u;
		firstUser = fu;
		firstCreatedAt = fca;
		comments = c;
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
	public Idea(int i, String d, String t, int[] s, int u, String fu, String[] ca, String fca){
		id = i;
		description = d;
		sparkIds = s;
		userId = u;
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
	
	public Idea(int i, String d, String[] t, Spark[] s, String fu, String fca, Comment[] c){
		id = i;
		description = d;
		sparks = s;
		firstUser = fu;
		firstCreatedAt = fca;
		comments = c;
		
		tags = t;
		tagsString = "";
		int index = 0;
		for (String tag : tags) {
			tagsString += tag;
			if (index < tags.length - 1) {
				tagsString += ", ";
			}
			index++;
		}
	}
	
	/**
	 * 
	 */
	public Idea(int i, String d, String ca) {
		id = i;
		description = d;
		firstCreatedAt = ca;
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
		return 'I';
	}
	
	/**
	 * 
	 * @return The ID of the first user (presumably the first person to create the idea)
	 */
	public String getUsername(){
		return firstUser;
	}
	
	public void setUsername(String u) {
		firstUser = u;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public void setUserId(int u) {
		userId = u;
	}
	
	public void setTags(String[] s){
		tags = s;
	}
	
	public int[] getIds() {
		return sparkIds;
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
	
	public String toString(){
		return description;
	}
	
	public Comment[] getComments() {
		return comments;
	}
	
	/*public Bitmap[] getBitmaps() {
		return sparkThumbs;
	}
	
	public Bitmap getBitmap(int i) {
		if (i < sparkThumbs.length) { 
			return sparkThumbs[i];
		}
		return null;
	}
	
	public void setBitmaps(Bitmap[] b) {
		sparkThumbs = b;
	}
	
	public void setBitmap(Bitmap b, int i) {
		if (i < sparkThumbs.length) { 
			sparkThumbs[i] = b;
		}
	}*/
	
	public void setSparks(Spark[] s) {
		sparks = s;
	}
	
	public void setSpark(int pos, Spark s) {
		if (sparks != null) {
			if (pos < sparks.length) {
				sparks[pos] = s;
			}
		}
	}
	
	public Spark[] getSparks() {
		return sparks;
	}
	
	public Spark getSpark(int pos) {
		if (sparks != null) {
			if (pos < sparks.length) {
				return sparks[pos];
			}
		}
		return null;
	}
}
