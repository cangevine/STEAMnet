package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.BaseClasses.Idea;
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
public class RetrieveDataTaskGetXJawns {
	char spark_type;
	char content_type;
	String content;
	String user;
	String[] tags;
	String tagsString;
	
	/** 
	 * @param int X - returns the first X sparks (by createdAt)
	 */
	
	public RetrieveDataTaskGetXJawns(int lim, GridView g, IndexGrid i) {
		
		Log.v("REPORT", "THE TASK IS BEGGINING, SIR!");
		OkHTTPTask task = new OkHTTPTask(g, i);
		task.execute("http://steamnet.herokuapp.com/api/v1/jawns.json?limit="+lim);
		
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
        	Log.d(TAG, "=> "+data);
        	try {
        		Log.v("REPORT", "WE WILL BEGIN TO PARSE THE DATA, SIR!");
				Jawn[] jawns = parseData(data);
				
				JawnAdapter a = new JawnAdapter(gridView.getContext(), jawns, 200);
				Log.v("REPORT", "WE HAVE ACCESSED THE JAWNADAPTER AND ARE PROCEEDING AS PLANNED, SIR!");
				indexGrid.setAdapter(a);
				indexGrid.setJawns(jawns);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        //PARSE DATA
        
		@SuppressWarnings("unused")
		Jawn[] parseData(String data) throws JSONException {
			Log.v("REPORT", "WE ARE PARSING THE DATA, SIR!");
			final String ID = "id";
        	final String SPARK_TYPE = "spark_type";
        	final String CONTENT_TYPE = "content_type";
        	final String CONTENT = "content";
        	final String CREATED_AT = "created_at";
        	final String CREATED_ATS = "created_ats";
        	final String UPDATED_AT = "updated_at";
        	final String USERS = "users";
        	final String USER = "user";
        	final String USERNAME = "name";
        	final String DESCRIPTION = "description";
        	final String TAGS = "tags";
        	final String SPARKS = "sparks";
        	// Creating JSON Parser instance
        	JSONParser jParser = new JSONParser();
        	 
        	// getting JSON string from URL
        	JSONArray jawns = new JSONArray(data);
        	
        	ArrayList<Jawn> jawnArrayList = new ArrayList<Jawn>();
        	
        	try {        	     
        	    // looping through All Jawns
        	    for(int i = 0; i < jawns.length(); i++){
        	        JSONObject j = jawns.getJSONObject(i);
        	        
        	        //checking to see if the Jawn is a Spark or an Idea
        	        if(/*ITS A SPARK*/){
        	        	// Storing each json item in variable
        				String id = j.getString(ID);
        				String sparkType = j.getString(SPARK_TYPE);
        				String contentType = j.getString(CONTENT_TYPE);
        				String content = j.getString(CONTENT);
        				String firstUser = "";
        				
        				//Getting Array of Users
                	    JSONArray usersJSON = j.getJSONArray(USERS);
                	     
                	    // looping through All Users
                	    ArrayList<Integer> usersArrayList = new ArrayList<Integer>();
                	    int count = 0;
                	    for(int q = 0; q < usersJSON.length(); q++){
                	        JSONObject u = usersJSON.getJSONObject(q);
                
                	        // Storing each json item in variable
                	        if(count == 0){
                	        	count++;
                	        	firstUser = u.getString(USERNAME);
                	        }
                	        int userID = u.getInt(ID);
                	        usersArrayList.add(userID);
                	    }
                	    int[] usersArray = new int[usersArrayList.size()];
                	    for(int q = 0; q < usersArrayList.size(); q++){
                	    	usersArray[q] = usersArrayList.get(q);
                	    }
                	    
                	    //Getting Array of Created Ats
                	    JSONArray createdAtsJSON = j.getJSONArray(CREATED_ATS);
                	    String[] createdAts = new String[10];
                	    String firstCreatedAt = "";
                	    int counter = 0;
                	    for(int c = 0; c < j.length(); c++){
                	        JSONObject s = createdAtsJSON.getJSONObject(c);
                	        // Storing each json item in variable
                	        if(counter == 0){
                	        	firstCreatedAt = s.getString(CREATED_AT);
                	        	counter++;
                	        }
                	        String createdAt = s.getString(CREATED_AT);
                	        createdAts[c] = createdAt;
                	    }
                	    
	        	        jawnArrayList.add(new Spark(Integer.parseInt(id), sparkType.charAt(0), contentType.charAt(0), content, createdAts, firstCreatedAt, usersArray, firstUser));
	        	        
        	        } else /*Assume its an Idea*/ {
        	        	
        	        	// Getting Idea parameters
                		int id = j.getInt(ID);
            	        String description = j.getString(DESCRIPTION);
            	        String tags = j.getString(TAGS);
            	        
            	        //Getting Array of Sparks
                	    JSONArray ideasJSON = j.getJSONArray(SPARKS);
                	     
                	    // looping through All Sparks
                	    int[] sparkIds = new int[10];
                	    for(int c = 0; c < ideasJSON.length(); c++){
                	        JSONObject s = ideasJSON.getJSONObject(c);
                	        // Storing each json item in variable
                	        int sparkId = s.getInt(ID);
                	        sparkIds[c] = sparkId;
                	    }
                	    
                	    String firstUser = "";
                	    //Getting Array of Users
                	    JSONArray usersJSON = j.getJSONArray(USERS);
                	    
                	    //looping through all Users
                	    int[] userIds = new int[10];
                	    int count = 0;
                	    for(int q = 0; q < usersJSON.length(); q++){
                	    	JSONObject u = usersJSON.getJSONObject(q);
                	    	if(count == 0){
                	    		firstUser = u.getString(USERNAME);
                	    		count++;
                	    	}
                	    	userIds[q] = u.getInt(ID);
                	    }
                	    
                	    //Getting Array of Created Ats
                	    JSONArray createdAtsJSON = j.getJSONArray(CREATED_ATS);
                	    
                	    //Looping through All Created Ats
                	    String[] createdAts = new String[10];
                	    String firstCreatedAt = "";
                	    int counter = 0;
                	    for(int w = 0; w < ideasJSON.length(); w++){
                	        JSONObject s = createdAtsJSON.getJSONObject(w);
                	        // Storing each json item in variable
                	        if(counter == 0){
                	        	firstCreatedAt = s.getString(CREATED_AT);
                	        	counter++;
                	        }
                	        String createdAt = s.getString(CREATED_AT);
                	        createdAts[w] = createdAt;
                	    }
                	    
                	Idea newIdea = new Idea(id, description, tags, sparkIds, userIds, firstUser, createdAts, firstCreatedAt);
        	        }
        	    }
        	    Jawn[] jawnArray = new Jawn[jawnArrayList.size()];
        	    for(int i = 0; i < jawnArrayList.size(); i++){
        	    	jawnArray[i] = jawnArrayList.get(i);
        	    }
        	    return jawnArray;
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
