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
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.squareup.okhttp.OkHttpClient;

public class UserLoader {
	IndexGrid indexgrid;
	JawnAdapter jAdapter;
	STEAMnetApplication sna;
	boolean snaExists;
	int incriment = 3;
	
	public UserLoader(IndexGrid ig, JawnAdapter j, STEAMnetApplication s) {
		indexgrid = ig;
		jAdapter = j;
		sna = s;
		snaExists = true;
		
		for (int i = 0; i < incriment; i++)
			loadMultimedia(i);
	}
	
	public UserLoader(IndexGrid ig, JawnAdapter j) {
		indexgrid = ig;
		jAdapter = j;
		snaExists = false;
		
		for (int i = 0; i < incriment; i++)
			loadMultimedia(i);
	}
	
	public void loadMultimedia(int pos) {
		if (pos < jAdapter.getJawns().length) {
			UserHelper u = new UserHelper(pos, UserLoader.this);
			if (snaExists)
				sna.setCurrentUserTask(u);
		}
	}
	
	public class UserHelper extends AsyncTask<String, Void, InputStream> {
		UserLoader uLoader;
		int position;
		Jawn jawn;
		Spark spark;
		Idea idea;
		Integer[] savedIndices = { null, null, null, null };
		ArrayList<String> ideaSparksJSON;
		ArrayList<Integer> unloadedThumbs;
		boolean edited = false;
		
		public UserHelper(int n, UserLoader m) {
			uLoader = m;
			position = n;
			jawn = jAdapter.getJawns()[n];
			this.execute();
		}
		
		@Override
		protected InputStream doInBackground(String... args) {
			final String USERS = "users";
			final String USER = "user";
			final String NAME = "name";
			final String ID = "id";
			
			if (jawn.getType() == 'S') {
				spark = jawn.getSelfSpark();
				if (spark.getUsername() == null) {
					String sparkUrl = "http://steamnet.herokuapp.com/api/v1/sparks/"+spark.getId()+".json";
					try {
						JSONObject sparkObj = new JSONObject(get(new URL(sparkUrl)));
						JSONArray sparkUsers = sparkObj.getJSONArray(USERS);
						if (sparkUsers.length() > 0) {
							if (sparkUsers.getJSONObject(0) != null) {
								JSONObject firstUserObj = sparkUsers.getJSONObject(0);
								spark.setUsername(firstUserObj.getString(NAME));
								
								ArrayList<Integer> userIds = new ArrayList<Integer>(sparkUsers.length());
								for (int i = 0; i < sparkUsers.length(); i++) {
									userIds.add(sparkUsers.getJSONObject(i).getInt(ID));
								}
								int[] userIdsArray = new int[userIds.size()];
								for (int i = 0; i < userIds.size(); i++) {
									userIdsArray[i] = userIds.get(i);
								}
								spark.setUserIds(userIdsArray);
								edited = true;
							}
						}
					} catch (MalformedURLException e) {
						Tracker myTracker = EasyTracker.getTracker(); 
		                myTracker.sendException(e.getMessage(), false);
						e.printStackTrace();
					} catch (JSONException e) {
						Tracker myTracker = EasyTracker.getTracker(); 
		                myTracker.sendException(e.getMessage(), false);
						e.printStackTrace();
					} catch (IOException e) {
						Tracker myTracker = EasyTracker.getTracker(); 
		                myTracker.sendException(e.getMessage(), false);
						e.printStackTrace();
					}
				}
			} else if (jawn.getType() == 'I') {
				idea = jawn.getSelfIdea();
				if (idea.getUsername() == null) {
					String ideaUrl = "http://steamnet.herokuapp.com/api/v1/ideas/"+idea.getId()+".json";
					try {
						JSONObject ideaObj = new JSONObject(get(new URL(ideaUrl)));
						JSONObject ideaUserJSON = ideaObj.getJSONObject(USER);
						idea.setUsername(ideaUserJSON.getString(NAME));
						
						idea.setUserId(ideaUserJSON.getInt(ID));
						edited = true;
					} catch (MalformedURLException e) {
						Tracker myTracker = EasyTracker.getTracker(); 
		                myTracker.sendException(e.getMessage(), false);
						e.printStackTrace();
					} catch (JSONException e) {
						Tracker myTracker = EasyTracker.getTracker(); 
		                myTracker.sendException(e.getMessage(), false);
						e.printStackTrace();
					} catch (IOException e) {
						Tracker myTracker = EasyTracker.getTracker(); 
		                myTracker.sendException(e.getMessage(), false);
						e.printStackTrace();
					}
				}
			}
			return null;
		}
		
		protected void onPostExecute(InputStream is) {
			if (edited) {
				if (spark != null && idea == null) 
					uLoader.setJawn(position, spark);
				else if (idea != null && spark == null)
					uLoader.setJawn(position, idea);
			}
			if (position + incriment < jAdapter.getJawns().length) {
				uLoader.loadMultimedia(position + incriment);
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
	}
	
	public void setJawn(int pos, Jawn jawn) {
		jAdapter.setJawn(pos, jawn);
		indexgrid.setJawns(jAdapter.getJawns());
		
		jAdapter.notifyDataSetChanged();
	}
}
