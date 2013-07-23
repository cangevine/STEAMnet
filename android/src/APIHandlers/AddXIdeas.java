package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.BaseClasses.Comment;
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.EventHandlers.EndlessScroller;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.json.parsers.JSONParser;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author SamBeckley
 * 
 */
public class AddXIdeas {
	char spark_type;
	char content_type;
	String content;
	String user;
	String[] tags;
	String tagsString;
	int currentTotal;
	int limit;
	
	EndlessScroller endlessScroller;
	
	/** 
	 * @param int X - returns the first X ideas (by createdAt)
	 */
	
	public AddXIdeas(int lim, GridView g, IndexGrid i, int curTotal, EndlessScroller es) {
		limit = lim;
		currentTotal = curTotal;
		endlessScroller = es;
		
		Log.v("REPORT", "GET X IDEAS IS BEGGINING, SIR!");
		OkHTTPTask task = new OkHTTPTask(g, i);
		task.execute("http://steamnet.herokuapp.com/api/v1/ideas.json?limit="+(limit+currentTotal));
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
        	try {
        		Log.v("REPORT", "WE WILL BEGIN TO PARSE THE DATA, SIR!");
				Jawn[] jawns = parseData(data);
				Log.v("JAWNS", Integer.toString(jawns.length));
				JawnAdapter a = indexGrid.getAdapter();
				Log.v("REPORT", "WE HAVE ACCESSED THE JAWNADAPTER AND ARE PROCEEDING AS PLANNED, SIR!");
				for (int i = limit; i < jawns.length; i++) {
					a.addAtPosition(jawns[i], a.getJawns().length);
				}
				a.notifyDataSetChanged();
				new MultimediaLoader(indexGrid, a);
				indexGrid.setJawns(a.getJawns());
				endlessScroller.doneRefreshing();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        //PARSE DATA
        
		@SuppressWarnings("unused")
		Jawn[] parseData(String data) throws JSONException {
			Log.v("REPORT", "WE ARE PARSING THE DATA, SIR!");
			final String JAWN_TYPE = "jawn_type";
			final String ID = "id";
        	final String SPARK_TYPE = "spark_type";
        	final String CONTENT_TYPE = "content_type";
        	final String CONTENT = "content";
        	final String CREATED_AT = "created_at";
        	final String CREATED_ATS = "created_ats";
        	final String UPDATED_AT = "updated_at";
        	final String USERS = "users";
        	final String USER = "user";
        	final String USERNAME = "name";
        	final String DESCRIPTION = "description";
        	final String TAGS = "tags";
        	final String SPARKS = "sparks";
        	final String USER_ID = "user_id";
        	final String COMMENTS = "comments";
        	final String COMMENT_TEXT = "comment_text";
        	final String NAME = "name";
        	// Creating JSON Parser instance
        	JSONParser jParser = new JSONParser();
        	 
        	// getting JSON string from URL
        	JSONArray ideasJSON = new JSONArray(data);
        	
        	ArrayList<Jawn> jawnArrayList = new ArrayList<Jawn>();
        	Log.v("LENGTH", Integer.toString(ideasJSON.length()));
        	try {        	     
        		for(int i = 0; i < ideasJSON.length(); i++){// Storing each json item in variable
        			JSONObject json = ideasJSON.getJSONObject(i);
        			// Getting Idea parameters
            		int id = json.getInt(ID);
        	        String description = json.getString(DESCRIPTION);
        	        //String tags = j.getString(TAGS);
        	        
        	        //Getting Array of Sparks
            	    JSONArray sparksJSON = json.getJSONArray(SPARKS);
            	    Log.v("STUFF", sparksJSON.toString()); 
            	    Log.v("ideasJSON Length", Integer.toString(sparksJSON.length()));
            	    // looping through All Sparks
            	    ArrayList<Integer> sparkIdArrayList = new ArrayList<Integer>();
            	    for(int c = 0; c < sparksJSON.length(); c++){
            	        JSONObject s = sparksJSON.getJSONObject(c);
            	        // Storing each json item in variable
            	        int sparkId = s.getInt(ID);
            	        sparkIdArrayList.add(sparkId);
            	    }
            	    
            	    Log.v("YOMAMA", sparkIdArrayList.toString());
            	    int[] sparkIdArray = new int[sparkIdArrayList.size()];
            	    
            	    for(int w = 0; w < sparkIdArrayList.size(); w++){
            	    	sparkIdArray[w] = sparkIdArrayList.get(w);
            	    }
            	    
            	    String firstUser = "";
            	    
            	    //int userId = json.getInt(USER_ID);
            	    int[] userIds = new int[10];
            	    userIds[0] = 1;
            	    
            	    JSONArray commentsJSON = json.getJSONArray(COMMENTS);
	        	    
	        	    ArrayList<Comment> commentsArrayList = new ArrayList<Comment>();
	        	    for(int k = 0; k < commentsJSON.length(); k++){
	        	    	JSONObject c = commentsJSON.getJSONObject(k);
	        	    	String commentText = c.getString(COMMENT_TEXT);
	        	    	JSONObject userObj = c.getJSONObject(USER);
	        	    	String userId = userObj.getString(ID);
	        	    	String username = userObj.getString(NAME);

	        	    	commentsArrayList.add(new Comment(Integer.valueOf(userId), commentText, username));
	        	    }
	        	    
	        	    Comment[] commentArray = new Comment[commentsArrayList.size()];
	        	    for(int w = 0; w < commentsArrayList.size(); w++){
	        	    	commentArray[w] = commentsArrayList.get(w);
	        	    }
            	    
            	    String createdAt = json.getString(CREATED_AT);
            	    
            	    String[] tags = new String[10];
            	    
            	    
            	    jawnArrayList.add(new Idea(id, description, tags, sparkIdArray, userIds, user, createdAt, commentArray));
            	    
        		}
        	    Log.v("ARRAY", jawnArrayList.toString());
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

}
