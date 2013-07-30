package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.SpinnerAdapter;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.squareup.okhttp.OkHttpClient;

/**
 * @author aqeelphillips
 * 
 */
public class GetXRandomJawns {
	char spark_type;
	char content_type;
	String content;
	String user;
	String[] tags;
	String tagsString;
	Context context;
	Activity activity;
	MainActivity mainActivity;
	Jawn[] jawns;
	
	STEAMnetApplication sna;
	
	/** 
	 * @param int X - returns the first X sparks (by createdAt)
	 */
	
	public GetXRandomJawns(int lim, GridView g, IndexGrid i, Context c, boolean sparks, boolean ideas) {
		Log.v("GetXJawns", "called");
		context = c;
		activity = (Activity) context;
		if (activity.getClass().getName().equals("org.friendscentral.steamnet.Activities.MainActivity")) {
			mainActivity = (MainActivity) activity;
		}
		
		sna = (STEAMnetApplication) context.getApplicationContext();
		Double randomSeed = Math.random();
		sna.setSeedNum(randomSeed);
		String url = "http://steamnet.herokuapp.com/api/v1/jawns.json?limit="+lim+"&lite=true&seed="+randomSeed;
		Log.v("RandomJawn fetch url:", url);
		
		if (sparks && !ideas) {
			url += "&filter=sparks";
		} else if (!sparks && ideas) {
			url += "&filter=ideas";
		}

		if (sna.getCurrentTask() != null) {
			sna.getCurrentTask().cancel(true);
			if (sna.getCurrentMultimediaTask() != null)
				sna.getCurrentMultimediaTask().cancel(true);
			if (sna.getCurrentUserTask() != null)
				sna.getCurrentUserTask().cancel(true);
		}
		
		i.getAdapter().setJawns(new Jawn[0]);
		g.setAdapter(new SpinnerAdapter(context, 16));
		
		OkHTTPTask task = new OkHTTPTask(g, i);
		task.execute(url);
		sna.setCurrentTask(task);
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
        	try {
				jawns = parseData(data);
				JawnAdapter a = new JawnAdapter(gridView.getContext(), jawns, 200);
				indexGrid.setAdapter(a);
				indexGrid.setJawns(jawns);
				if (mainActivity != null) {
					mainActivity.setSparkEventHandlers();
					mainActivity.setScrollListener();
				}
				Log.v("Thread count:", String.valueOf(Thread.activeCount()));
				new LoadMultimediaInBackground(indexGrid);
				Log.v("Thread count:", String.valueOf(Thread.activeCount()));
				new LoadUsersInBackground(indexGrid);
				Log.v("Thread count:", String.valueOf(Thread.activeCount()));
				//new decodeMultimedia();
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
        
        //PARSE DATA
        
		Jawn[] parseData(String data) throws JSONException {
			final String JAWN_TYPE = "jawn_type";
			final String ID = "id";
        	final String SPARK_TYPE = "spark_type";
        	final String CONTENT_TYPE = "content_type";
        	final String CONTENT = "content";
        	final String CREATED_AT = "created_at";
        	final String DESCRIPTION = "description";
        	final String FILE = "file";
        	
        	// getting JSON string from URL
        	JSONArray jawns = new JSONArray(data);
        	
        	ArrayList<Jawn> jawnArrayList = new ArrayList<Jawn>();
        	
        	try {        	     
        	    for (int i = 0; i < jawns.length(); i++) {
        	        JSONObject j = jawns.getJSONObject(i);
        	        
        	        if(j.getString(JAWN_TYPE).equals("spark")){
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
	
	class LoadMultimediaInBackground extends AsyncTask<IndexGrid, Void, Void> {
		
		public LoadMultimediaInBackground(IndexGrid indexGrid) {
			this.execute(indexGrid);
		}
		
		@Override
		protected Void doInBackground(IndexGrid... i) {
			new MultimediaLoader(i[0], i[0].getAdapter(), sna);
			return null;
		}
	}
	
	class LoadUsersInBackground extends AsyncTask<IndexGrid, Void, Void> {
		
		public LoadUsersInBackground(IndexGrid indexGrid) {
			this.execute(indexGrid);
		}
		
		@Override
		protected Void doInBackground(IndexGrid... i) {
			new UserLoader(i[0], i[0].getAdapter(), sna);
			return null;
		}
	}

}
