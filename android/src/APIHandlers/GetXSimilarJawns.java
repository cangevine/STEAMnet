package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.SpinnerAdapter;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author aqeelphillips
 * 
 */
public class GetXSimilarJawns {
	char spark_type;
	char content_type;
	String content;
	String user;
	String[] tags;
	String tagsString;
	Context context;
	Activity activity;
	MainActivity mainActivity;
	
	int limit;
	
	Jawn[] savedJawns;
	JawnAdapter savedAdapter;
	
	STEAMnetApplication sna;
	
	/** 
	 * @param int X - returns the first X sparks (by createdAt)
	 */
	
	public GetXSimilarJawns(int lim, GridView g, IndexGrid i, Context c, String[] tags) {
		Log.v("GetXSimilarJawns", "called");
		context = c;
		activity = (Activity) context;
		if (activity.getClass().getName().equals("org.friendscentral.steamnet.Activities.MainActivity")) {
			mainActivity = (MainActivity) activity;
		}
		
		String[] urls = new String[tags.length];
		int index = 0;
		for (String tag : tags) {
			String url = "http://steamnet.herokuapp.com/api/v1/tags/"+tag+".json?limit="+lim+"&lite=true";
			urls[index] = url;
			index++;
		}
		
		sna = (STEAMnetApplication) context.getApplicationContext();
		if (sna.getCurrentTask() != null) {
			sna.getCurrentTask().cancel(true);
			if (sna.getCurrentMultimediaTask() != null)
				sna.getCurrentMultimediaTask().cancel(true);
			if (sna.getCurrentUserTask() != null)
				sna.getCurrentUserTask().cancel(true);
		}
		
		limit = lim;
		
		OkHTTPTask task = new OkHTTPTask(g, i, urls);
		task.execute();
		sna.setCurrentTask(task);
	}
	
	class OkHTTPTask extends AsyncTask<String, Void, String[]> {
		
		String TAG = "RetreiveDataTask";
		
		OkHttpClient client;
		GridView gridView;
		IndexGrid indexGrid;
		String[] urls;
		
		public OkHTTPTask(GridView g, IndexGrid i, String[] u){
			client = new OkHttpClient();
			gridView = g;
			indexGrid = i;
			urls = u;
		}
        
		
		@SuppressWarnings("unused")
		private Exception exception;
        
        protected String[] doInBackground(String... s) {
            try {
            	ArrayList<String> dataList = new ArrayList<String>(urls.length);
            	for (String url : urls) {
            		dataList.add(get(new URL(url)));
            	}
            	
            	String[] dataArr = new String[dataList.size()];
            	int index = 0;
            	for (String data : dataList) {
            		dataArr[index] = data;
            		index++;
            	}
            	return dataArr;
            } catch (Exception e) {
                this.exception = e;
                Log.e(TAG, "Exception: "+e);
                Tracker myTracker = EasyTracker.getTracker(); 
                myTracker.sendException(e.getMessage(), false);
                return null;
            }
        }

        @SuppressWarnings("unused")
		protected void onPostExecute(String[] data) {
        	try {
        		//Get ALL jawns
        		ArrayList<Jawn> rawJawns = new ArrayList<Jawn>();
        		int index = 0;
        		for (String json : data) {
        			Jawn[] parsedData = parseData(json);
        			for (Jawn jawn : parsedData)
        				rawJawns.add(jawn);
        			index++;
        		}
        		
        		// Remove blanks and duplicates 
        		ArrayList<Jawn> jawns = new ArrayList<Jawn>();
        		for (int i = 0; i < rawJawns.size(); i++) {
					if (rawJawns.get(i) != null) {
						Jawn newJawn = rawJawns.get(i);
						//Verify that it's not in the List already:
						if (!checkForJawnInArray(newJawn, jawns)) {
							jawns.add(newJawn);
						}
					}
        		}
        		
        		//And randomize the list:
        		Collections.shuffle(jawns);
        		
        		//Turn ArrayList to Array
        		Jawn[] completeJawnArr = new Jawn[jawns.size()];
        		for (int i = 0; i < jawns.size(); i++) {
        			completeJawnArr[i] = jawns.get(i);
        		}
        		
				if (completeJawnArr != null) {
					JawnAdapter a = new JawnAdapter(gridView.getContext(), completeJawnArr, 200);
					indexGrid.setAdapter(a);
					indexGrid.setJawns(completeJawnArr);
				} else {
					Toast.makeText(context, "No matches found for those tags", Toast.LENGTH_LONG).show();
				}
				
				if (mainActivity != null) {
					mainActivity.setSparkEventHandlers();
					mainActivity.setScrollListener();
				}
				new LoadMultimediaInBackground(indexGrid);
				new LoadUsersInBackground(indexGrid);
			} catch (JSONException e) {
				Tracker myTracker = EasyTracker.getTracker(); 
                myTracker.sendException(e.getMessage(), false);
				e.printStackTrace();
			}
        }
        
        boolean checkForJawnInArray(Jawn jawn, ArrayList<Jawn> jawnsArr) {
        	for (Jawn compareJawn : jawnsArr) {
        		if (compareJawn.getType() == 'S' && jawn.getType() == 'S') {
        			if (((Spark) compareJawn).getId() == ((Spark) jawn).getId()) {
        				return true;
        			}
        		} else if (compareJawn.getType() == 'I' && jawn.getType() == 'I') {
        			if (((Idea) compareJawn).getId() == ((Idea) jawn).getId()) {
        				return true;
        			}
        		}
        	}
        	return false;
        }
        
        //PARSE DATA
        
		Jawn[] parseData(String data) throws JSONException {
			final String JAWNS = "jawns";
			final String TAG_TEXT = "tag_text";
			final String JAWN_TYPE = "jawn_type";
			final String ID = "id";
        	final String SPARK_TYPE = "spark_type";
        	final String CONTENT_TYPE = "content_type";
        	final String CONTENT = "content";
        	final String CREATED_AT = "created_at";
        	final String DESCRIPTION = "description";
        	final String FILE = "file";

        	/*if (data.equals("{\"status\":\"500\",\"error\":\"Internal Server Error\"}")) {
        		return null;
        	}*/
        	
        	if (data.equals("")) {
        		return null;
        	}
        	
        	try {
        		JSONObject tagJSON = new JSONObject(data);
        		
        		if (tagJSON.has(TAG_TEXT)) {
        			JSONArray jawns = tagJSON.getJSONArray(JAWNS);
        			ArrayList<Jawn> jawnArrayList = new ArrayList<Jawn>();
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
        		}
        	} catch (JSONException e) {
        		Tracker myTracker = EasyTracker.getTracker(); 
                myTracker.sendException(e.getMessage(), false);
        	    e.printStackTrace();
        	}
        	return null;
        }
        
		String get(URL url) {
			HttpURLConnection connection = client.open(url);
			InputStream in = null;
			try {
				// Read the response.
				in = connection.getInputStream();
				byte[] response = readFully(in);
				return new String(response, "UTF-8");
          	} catch (IOException e) {
          		Tracker myTracker = EasyTracker.getTracker(); 
                myTracker.sendException(e.getMessage(), false);
				return "";
			} finally {
          		if (in != null)
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
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
	
	public JawnAdapter getSavedAdapter() {
		return savedAdapter;
	}
	
	public Jawn[] getSavedJawns() {
		return savedJawns;
	}

}
