package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.json.parsers.JSONParser;
import com.squareup.okhttp.OkHttpClient;

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
	
	GridView gridview;
	IndexGrid indexgrid;
	
	/**
	 * @param id - int, ID of the Spark you want to get
	 */
	
	public RetrieveDataTaskGetSpark(int id, GridView g, IndexGrid i) {
		gridview = g;
		indexgrid = i;
		
		OkHTTPTask task = new OkHTTPTask(g, i);
		task.execute("http://steamnet.herokuapp.com/api/v1/sparks/"+id+".json");
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
				Log.v("LOOK HERE", "Still working");
				JawnAdapter j = indexGrid.getAdapter();
				Log.v("LOOK HERE", "And still working!");
				//Log.v("LOOK HERE", String.valueOf(j.getCount()));
				Jawn[] jawns = indexGrid.getJawns();
				Jawn[] newJawns = new Jawn[jawns.length + 1];
				for (int i = 0; i < jawns.length; i++) {
					newJawns[i] = jawns[i];
				}
				newJawns[jawns.length] = newSpark; 
				indexGrid.setJawns(newJawns);
				Log.v("LOOK RIGHT HERE", String.valueOf(j.getJawns().length));
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
				String createdAt = json.getString(CREATED_AT);
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
        	    
        	    
        	    String[] createdAts = new String[1];
        	    createdAts[0] = createdAt;
        	    
				Spark newSpark = new Spark(Integer.parseInt(id), sparkType.charAt(0), contentType.charAt(0), content, createdAts, createdAt, usersArray, firstUser);
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
