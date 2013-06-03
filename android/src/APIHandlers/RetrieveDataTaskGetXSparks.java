package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.json.parsers.JSONParser;
import com.squareup.okhttp.OkHttpClient;

import BaseClasses.Spark;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

/**
 * @author SamBeckley
 * 
 */
public class RetrieveDataTaskGetXSparks {
	char spark_type;
	char content_type;
	String content;
	String user;
	String[] tags;
	String tagsString;
	
	/** 
	 * @param int X - returns the first X sparks (by createdAt)
	 */
	
	public RetrieveDataTaskGetXSparks(int lim, GridView g, IndexGrid i) {
		
		Log.v("REPORT", "THE TASK IS BEGGINING, SIR!");
		OkHTTPTask task = new OkHTTPTask(g, i);
		task.execute("http://steamnet.herokuapp.com/api/v1/sparks.json?limit="+lim);
		
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
				Spark[] sparks = parseData(data);
				
				JawnAdapter a = new JawnAdapter(gridView.getContext(), sparks, 200);
				Log.v("REPORT", "WE HAVE ACCESSED THE JAWNADAPTER AND ARE PROCEEDING AS PLANNED, SIR!");
				indexGrid.setAdapter(a);
				indexGrid.setSparks(sparks);
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
        	// Creating JSON Parser instance
        	JSONParser jParser = new JSONParser();
        	 
        	// getting JSON string from URL
        	JSONArray sparks = new JSONArray(data);
        	
        	ArrayList<Spark> sparkArrayList = new ArrayList<Spark>();
        	
        	try {        	     
        	    // looping through All Contacts
        	    for(int i = 0; i < sparks.length(); i++){
        	        JSONObject s = sparks.getJSONObject(i);
        	         
        	        // Storing each json item in variable
        	        String id = s.getString(ID);
        	        String sparkType = s.getString(SPARK_TYPE);
        	        String contentType = s.getString(CONTENT_TYPE);
        	        String content = s.getString(CONTENT);
        	        String createdAt = s.getString(CREATED_AT);
        	        
        	        sparkArrayList.add(new Spark(Integer.parseInt(id), sparkType.charAt(0), contentType.charAt(0), content, createdAt));
        	    }
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
