package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.Activities.IdeaDetailActivity;
import org.friendscentral.steamnet.BaseClasses.Comment;
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.json.parsers.JSONParser;
import com.squareup.okhttp.OkHttpClient;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class GetIdeaForDetail {
	int ideaId;
	IdeaDetailActivity ideaDetailActivity;
	ProgressDialog dialog;
	
	public GetIdeaForDetail(int id, IdeaDetailActivity ida) {
		Log.v("GetSparkForDetail", "Created new instance");
		ideaId = id;
		ideaDetailActivity = ida;
		new RetrieveIdea();
	}
	
	class RetrieveIdea extends AsyncTask<String, Void, Idea> {
		
		String TAG = "RetreiveDataTask";
		
		OkHttpClient client;
		
		public RetrieveIdea(){
			client = new OkHttpClient();
			dialog = new ProgressDialog(ideaDetailActivity);
			dialog.setTitle("Loading Idea data");
			dialog.show();
			this.execute("http://steamnet.herokuapp.com/api/v1/ideas/"+ideaId+".json");
		}
        
		
        @SuppressWarnings("unused")
		private Exception exception;
        
        protected Idea doInBackground(String... urls) {
            try {
            	String JSON = get(new URL(urls[0]));
            	Idea newIdea = parseData(JSON);
            	int index = 0;
            	for (Spark spark : newIdea.getSparks()) {
            		spark.setBitmap(getBitmapFromUrl(spark));
            		newIdea.setSpark(index, spark);
            		index++;
            	}
            	return newIdea;
            } catch (Exception e) {
                this.exception = e;
                Log.e(TAG, "Exception: "+e);
                Tracker myTracker = EasyTracker.getTracker(); 
                myTracker.sendException(e.getMessage(), false);
            }
            return null;
        }

        protected void onPostExecute(Idea idea) {
    		Log.v("GetIdeaForDetail - Retrieve Idea", "successfully retrieved idea");
			dialog.dismiss();
			ideaDetailActivity.initialize(idea);
        }
        
		Idea parseData(String data) throws JSONException {
        	//Ideas
			final String ID = "id";
        	final String DESCRIPTION = "description";
        	final String TAGS = "tags";
        	final String SPARKS = "sparks";
        	final String USER = "user";
        	//Sparks (currently unused)
        	final String SPARK_TYPE = "spark_type";
        	final String CONTENT_TYPE = "content_type";
        	final String CONTENT = "content";
        	final String CREATED_AT = "created_at";
        	final String COMMENTS = "comments";
        	final String COMMENT_TEXT = "comment_text";
        	final String NAME = "name";
        	final String FILE = "file";
        	 
        	// getting JSON string from URL
        	JSONObject json = new JSONObject(data);
        	 
    	    // Getting Idea parameters
    		int id = json.getInt(ID);
	        String description = json.getString(DESCRIPTION);
	        JSONArray tagsJSON = json.getJSONArray(TAGS);
	        String[] tags = new String[tagsJSON.length()];
	        for (int i = 0; i < tagsJSON.length(); i++) {
	        	tags[i] = tagsJSON.getString(i);
	        }
	        
	        //Getting Array of Sparks
    	    JSONArray sparksJSON = json.getJSONArray(SPARKS);
    	    Spark[] sparks = new Spark[sparksJSON.length()];
    	    for (int i = 0; i < sparksJSON.length(); i++) {
    	    	Log.v("Get idea for detail", "One spark retrieved");
    	        JSONObject s = sparksJSON.getJSONObject(i);
    	        
    	        int sparkId = s.getInt(ID);
    	        String sparkDate = s.getString(CREATED_AT);
    	        char sparkContentType = s.getString(CONTENT_TYPE).charAt(0);
    	        char sparkType = s.getString(SPARK_TYPE).charAt(0);
    	        String content = s.getString(CONTENT);
    	        
    	        Spark newSpark = new Spark(sparkId, sparkType, sparkContentType, content, sparkDate);
    	        if (s.has(FILE)) {
    	        	String fileUrl = s.getString(FILE);
    	        	newSpark.setCloudLink(fileUrl);
    	        }
    	        sparks[i] = newSpark;
    	    }
    	    //Getting Array of Users
    	    JSONObject firstUserJSON = json.getJSONObject(USER);
    	    String firstUser = firstUserJSON.getString(NAME);

    	    String firstCreatedAt = json.getString(CREATED_AT);
    	    
    	    JSONArray ideaComments = json.getJSONArray(COMMENTS);
    	    Comment[] comments = new Comment[ideaComments.length()];
    	    for (int i = 0; i < ideaComments.length(); i++) {
    	    	JSONObject c = ideaComments.getJSONObject(i);
    	    	int curCommentId = c.getInt(ID);
    	    	String curCommentContent = c.getString(COMMENT_TEXT);
    	    	JSONObject curCommentUser = c.getJSONObject(USER);
    	    	String curCommentUsername = curCommentUser.getString(NAME);
    	    	Comment newComment = new Comment(curCommentId, curCommentContent, curCommentUsername);
    	    	comments[i] = newComment;
    	    }
    	    
    	    return new Idea(id, description, tags, sparks, firstUser, firstCreatedAt, comments);   
        }
		
		public Bitmap getBitmapFromUrl(Spark spark) {
			if (spark.getBitmap() == null && spark.getContentType() != 'T' && spark.getContentType() != 'A') {
				if (spark.getCloudLink() != null) {
					try {
						String url = spark.getCloudLink();
						InputStream is = (InputStream) new URL(url).getContent();
						Bitmap bitmap = BitmapFactory.decodeStream(is);
						float aspectRatio = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
						Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (aspectRatio * 200), 200, true);
						return scaledBitmap;
					} catch (MalformedURLException e) {
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
