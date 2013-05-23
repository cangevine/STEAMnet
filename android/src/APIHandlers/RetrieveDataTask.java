package APIHandlers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.squareup.okhttp.OkHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class RetrieveDataTask {
	char spark_type;
	char content_type;
	String content;
	String user;
	String[] tags;
	String tagsString;
	
	public RetrieveDataTask(char st, char ct, String c, String u, String[] t) {
		spark_type = st;
		content_type = ct;
		content = c;
		user = u;
		tags = t;
		
		tagsString = "";
		for (int i = 0; i < tags.length; i++) {
			tagsString += tags[i];
			if (i != tags.length - 1) {
				tagsString += ",";
			}
		}
		
		OkHTTPTask task = new OkHTTPTask();
		task.execute("http://steamnet.herokuapp.com/api/v1/sparks.json");
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
	        	
	        	String postData = "&spark[spark_type]="+spark_type+"&spark[content_type]="+content_type+"&spark[content]="+content+"&username="+user+"&tags="+tagsString;
	        	Log.v(TAG, postData);
	        	
	        	//return get(new URL(urls[0]));
	        	
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
	    
	    /*
	     * TRYING TO MAKE POST FROM HERE DOWN
	     */
	    
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
	    /*
	     * END ATTEMPT TO MAKE POST
	     */
	    
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