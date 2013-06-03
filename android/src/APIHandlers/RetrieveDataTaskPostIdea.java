package APIHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.squareup.okhttp.OkHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class RetrieveDataTaskPostIdea {
	String description;
	int[] sparks;
	String sparksString;
	String[] tags;
	String tagsString;
	String user;
	
	/**
	 * @param (String, int[], String[], String)
	 * @param String - description of the Idea
	 * @param int[] - Spark IDs, will be broken down to a String of comma separated numbers
	 * @param String[] - Tags
	 * @param String - username, must be legit for authentication purposes
	 * 
	 * @throws IOexception
	 */
	
	public RetrieveDataTaskPostIdea(String d, int[] s, String[] t, String u) {
		description = d;
		sparks = s;
		tags = t;
		user = u;
		tagsString = "";
		for (int i = 0; i < tags.length; i++) {
			tagsString += tags[i];
			if (i != tags.length - 1) {
				tagsString += ",";
			}
		}
		sparksString = "";
		for (int i = 0; i < sparks.length; i++) {
			sparksString += sparks[i];
			if (i != sparks.length - 1) {
				sparksString += ",";
			}
		}
		
		OkHTTPTask task = new OkHTTPTask();
		task.execute("http://steamnet.herokuapp.com/api/v1/ideas.json");
	}
	
	@SuppressWarnings("unused")
	class OkHTTPTask extends AsyncTask<String, Void, String> {
	
		String TAG = "RetreiveDataTask";
		
	    OkHttpClient client = new OkHttpClient();
		
	    private Exception exception;
	    
	    @Override
	    protected String doInBackground(String... urls) {
	        try {
	        	
	        	/*
	        	 * BELOW ME IS THE STRING TO EDIT, READ THE README ON GITHUB
	        	 * RIGHT HERE!
	        	 * LOOK! ITS RIGHT THERE!
	        	 */
	        	
	        	//String postData = "&spark[spark_type]="+spark_type+"&spark[content_type]="+content_type+"&spark[content]="+content+"&username="+user+"&tags="+tagsString;
	        	String postData = "&idea[description]="+description+"&tags="+tagsString+"&sparks="+sparksString+"&username="+user;
	        	Log.v(TAG, postData);
	        		        	
	        	return post(new URL(urls[0]), postData.getBytes());
	        	
	        } catch (Exception e) {
	            this.exception = e;
	            Log.e(TAG, "Exception: "+e);
	            return null;
	        }
	    }
	
	    protected void onPostExecute(String data) {
	    	Log.d(TAG, "=> "+data);
	    }
	    
	    String post(URL url, byte[] body) throws IOException {
	        HttpURLConnection connection = client.open(url);
	        OutputStream out = null;
	        InputStream in = null;
	        try {
	          // Write the request.
	          Log.v("TEST", "Writing the Request");
	          connection.setRequestMethod("POST");
	          out = connection.getOutputStream();
	          out.write(body);
	          Log.v("TEST", "Closing the Request");
	          out.close();
	
	          // Read the response.
	          if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	        	Log.v("TEST", "GOT AN ERROR!!!!!!");
	            throw new IOException("Unexpected HTTP response: "
	                + connection.getResponseCode() + " " + connection.getResponseMessage());
	          }
	          in = connection.getInputStream();
	          Log.v("TEST", "Reading the first line...");
	          return readFirstLine(in);
	        } finally {
	          // Clean up.
	          if (out != null) out.close();
	          if (in != null) in.close();
	        }
	      }
	    
	    String readFirstLine(InputStream in) throws IOException {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	        return reader.readLine();
	      }
	    
	 }

}
