package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.Activities.SparkDetailActivity;
import org.friendscentral.steamnet.BaseClasses.Comment;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.json.parsers.JSONParser;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author aqeelphillips
 * 
 */
public class GetSparkForDetail {
	int sparkId;
	ProgressDialog dialog;
	SparkDetailActivity sparkDetailActivity;
	
	/** 
	 * @param int X - returns the first X sparks (by createdAt)
	 */
	
	public GetSparkForDetail(int id, SparkDetailActivity sda) {
		Log.v("GetSparkForDetail", "Created new instance");
		sparkId = id;
		sparkDetailActivity = sda;
		RetrieveSpark task = new RetrieveSpark();
		dialog = new ProgressDialog(sparkDetailActivity);
		dialog.setTitle("Loading Spark data");
		dialog.show();
		task.execute("http://steamnet.herokuapp.com/api/v1/sparks/"+sparkId+".json");
		
	}
	
	class RetrieveSpark extends AsyncTask<String, Void, String> {
		
		String TAG = "RetreiveDataTask";
		
		OkHttpClient client;
		
		public RetrieveSpark(){
			client = new OkHttpClient();
		}
        
		
        @SuppressWarnings("unused")
		private Exception exception;
        
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
        	try {
        		Log.v("GetSparkForDetail - Retrieve Spark", "successfully retrieved spark");
				Spark spark = parseData(data);
				new decodeMultimedia(spark);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        //PARSE DATA
        
		@SuppressWarnings("unused")
		Spark parseData(String data) throws JSONException {
        	final String ID = "id";
        	final String SPARK_TYPE = "spark_type";
        	final String CONTENT_TYPE = "content_type";
        	final String CONTENT = "content";
        	final String CREATED_AT = "created_at";
        	final String USERS = "users";
        	final String USER = "user";
        	final String USERNAME = "name";
        	final String COMMENTS = "comments";
        	final String COMMENT_TEXT = "comment_text";
        	final String NAME = "name";
        	final String FILE = "file";
        	// Creating JSON Parser instance
        	JSONParser jParser = new JSONParser();
        	
        	// getting JSON string from URL
        	JSONObject json = new JSONObject(data);
			String id = json.getString(ID);
			String sparkType = json.getString(SPARK_TYPE);
			String contentType = json.getString(CONTENT_TYPE);
			String content = json.getString(CONTENT);
			String createdAt = json.getString(CREATED_AT);
			String firstUser = "";
			//Getting Array of Users
    	    JSONArray usersJSON = json.getJSONArray(USERS);
	        	     
    	    // looping through All Users
    	    //ArrayList<Integer> usersArrayList = new ArrayList<Integer>();
    	    //int count = 0;
    	    /*for(int q = 0; q < usersJSON.length(); q++){
    	        JSONObject u = usersJSON.getJSONObject(q);
    	        // Storing each json item in variable
    	        if(count == 0){
    	        	count++;
    	        	firstUser = u.getString(USERNAME);
    	        }
    	        int userID = u.getInt(ID);
    	        usersArrayList.add(userID);
    	    }*/
    	    int[] usersArray = new int[1];
    	    // TODO DYNAMICALLY GET THE USER!!:
    	    usersArray[0] = 1;
    	    /*for(int q = 0; q < usersArrayList.size(); q++){
    	    	usersArray[q] = usersArrayList.get(q);
    	    }*/
    	    
    	    String[] createdAts = new String[1];
    	    createdAts[0] = createdAt;
	        	    //******************
	        	    
	        	    
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
    	    for(int j = 0; j < commentsArrayList.size(); j++){
    	    	commentArray[j] = commentsArrayList.get(j);
    	    }
	        	    
	        	    
	        	    //*****************************************
    	    Spark newSpark = new Spark(Integer.parseInt(id), sparkType.charAt(0), contentType.charAt(0), content, createdAts, createdAts[0], usersArray, firstUser, commentArray);
    	    if (contentType.charAt(0) != 'T') {
    	    	if (json.has(FILE)) {
    	    		if (json.getString(FILE) != null) {
        	    		String url = json.getString(FILE);
        	    		Log.v("Url of Spark's file attachment", url);
        	    		newSpark.setCloudLink(url);
    	    		}
    	    	}
    	    }
    	    return newSpark;
        }

		private class decodeMultimedia extends AsyncTask<String, Void, InputStream> {
			Spark spark;
			Bitmap bitmap;
			
			public decodeMultimedia(Spark s) {
				spark = s;
				this.execute();
			}
			
			@Override
			protected InputStream doInBackground(String... params) {
				InputStream is = null;
				char c = spark.getContentType();
				if (c == 'P') {
					Log.v("decodeMultimedia", c+" Spark contains a picture. Decoding image");
					if (spark.getCloudLink() != null) {
						try {
							String url = spark.getCloudLink();
							Log.v("Cloud link:", url);
							is = (InputStream) new URL(url).getContent();
							Bitmap bitmap = BitmapFactory.decodeStream(is);
							float aspectRatio = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
							Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (aspectRatio * 500), 500, true);
							spark.setBitmap(scaledBitmap);
							boolean bitmapExists = (spark.getBitmap() != null);
							Log.v("Bitmapexists?", String.valueOf(bitmapExists));
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else if (c == 'A') {
					Log.v("decodeMultimedia", "Spark contains audio. Doing nothing and decoding on runtime");
				}
				return is;
			}
			
			protected void onPostExecute(InputStream i) {
				Log.v("GetSparkForDetail - DecodeMultimedia", "Multimedia successfully decoded");
				dialog.dismiss();
				sparkDetailActivity.setFiller(spark);
			}
			
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
