package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.json.parsers.JSONParser;
import com.squareup.okhttp.OkHttpClient;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Sam Beckley
 */
@SuppressWarnings("unused")
public class RetrieveDataTaskGetSpark {
	char spark_type;
	char content_type;
	String content;
	String[] users;
	String[] tags;
	String tagsString;
	
	/**
	 * @param id - int, ID of the Spark you want to get
	 */
	
	public RetrieveDataTaskGetSpark(int id) {
		
		tagsString = "";
		for (int i = 0; i < tags.length; i++) {
			tagsString += tags[i];
			if (i != tags.length - 1) {
				tagsString += ",";
			}
		}
		
		OkHTTPTask task = new OkHTTPTask();
		task.execute("http://steamnet.herokuapp.com/api/v1/sparks/"+id+".json");
	}
	
	class OkHTTPTask extends AsyncTask<String, Void, String> {
	
		String TAG = "RetreiveDataTask";
		
	    OkHttpClient client = new OkHttpClient();
		
	    
		private Exception exception;
	    
	    @Override
	    protected String doInBackground(String... urls) {
	        try {
	        	
	        	return get(new URL(urls[0]));
	        		        	
	        } catch (Exception e) {
	            this.exception = e;
	            Log.e(TAG, "Exception: "+e);
	            return null;
	        }
	    }
	    
	    protected void onPostExecute(String data) {
	    	Log.v("TEST", "MOVING INTO POST EXECUTE PHASE, SIR");
        	Log.d(TAG, "=> "+data);
        	try {
        		Log.v("TEST", "GONNA PARSE ME SOME DATA");
				Spark newSpark = parseData(data);
				//call some function in main thread to pass over newSpark
				//LOOK HERE
				//ITS RIGHT HERE
				//REALLY!
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    /**
	     * @param data - String
	     * @throws JSONException
	     */
	    Spark parseData(String data) throws JSONException {
			Log.v("TEST", "BEGGINING TO PARSE DATA");
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
        	// Creating JSON Parser instance
        	JSONParser jParser = new JSONParser();
        	 
        	// getting JSON string from URL
        	JSONObject json = new JSONObject(data);
        	 
        	try {
				// Storing each json item in variable
				String id = json.getString(ID);
				String sparkType = json.getString(SPARK_TYPE);
				String contentType = json.getString(CONTENT_TYPE);
				String content = json.getString(CONTENT);
				String firstUser = "";
				
				//Getting Array of Users
        	    JSONArray usersJSON = json.getJSONArray(USERS);
        	     
        	    // looping through All Users
        	    ArrayList<Integer> usersArrayList = new ArrayList<Integer>();
        	    int count = 0;
        	    for(int i = 0; i < usersJSON.length(); i++){
        	        JSONObject u = usersJSON.getJSONObject(i);
        
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
        	    JSONArray createdAtsJSON = json.getJSONArray(CREATED_ATS);
        	    String[] createdAts = new String[10];
        	    String firstCreatedAt = "";
        	    int counter = 0;
        	    for(int c = 0; c < json.length(); c++){
        	        JSONObject s = createdAtsJSON.getJSONObject(c);
        	        // Storing each json item in variable
        	        if(counter == 0){
        	        	firstCreatedAt = s.getString(CREATED_AT);
        	        	counter++;
        	        }
        	        String createdAt = s.getString(CREATED_AT);
        	        createdAts[c] = createdAt;
        	    }
        	    
				Spark newSpark = new Spark(Integer.parseInt(id), sparkType.charAt(0), contentType.charAt(0), content, createdAts, firstCreatedAt, usersArray, firstUser);
				Log.v("TEST", newSpark.toString());
				return newSpark;
        	} catch (JSONException e) {
        	    e.printStackTrace();
        	}
        	return null;
        }
	    
	    //THIS METHOD COPY/PASTED FROM WEBSITE
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
	    
	    //THIS METHOD COPY/PASTED FROM WEBSITE
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
