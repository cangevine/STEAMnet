package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.json.parsers.JSONParser;
import com.squareup.okhttp.OkHttpClient;

import BaseClasses.Spark;
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
	String user;
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
        	final String ID = "id";
        	final String SPARK_TYPE = "spark_type";
        	final String CONTENT_TYPE = "content_type";
        	final String CONTENT = "content";
        	final String CREATED_AT = "created_at";
        	final String UPDATED_AT = "updated_at";
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
				String createdAt = json.getString(CREATED_AT);
				String updatedAt = json.getString(UPDATED_AT);
				
				Spark newSpark = new Spark(Integer.parseInt(id), sparkType.charAt(0), contentType.charAt(0), content, createdAt);
				//call some function in main thread to pass over newSpark
				//LOOK HERE
				//ITS RIGHT HERE
				//REALLY!
				Log.v("TEST", content);
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
