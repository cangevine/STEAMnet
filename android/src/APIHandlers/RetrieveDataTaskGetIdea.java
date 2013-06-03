package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.json.parsers.JSONParser;
import com.squareup.okhttp.OkHttpClient;

import BaseClasses.Idea;
import BaseClasses.Spark;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Sam Beckley
 */
@SuppressWarnings("unused")
public class RetrieveDataTaskGetIdea {
	int id;
	
	/**
	 * @param id - int, ID of the Idea you want to get
	 */
	
	public RetrieveDataTaskGetIdea(int q) {
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
        	//Sparks
        	final String SPARK_TYPE = "spark_type";
        	final String CONTENT_TYPE = "content_type";
        	final String CONTENT = "content";
        	final String CREATED_AT = "created_at";
        	final String UPDATED_AT = "updated_at";
        	// Creating JSON Parser instance
        	JSONParser jParser = new JSONParser();
        	 
        	// getting JSON string from URL
        	JSONObject json = new JSONObject(data);
        	 
        	JSONArray sparksJSON = null;
        	
        	try {
        		ArrayList<Spark> sparkList = new ArrayList<Spark>();
        	    // Getting Idea parameters
        		int id = json.getInt(ID);
    	        String description = json.getString(DESCRIPTION);
    	        String tags = json.getString(TAGS);
    	        String user = json.getString(USER);
    	        
    	        //Getting Array of Sparks
        	    sparksJSON = json.getJSONArray(SPARKS);
        	     
        	    // looping through All Sparks
        	    for(int i = 0; i < sparksJSON.length(); i++){
        	        JSONObject s = sparksJSON.getJSONObject(i);
        	        // Storing each json item in variable
        	        int sparkId = s.getInt(ID);
        	        String sparkType = s.getString(SPARK_TYPE);
    				String contentType = s.getString(CONTENT_TYPE);
    				String content = s.getString(CONTENT);
    				String createdAt = s.getString(CREATED_AT);
        	        sparkList.add(new Spark(id, sparkType.charAt(0), contentType.charAt(0), content, createdAt));
        	    }
        	Idea newIdea = new Idea(id, description, tags, sparkList, user);
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
