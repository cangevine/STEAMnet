package org.friendscentral.steamnet.BaseClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

/**
 * 
 * @author sambeckley 
 * 
 */

@SuppressWarnings("serial")
public class Spark extends Jawn implements Serializable {
	int id;
	char sparkType;
	char contentType;
	Comment[] comments;
	String content;
	String[] createdAts;
	String firstCreatedAt;
	int[] userIds;
	String[] tags;
	String tagsString;
	String firstUser;
	String multimediaCloudLink;
	
	Uri multimedia;
	Bitmap image;
	
	/*
	 * Don't need updated_at since they should never be edited, but perhaps we need a list of "uploaded at" dates
	 * Should Sparks have a name/caption type field?
	 * 
	 * Users/CreatedAts need to be plural since multiple users (at multiple times) can be credited with the same Spark
	 * 
	 * Just setting everything in the creator, since after we grab it you should never need to edit it
	 */
	public Spark(int i, char st, char ct, String c, String[] ca, String fca, int[] u, String fu, Comment[] coms){
		id = i;
		sparkType = st;
		contentType = ct;
		content = c;
		firstCreatedAt = fca;
		createdAts = ca;
		userIds = u;
		firstUser = fu;
		comments = coms;
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
		id = i;
		sparkType = st;
		contentType = ct;
		content = c;
		createdAts = ca;
		firstCreatedAt = createdAts[0];
		userIds = u;
		firstUser = fu;
	}
	/**
	 * @param (id, char, char, String, String)
	 * @param int - id
	 * @param char - sparkType
	 * @param char - contentType
	 * @param String - content
	 * @param String - createdAt
	 */
	public Spark(int i, char st, char ct, String c, String[] ca) {
		id = i;
		sparkType = st;
		contentType = ct;
		content = c;
		createdAts = ca;
		userIds = new int[0];
	}
	/**
	 * 
	 */
	public Spark(int i, char st, char ct, String c, String ca) {
		id = i;
		sparkType = st;
		contentType = ct;
		content = c;
		firstCreatedAt = ca;
		userIds = new int[0];
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
		userIds = new int[0];
	}
	/**
	 * @param char - sparkType
	 * @param char - contentType
	 * @param String - content
	 */
	public Spark(char st, char ct, String c){
		sparkType = st;
		contentType = ct;
		content = c;
		userIds = new int[0];
	}
	/**
	 * @param char - sparkType
	 * @param char - contentType
	 * @param String - content
	 * @param String - userId
	 */
	public Spark(char st, char ct, String c, String id, String t){
		sparkType = st;
		contentType = ct;
		content = c;
		userIds = new int[1];
		userIds[0] = Integer.valueOf(id);
		firstUser = id;
		tagsString = t;
		tags = tagsString.split(", ");
	}
	
	/**
	 * @param char - sparkType
	 * @param char - contentType
	 * @param String - content
	 * @param String - userId
	 */
	public Spark(char st, char ct, String c, String id){
		sparkType = st;
		contentType = ct;
		content = c;
		userIds = new int[1];
		userIds[0] = Integer.valueOf(id);
		firstUser = id;
	}
	
	/**
	 * id = -1, sparkType/contentType/content = " "
	 */
	
	public Spark(){
		id = -1;
		sparkType = " ".charAt(0);
		contentType = " ".charAt(0);
		content = " ";
		userIds = new int[0];
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
	
	public void setTags(String t) {
		tagsString = t;
	}
	
	public void setTags(String[] t) {
		tags = t;
	}
	
	/**
	 * 
	 * @return The first User - Presumably the first person to Spark the Spark
	 */
	public String getUsername(){
		return firstUser;
	}
	
	public void setUsername(String u) {
		firstUser = u;
	}
	
	/**
	 * 
	 * @return The entire Users[]
	 */
	public int[] getUserIds(){
		return userIds;
	}
	
	public void setUserIds(int[] u) {
		userIds = u;
	}
	
	public String getDate(){
		return firstCreatedAt;
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
	
	public void setUri(Uri uri) {
		multimedia = uri;
	}
	
	public Uri getUri() {
		return multimedia;
	}
	
	public void setBitmap(Bitmap b) {
		image = b;
	}
	
	public Bitmap getBitmap() {
		return image;
	}
	public String getTagsString() {
		return tagsString;
	}
	public String getFirstUser() {
		return firstUser;
	}
	
	public String getCloudLink() {
		return multimediaCloudLink;
	}
	public void setCloudLink(String s) {
		multimediaCloudLink = s;
	}
	
	/*private void writeObject(ObjectOutputStream out) throws IOException{

	    out.writeInt(id);
	    
	    out.writeChar(sparkType);
	    out.writeChar(contentType);

	    out.writeObject(comments);
	    out.writeObject(content);
	    out.writeObject(createdAts);
	    out.writeObject(firstCreatedAt);
	    out.writeObject(tags);
	    out.writeObject(tagsString);
	    out.writeObject(firstUser);
	    out.writeObject(multimedia);


	    out.writeInt(image.getRowBytes());
	    out.writeInt(image.getHeight());
	    out.writeInt(image.getWidth());

	    int bmSize = image.getRowBytes() * image.getHeight();
	    if(dst==null || bmSize > dst.capacity())
	        dst= ByteBuffer.allocate(bmSize);

	    out.writeInt(dst.capacity());

	    dst.position(0);

	    image.copyPixelsToBuffer(dst);
	    if(bytesar==null || bmSize > bytesar.length)
	        bytesar=new byte[bmSize];

	    dst.position(0);
	    dst.get(bytesar);


	    out.write(bytesar, 0, bytesar.length);

	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{

	    id=in.readInt();
	    
	    sparkType = in.readChar();
	    contentType = in.readChar();
	    
	    comments = (Comment[]) in.readObject();
	    content = (String) in.readObject();
	    createdAts = (String[]) in.readObject();
	    firstCreatedAt = (String) in.readObject();
	    tags = (String[]) in.readObject();
	    tagsString = (String) in.readObject();
	    firstUser = (String) in.readObject();
	    multimedia = (Uri) in.readObject();


	    int nbRowBytes=in.readInt();
	    int height=in.readInt();
	    int width=in.readInt();

	    int bmSize=in.readInt();



	    if(bytesar==null || bmSize > bytesar.length)
	        bytesar= new byte[bmSize];


	    int offset=0;

	    while(in.available()>0){
	        offset=offset + in.read(bytesar, offset, in.available());
	    }


	    if(dst==null || bmSize > dst.capacity())
	        dst= ByteBuffer.allocate(bmSize);
	    dst.position(0);
	    dst.put(bytesar);
	    dst.position(0);
	    image=Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	    image.copyPixelsFromBuffer(dst);
	    //in.close();
	}*/
}
