package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.BaseClasses.Comment;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.json.parsers.JSONParser;
import com.squareup.okhttp.OkHttpClient;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

/**
 * @author SamBeckley
 * 
 */
public class AddXSparks {
	char spark_type;
	char content_type;
	String content;
	String user;
	String[] tags;
	String tagsString;
	int currentTotal;
	int limit;
	
	/** 
	 * @param int X - returns the first X sparks (by createdAt)
	 */
	
	public AddXSparks(int lim, GridView g, IndexGrid i, int curTotal) {
		currentTotal = curTotal;
		limit = lim;
		
		Log.v("REPORT", "GET X SPARKS IS BEGGINING, SIR!");
		OkHTTPTask task = new OkHTTPTask(g, i);
		task.execute("http://steamnet.herokuapp.com/api/v1/sparks.json?limit="+(limit+currentTotal));
		
	}
	
	class OkHTTPTask extends AsyncTask<String, Void, String> {
		
		String TAG = "RetreiveDataTask";
		
		OkHttpClient client;
		GridView gridView;
		IndexGrid indexGrid;
		
		public OkHTTPTask(GridView g, IndexGrid i){
			client = new OkHttpClient();
			gridView = g;
			indexGrid = i;
		}
        
		
        @SuppressWarnings("unused")
		private Exception exception;
        
        protected String doInBackground(String... urls) {
        	Log.v("REPORT", "WE ARE EXECUTING THE REQUEST IN THE BACKGROUND, SIR!");
            try {
            	return get(new URL(urls[0]));
            	
            } catch (Exception e) {
                this.exception = e;
                Log.e(TAG, "Exception: "+e);
                return null;
            }
        }

        protected void onPostExecute(String data) {
        	Log.v("REPORT", "WE HAVE MOVED INTO THE POST EXECUTE PHASE, SIR!");
        	try {
        		Log.v("REPORT", "WE WILL BEGIN TO PARSE THE DATA, SIR!");
				Jawn[] jawns = parseData(data);
				Log.v("JAWNS", Integer.toString(jawns.length));
				JawnAdapter a = indexGrid.getAdapter();
				Log.v("REPORT", "WE HAVE ACCESSED THE JAWNADAPTER AND ARE PROCEEDING AS PLANNED, SIR!");
				for (int i = limit; i < jawns.length; i++) {
					a.addAtPosition(jawns[i], a.getJawns().length);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        //PARSE DATA
        
		@SuppressWarnings("unused")
		Spark[] parseData(String data) throws JSONException {
			Log.v("REPORT", "WE ARE PARSING THE DATA, SIR!");
        	final String ID = "id";
        	final String SPARK_TYPE = "spark_type";
        	final String CONTENT_TYPE = "content_type";
        	final String CONTENT = "content";
        	final String CREATED_AT = "created_at";
        	final String USERS = "users";
        	final String USERNAME = "name";
        	final String COMMENTS = "comments";
        	final String COMMENT_TEXT = "comment_text";
        	// Creating JSON Parser instance
        	JSONParser jParser = new JSONParser();
        	 
        	// getting JSON string from URL
        	JSONArray sparks = new JSONArray(data);
        	
        	ArrayList<Spark> sparkArrayList = new ArrayList<Spark>();
        	Log.v("LENGTH", Integer.toString(sparks.length()));
        	try {        	     
        		for(int i = 0; i < sparks.length(); i++){// Storing each json item in variable
        			JSONObject json = sparks.getJSONObject(i);
					String id = json.getString(ID);
					String sparkType = json.getString(SPARK_TYPE);
					String contentType = json.getString(CONTENT_TYPE);
					String content = json.getString(CONTENT);
					String createdAt = json.getString(CREATED_AT);
					String firstUser = "";
					//Getting Array of Users
	        	    JSONArray usersJSON = json.getJSONArray(USERS);
	        	     
	        	    // looping through All Users
	        	    //ArrayList<Integer> usersArrayList = new ArrayList<Integer>();
	        	    //int count = 0;
	        	    /*for(int q = 0; q < usersJSON.length(); q++){
	        	        JSONObject u = usersJSON.getJSONObject(q);
	        	        // Storing each json item in variable
	        	        if(count == 0){
	        	        	count++;
	        	        	firstUser = u.getString(USERNAME);
	        	        }
	        	        int userID = u.getInt(ID);
	        	        usersArrayList.add(userID);
	        	    }*/
	        	    int[] usersArray = new int[1];
	        	    usersArray[0] = 1;
	        	    /*for(int q = 0; q < usersArrayList.size(); q++){
	        	    	usersArray[q] = usersArrayList.get(q);
	        	    }*/
	        	    
	        	    String[] createdAts = new String[1];
	        	    createdAts[0] = createdAt;
	        	    //******************
	        	    
	        	    
	        	    JSONArray commentsJSON = json.getJSONArray(COMMENTS);
	        	    
	        	    ArrayList<Comment> commentsArrayList = new ArrayList<Comment>();
	        	    for(int k = 0; k < commentsJSON.length(); k++){
	        	    	JSONObject c = commentsJSON.getJSONObject(k);
	        	    	String commentText = c.getString(COMMENT_TEXT);
	        	    	
	        	    	/*
	        	    	 * 0 as a substitute for the real user id
	        	    	 * int commentUser = json.getString(COMMENT_USER);
	        	    	 * or something
	        	    	 */
	        	    	
	        	    	commentsArrayList.add(new Comment(0, commentText));
	        	    }
	        	    
	        	    Comment[] commentArray = new Comment[commentsArrayList.size()];
	        	    for(int j = 0; j < commentsArrayList.size(); j++){
	        	    	commentArray[j] = commentsArrayList.get(j);
	        	    }
	        	    
	        	    
	        	    //*****************************************
	        	    Spark newSpark = new Spark(Integer.parseInt(id), sparkType.charAt(0), contentType.charAt(0), content, createdAts, usersArray, firstUser);
	        	    newSpark.setComments(commentArray);
					sparkArrayList.add(newSpark);
        		}
        	    Log.v("ARRAY", sparkArrayList.toString());
        	    Spark[] sparkArray = new Spark[sparkArrayList.size()];
        	    for(int i = 0; i < sparkArrayList.size(); i++){
        	    	sparkArray[i] = sparkArrayList.get(i);
        	    }
        	    return sparkArray;
        	} catch (JSONException e) {
        	    e.printStackTrace();
        	}
        	return null;
        }
        
        String get(URL url) throws IOException {
          HttpURLConnection connection = client.open(url);
          InputStream in = null;
          try {
            // Read the response.
            in = connection.getInputStream();
            byte[] response = readFully(in);
            return new String(response, "UTF-8");
          } finally {
            if (in != null) in.close();
          }
        }
        
        byte[] readFully(InputStream in) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
              out.write(buffer, 0, count);
            }
            return out.toByteArray();
          }
	    
	 }

}
