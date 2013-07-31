package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

/**
 * 
 * @author aqeelphillips
 *
 */

public class RefreshXJawns {
	GridView gridview;
	IndexGrid indexgrid;
	JawnAdapter jDapter;
	Context context;
	int limit;
	
	boolean sparks;
	boolean ideas;
	
	/**
	 * 
	 * @param lim - the amount of Jawns loaded at a time
	 * @param g - the GridView of the MainActivity
	 * @param i - the IndexGrid that is associated with the afformentioned GridView
	 * @param c - Activity context
	 * @param s - Whether the "Sparks" box in the Filter Settings sidebar section is checked
	 * @param is - Whether the "Ideas" box in the Filter Settings sidebar section is checked
	 */
	
	public RefreshXJawns(int lim, GridView g, IndexGrid i, Context c, boolean s, boolean is) {
		Log.v("RefreshXJawns", "called");
		
		gridview = g;
		indexgrid = i;
		jDapter = i.getAdapter();
		context = c;
		limit = lim;
		Log.v("RefreshXJawns:", "Limit executing: "+limit);
		
		sparks = s;
		ideas = is;
		
		STEAMnetApplication sna = (STEAMnetApplication) context.getApplicationContext();
		JawnFetcher fetcher = new JawnFetcher(0);
		sna.setCurrentTask(fetcher);
	}

	class JawnFetcher extends AsyncTask<String, Void, String> {
		int offset;
		
		public JawnFetcher(int o) {
			offset = o;
			String url = "http://steamnet.herokuapp.com/api/v1/jawns.json?limit="+limit+"&offset="+offset+"&lite=true";
			
			if (offset == 0) 
				url = "http://steamnet.herokuapp.com/api/v1/jawns.json?limit="+limit+"&lite=true";
			
			if (sparks && !ideas) {
				url += "&filter=sparks";
				Toast.makeText(context, "Retrieving new Sparks", Toast.LENGTH_LONG).show();
			} else if (!sparks && ideas) {
				url += "&filter=ideas";
				Toast.makeText(context, "Retrieving new Ideas", Toast.LENGTH_LONG).show();
			} else 
				Toast.makeText(context, "Retrieving new Sparks and Ideas", Toast.LENGTH_LONG).show();
			
			this.execute(url);
		}
		
		protected String doInBackground(String... urls) {
			try {
				return get(new URL(urls[0]));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String jsonString) {
			try {
				Jawn[] parsedJawns = parseData(jsonString);
				Jawn firstJawn = jDapter.getJawnAt(0);
				
				int jawnsToAdd = 0;
				for (Jawn j : parsedJawns) {
					if (j.getType() == 'I' && firstJawn.getType() == 'I') {
						if (((Idea) j).getId() == ((Idea) firstJawn).getId())
							break;
					} else if (j.getType() == 'S' && firstJawn.getType() == 'S') {
						if (((Spark) j).getId() == ((Spark) firstJawn).getId()) {
							Log.v("RefreshXJawns", "Found matching Spark");
							Log.v("RefreshXJawns", "Index of matching Spark: "+jawnsToAdd);
							break;
						}
					}
					jawnsToAdd++;
				}
				
				for (int i = 0; i < jawnsToAdd; i++) {
					jDapter.addAtPosition(parsedJawns[i], i);
					Log.v("RefreshXJawns:", "Length of the Adapter's array: "+jDapter.getJawns().length);
				}
				indexgrid.setJawnsWithCaching(jDapter.getJawns());
				new MultimediaLoader(indexgrid, jDapter);
				new UserLoader(indexgrid, jDapter);
				MainActivity ma = (MainActivity) context;
				if (ma != null) {
					ma.setSparkEventHandlers();
					ma.setScrollListener();
				}
				
				if (jawnsToAdd == limit) {
					STEAMnetApplication sna = (STEAMnetApplication) context.getApplicationContext();
					JawnFetcher fetcher = new JawnFetcher(limit + offset);
					sna.setCurrentTask(fetcher);
				} else {
					if (offset == 0 && jawnsToAdd == 0)
						Toast.makeText(context, "No new Sparks or Ideas to fetch", Toast.LENGTH_LONG).show();
					Log.v("RefreshXJawns", "Finished refreshing!");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		String get(URL url) throws IOException {
			HttpURLConnection connection = new OkHttpClient().open(url);
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
        
        Jawn[] parseData(String data) throws JSONException {
			final String JAWN_TYPE = "jawn_type";
			final String ID = "id";
        	final String SPARK_TYPE = "spark_type";
        	final String CONTENT_TYPE = "content_type";
        	final String CONTENT = "content";
        	final String CREATED_AT = "created_at";
        	final String DESCRIPTION = "description";
        	final String FILE = "file";
        	
        	JSONArray jawns = new JSONArray(data);
        	
        	ArrayList<Jawn> jawnArrayList = new ArrayList<Jawn>();
        	
        	try {        	     
        	    for (int i = 0; i < jawns.length(); i++) {
        	        JSONObject j = jawns.getJSONObject(i);
        	        
        	        if (j.getString(JAWN_TYPE).equals("spark")) {
        	        	
    					String id = j.getString(ID);
    					String sparkType = j.getString(SPARK_TYPE);
    					String contentType = j.getString(CONTENT_TYPE);
    					String content = j.getString(CONTENT);
    					String createdAt = j.getString(CREATED_AT);

    	        	    Spark newSpark = new Spark(Integer.parseInt(id), sparkType.charAt(0), contentType.charAt(0), content, createdAt);
    	        	    if (contentType.charAt(0) != 'T') {
    	        	    	if (j.has(FILE)) {
    	        	    		if (j.getString(FILE) != null) {
	    	        	    		String url = j.getString(FILE);
	    	        	    		newSpark.setCloudLink(url);
    	        	    		}
    	        	    	}
    	        	    }
	        	        jawnArrayList.add(newSpark);
	        	        
        	        } else if (j.getString(JAWN_TYPE).equals("idea")) {
        	        	
                		int id = j.getInt(ID);
            	        String description = j.getString(DESCRIPTION);
                	    String createdAt = j.getString(CREATED_AT);

                	    jawnArrayList.add(new Idea(id, description, createdAt));
                	    
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
	}
}
