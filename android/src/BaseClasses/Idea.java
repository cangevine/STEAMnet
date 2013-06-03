package BaseClasses;

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
	ArrayList<String> tags;
	ArrayList<Spark> sparks;
	String username;
	
	/**
	 * id = -1, tags/sparks = empty, ArrayLists, username = ""
	 */
	public Idea(){
		id= -1;
		description = "";
		tags = new ArrayList<String>();
		sparks = new ArrayList<Spark>();
		username = "";
	}
	
	/**
	 * @param (int, String, ArrayList<String>, ArrayList<Spark>, String)
	 * @param int - id
	 * @param String - description
	 * @param ArrayList<String> - tags
	 * @param ArrayList<Sparks> - sparks
	 * @param String - username
	 */
	public Idea(int i, String d, ArrayList<String> t, ArrayList<Spark> s, String u) {
		id = i;
		description = d;
		tags = t;
		sparks = s;
		username = u;
	}
	
	/**
	 * @param (int, String, String, ArrayList<Spark>, String)
	 * @param int - id
	 * @param String - description
	 * @param String - split up string to be ArrayList<String> of tags, String should be in the form of "tag1,tag2,tag3..."
	 * @param ArrayList<Spark> - sparks
	 * @param String - username
	 */
	public Idea(int i, String d, String t, ArrayList<Spark> s, String u){
		id = i;
		description = d;
		sparks = s;
		username = u;
		
		tags = new ArrayList<String>();
		while(true){
			if(t.indexOf(",") == -1){
				tags.add(t);
				break;
			} else {
				tags.add(t.substring(0, t.indexOf(",")));
				t = t.substring((t.indexOf(",") + 1));
			}
		}
	}
	
	public void setDescription(String s){
		description = s;
	}
	
	public void addTag(String t){
		tags.add(t);
	}
	
	public void addSpark(Spark s){
		sparks.add(s);
	}
	
	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ArrayList<String> getTags() {
		return tags;
	}
	
	public ArrayList<Spark> getSparks() {
		return sparks;
	}
	
	public String getUsername(){
		return username;
	}
	
	public char getType(){
		return "I".charAt(0);
	}
}
