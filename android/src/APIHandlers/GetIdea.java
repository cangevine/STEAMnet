package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.BaseClasses.Idea;
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
public class GetIdea {
	int id;
	
	/**
	 * @param id - int, ID of the Idea you want to get
	 */
	
	public GetIdea(int q) {
		id = q;
		
		OkHTTPTask task = new OkHTTPTask();
		task.execute("http://steamnet.herokuapp.com/api/v1/ideas/"+id+".json");
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
				parseData(data);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    /**
	     * @param data - String
	     * @throws JSONException
	     */
	    void parseData(String data) throws JSONException {
			Log.v("TEST", "BEGGINING TO PARSE DATA");
        	//Ideas
			final String ID = "id";
        	final String DESCRIPTION = "description";
        	final String TAGS = "tags";
        	final String SPARKS = "sparks";
        	final String USER = "username";
        	//Sparks (currently unused)
        	final String SPARK_TYPE = "spark_type";
        	final String CONTENT_TYPE = "content_type";
        	final String CONTENT = "content";
        	final String CREATED_AT = "created_at";
        	final String CREATED_ATS = "created_ats";
        	final String USERS = "users";
        	final String USERNAME = "name";
        	// Creating JSON Parser instance
        	JSONParser jParser = new JSONParser();
        	 
        	// getting JSON string from URL
        	JSONObject json = new JSONObject(data);
        	 
        	JSONArray ideasJSON = null;
        	
        	try {
        	    // Getting Idea parameters
        		int id = json.getInt(ID);
    	        String description = json.getString(DESCRIPTION);
    	        String tags = json.getString(TAGS);
    	        
    	        //Getting Array of Sparks
        	    ideasJSON = json.getJSONArray(SPARKS);
        	     
        	    // looping through All Sparks
        	    int[] sparkIds = new int[10];
        	    for(int i = 0; i < ideasJSON.length(); i++){
        	        JSONObject s = ideasJSON.getJSONObject(i);
        	        // Storing each json item in variable
        	        int sparkId = s.getInt(ID);
        	        sparkIds[i] = sparkId;
        	    }
        	    
        	    String firstUser = "";
        	    int userId = 0;
        	    //Getting Array of Users
        	    JSONArray usersJSON = json.getJSONArray(USERS);
        	    
        	    firstUser = usersJSON.getJSONObject(0).getString(USERNAME);
        	    userId = usersJSON.getJSONObject(0).getInt(ID);
        	    
        	    
        	    //Getting Array of Created Ats
        	    JSONArray createdAtsJSON = json.getJSONArray(CREATED_ATS);
        	    
        	    //Looping through All Created Ats
        	    String[] createdAts = new String[10];
        	    String firstCreatedAt = "";
        	    int counter = 0;
        	    for(int i = 0; i < ideasJSON.length(); i++){
        	        JSONObject s = createdAtsJSON.getJSONObject(i);
        	        // Storing each json item in variable
        	        if(counter == 0){
        	        	firstCreatedAt = s.getString(CREATED_AT);
        	        	counter++;
        	        }
        	        String createdAt = s.getString(CREATED_AT);
        	        createdAts[i] = createdAt;
        	    }
        	    
        	Idea newIdea = new Idea(id, description, tags, sparkIds, userId, firstUser, createdAts, firstCreatedAt);
        	/*
        	 * PUT SOME FUNCTION HERE TO PASS newIdea BACK TO THE MAIN THREAD
        	 */
        	} catch (JSONException e) {
        	    e.printStackTrace();
        	}
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
