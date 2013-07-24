package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Comment;
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.json.parsers.JSONParser;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author SamBeckley
 * 
 */
public class GetXJawns {
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
	
	/** 
	 * @param int X - returns the first X sparks (by createdAt)
	 */
	
	public GetXJawns(int lim, GridView g, IndexGrid i, Context c) {
		Log.v("GetXJawns", "called");
		context = c;
		activity = (Activity) context;
		if (activity.getClass().getName().equals("org.friendscentral.steamnet.Activities.MainActivity")) {
			mainActivity = (MainActivity) activity;
		}
		
		OkHTTPTask task = new OkHTTPTask(g, i);
		task.execute("http://steamnet.herokuapp.com/api/v1/jawns.json?limit="+lim);
		
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
        	Log.v("REPORT", "JAAAAAAAAAAAWN");
        	Log.v("REPORT", "WE HAVE MOVED INTO THE POST EXECUTE PHASE, SIR!");
        	try {
        		Log.v("REPORT", "WE WILL BEGIN TO PARSE THE DATA, SIR!");
				jawns = parseData(data);
				JawnAdapter a = new JawnAdapter(gridView.getContext(), jawns, 200);
				Log.v("REPORT", "WE HAVE ACCESSED THE JAWNADAPTER AND ARE PROCEEDING AS PLANNED, SIR!");
				indexGrid.setAdapter(a);
				indexGrid.setJawns(jawns);
				if (mainActivity != null) {
					mainActivity.setSparkEventHandlers();
					mainActivity.setScrollListener();
				}
				new MultimediaLoader(indexGrid, a);
				//new decodeMultimedia();
			} catch (JSONException e) {
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
        	final String COMMENTS = "comments";
        	final String COMMENT_TEXT = "comment_text";
        	final String NAME = "name";
        	final String FILE = "file";
        	
        	// Creating JSON Parser instance
        	JSONParser jParser = new JSONParser();
        	 
        	// getting JSON string from URL
        	JSONArray jawns = new JSONArray(data);
        	
        	ArrayList<Jawn> jawnArrayList = new ArrayList<Jawn>();
        	
        	try {        	     
        	    // looping through All Jawns
        	    for(int i = 0; i < jawns.length(); i++){
        	        JSONObject j = jawns.getJSONObject(i);
        	        
        	        //checking to see if the Jawn is a Spark or an Idea
        	        if(j.getString(JAWN_TYPE).equals("spark")){
    					String id = j.getString(ID);
    					String sparkType = j.getString(SPARK_TYPE);
    					String contentType = j.getString(CONTENT_TYPE);
    					String content = j.getString(CONTENT);
    					String createdAt = j.getString(CREATED_AT);
    					String firstUser = "";    	        	     
    	        	    
    					
    					// TODO This is NOT dynamic:
    	        	    int[] usersArray = new int[1];
    	        	    usersArray[0] = 1;
    	        	    
    	        	    
    	        	    JSONArray commentsJSON = j.getJSONArray(COMMENTS);
    	        	    
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
    	        	    
    	        	    String[] createdAts = new String[1];
    	        	    createdAts[0] = createdAt;
    	        	    
    	        	    Spark newSpark = new Spark(Integer.parseInt(id), sparkType.charAt(0), contentType.charAt(0), content, createdAts, createdAts[0], usersArray, "max", commentArray);
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
        	        	
        	        	// Getting Idea parameters
                		int id = j.getInt(ID);
            	        String description = j.getString(DESCRIPTION);
            	        //String tags = j.getString(TAGS);
            	        
            	        //Getting Array of Sparks
                	    JSONArray sparksJSON = j.getJSONArray(SPARKS);
                	     
                	    // looping through All Sparks
                	    ArrayList<Integer> sparkIdArrayList = new ArrayList<Integer>();
                	    for(int c = 0; c < sparksJSON.length(); c++){
                	        JSONObject s = sparksJSON.getJSONObject(c);
                	        // Storing each json item in variable
                	        int sparkId = s.getInt(ID);
                	        sparkIdArrayList.add(sparkId);
                	    }
                	    
                	    int[] sparkIdArray = new int[sparkIdArrayList.size()];
                	    Log.v("Spark Id Array:", sparkIdArrayList.toString());
                	    
                	    for(int w = 0; w < sparkIdArrayList.size(); w++){
                	    	sparkIdArray[w] = sparkIdArrayList.get(w);
                	    }
                	    
                	    String firstUser = "";
                	    
                	    int[] userIds = new int[10];
                	    userIds[0] = 1;
                	   
                	    
                	    JSONArray commentsJSON = j.getJSONArray(COMMENTS);
    	        	    
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
                	    
                	    String createdAt = j.getString(CREATED_AT);
                	    
                	    String[] tags = new String[10];
                	    
                	    Idea idea = new Idea(id, description, tags, sparkIdArray, userIds, user, createdAt, commentArray);
                	    idea.setBitmaps(new Bitmap[sparkIdArray.length]);
                	    jawnArrayList.add(idea);
                	    
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
		
		/*private class decodeMultimedia extends AsyncTask<String, Void, InputStream> {
			Bitmap bitmap;
			
			public decodeMultimedia() {
				this.execute();
			}
			
			@Override
			protected InputStream doInBackground(String... params) {
				InputStream is = null;
				for (int i = 0; i < jawns.length; i++) {
					if (jawns[i].getType() == 'S') {
						Spark spark = jawns[i].getSelfSpark();
						char c = spark.getContentType();
						if (c == 'P' || c == 'V' || c == 'L' || c == 'C') {
							Log.v("decodeMultimedia", c+" Spark contains a picture. Decoding image");
							if (spark.getCloudLink() != null) {
								try {
									String url = spark.getCloudLink();
									Log.v("Cloud link:", url);
									is = (InputStream) new URL(url).getContent();
									Bitmap bitmap = BitmapFactory.decodeStream(is);
									float aspectRatio = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
									Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (aspectRatio * 200), 200, true);
									Log.v("Width:", String.valueOf((int) (aspectRatio * 200)));
									spark.setBitmap(scaledBitmap);
								} catch (MalformedURLException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						} else if (c == 'A') {
							Log.v("decodeMultimedia", "Spark contains audio. Doing nothing and decoding on runtime");
						}
					}
				}
				return is;
			}
			
			protected void onPostExecute(InputStream i) {
				JawnAdapter a = new JawnAdapter(gridView.getContext(), jawns, 200);
				Log.v("REPORT", "WE HAVE ACCESSED THE JAWNADAPTER AND ARE PROCEEDING AS PLANNED, SIR!");
				indexGrid.setAdapter(a);
				indexGrid.setJawns(jawns);
				if (mainActivity != null) {
					mainActivity.setSparkEventHandlers();
					mainActivity.setScrollListener();
				}	
			}
			
		}*/
        
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
