package APIHandlers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.GridView;

import com.json.parsers.JSONParser;
import com.squareup.okhttp.OkHttpClient;

public class PostSpark {
	Context context;
	
	Spark spark;
	char spark_type;
	char content_type;
	String content;
	String user;
	String[] tags;
	String tagsString;
	String token;
	
	GridView gridView;
	IndexGrid indexGrid;
	JawnAdapter adapter;
	
	/**
	 * @param (char, char, String, GridView, IndexGrid)
	 * @param char - Spark Type
	 * @param ct - Content Type
	 * @param c - Content
	 * @param g - GridView
	 * @param i - IndexView
	 */
	public PostSpark(Spark s, GridView g, IndexGrid i, String un, String tok, Context c) {
		context = c;
		
		spark = s;
		spark_type = spark.getSparkType();
		content_type = spark.getContentType();
		content = spark.getContent();
		gridView = g;
		indexGrid = i;
		user = un;
		token = tok;
		adapter = indexGrid.getAdapter();
		
		tagsString = spark.getTagsString();
		
		Log.v("TAGS", tagsString);
		
		OkHTTPTask task = new OkHTTPTask();
		task.execute("http://steamnet.herokuapp.com/api/v1/sparks.json");
	}
	
	@SuppressWarnings("unused")
	class OkHTTPTask extends AsyncTask<String, Void, String> {
	
		String TAG = "RetreiveDataTask";
		
	    OkHttpClient client = new OkHttpClient();
		
	    private Exception exception;
	    
	    @Override
	    protected String doInBackground(String... urls) {
	        try {
	        	
	        	/*
	        	 * BELOW ME IS THE STRING TO EDIT, READ THE README ON GITHUB
	        	 * RIGHT HERE!
	        	 * LOOK! ITS RIGHT THERE!
	        	 */
	        	

	        	String data = "";
        		Log.v("PostSpark", "Submitting multimedia");
        		data = postMultimedia(urls[0]);
	        	return data;
	        	
	        } catch (Exception e) {
	            this.exception = e;
	            Log.e(TAG, "Exception: "+e);
	            return null;
	        }
	    }
	
	    protected void onPostExecute(String data) {
	    	Log.d(TAG, "=> "+data);
	    	try {
				Spark newSpark = parseData(data);
				indexGrid.addJawn(newSpark);
				new MultimediaLoader(indexGrid, adapter);
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    }
	    
	    Spark parseData(String data) throws JSONException {
			Log.v("TEST", "BEGINNING TO PARSE DATA");
        	final String ID = "id";
        	final String SPARK_TYPE = "spark_type";
        	final String CONTENT_TYPE = "content_type";
        	final String CONTENT = "content";
        	final String CREATED_AT = "created_at";
        	final String UPDATED_AT = "updated_at";
        	final String USERS = "users";
        	final String USER = "user";
        	final String USERNAME = "name";
        	final String FILE = "file";
        	// Creating JSON Parser instance
        	JSONParser jParser = new JSONParser();
        	 
        	// getting JSON string from URL
        	JSONObject json = new JSONObject(data);
        	 
        	try {
				// Storing each json item in variable
				int id = json.getInt(ID);
				char sparkType = json.getString(SPARK_TYPE).charAt(0);
				char contentType = json.getString(CONTENT_TYPE).charAt(0);
				String content = json.getString(CONTENT);
				String createdAt = json.getString(CREATED_AT);
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
        	    
				Spark newSpark = new Spark(id, sparkType, contentType, content, createdAts, usersArray, firstUser);
				if (json.has(FILE)) {
        	    	String cloudUrl = json.getString(FILE);
        	    	newSpark.setCloudLink(cloudUrl);
        	    }
				return newSpark;
        	} catch (JSONException e) {
        	    e.printStackTrace();
        	}
        	return null;
        }
	    
	    //I think this method can be deleted
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
	    
	    /*
	     * TRYING TO MAKE POST FROM HERE DOWN
	     */
	    
	    /*String post(URL url, byte[] body) throws IOException {
	        HttpURLConnection connection = client.open(url);
	        OutputStream out = null;
	        InputStream in = null;
	        try {
	          // Write the request.
	          Log.v("TEST", "Writing the Request");
	          connection.setRequestMethod("POST");
	          out = connection.getOutputStream();
	          out.write(body);
	          Log.v("TEST", "Closing the Request");
	          out.close();
	
	          // Read the response.
	          if (connection.getResponseCode() != HttpURLConnection.HTTP_OK && connection.getResponseCode() != 201) {
	        	Log.v("TEST", "GOT AN ERROR!!!!!!");
	            throw new IOException("Unexpected HTTP response: "
	                + connection.getResponseCode() + " " + connection.getResponseMessage());
	          }
	          in = connection.getInputStream();
	          Log.v("TEST", "Reading the first line...");
	          return readFirstLine(in);
	        } finally {
	          // Clean up.
	          if (out != null) out.close();
	          if (in != null) in.close();
	        }
	      }*/
	    
	    String readFirstLine(InputStream in) throws IOException {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	        return reader.readLine();
	      }
	    /*
	     * END ATTEMPT TO MAKE POST
	     */
	    
	    //I think this method can be deleted
	    byte[] readFully(InputStream in) throws IOException {
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        for (int count; (count = in.read(buffer)) != -1; ) {
	          out.write(buffer, 0, count);
	        }
	        return out.toByteArray();
	      }
	    
	    public String postMultimedia(String url) throws IOException {
	    	HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
	    	MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	    	
	    	FileBody fileBody = null;
	    	File tmpFile = null;
	    	
	    	switch (content_type) {
	    	case 'P':
	    		Log.v("PostSpark", "PostMultimedia called - Spark is a picture");
	    		File pictureSampleDir = Environment.getExternalStorageDirectory();
	    		Log.v("Path of temp file:", pictureSampleDir.getAbsolutePath());
		    	File pictureFile = File.createTempFile("temp_picture_thumbnail", ".jpeg", pictureSampleDir);
		    	FileOutputStream pictureStream = new FileOutputStream(pictureFile);
		    	Bitmap picture = spark.getBitmap();
		    	picture.compress(Bitmap.CompressFormat.JPEG, 70, pictureStream);
		    	pictureStream.close();
		    	pictureStream.flush();
		        fileBody = new FileBody(pictureFile, "image/jpeg");
		        multipartEntity.addPart("spark[file]", fileBody);
		        tmpFile = pictureFile;
	    		break;
	    	case 'A':
	    		Log.v("PostSpark", "PostMultimedia called - Spark is audio");
	    		String audioPath = getRealPathFromURI(spark.getUri());
		        File audioFile = new File(audioPath);
		        fileBody = new FileBody(audioFile, "audio/mpeg");
		        multipartEntity.addPart("spark[file]", fileBody);
		        tmpFile = audioFile;
	    		break;
	    	case 'C':
	    		Log.v("PostSpark", "PostMultimedia called - Spark is a code snippet");
	    		File codeSampleDir = Environment.getExternalStorageDirectory();
		    	File codeFile = File.createTempFile("temp_gist_thumbnail", ".jpeg", codeSampleDir);
		    	FileOutputStream codeStream = new FileOutputStream(codeFile);
		    	Bitmap codeScreenshot = spark.getBitmap();
		    	codeScreenshot.compress(Bitmap.CompressFormat.JPEG, 100, codeStream);
		    	codeStream.close();
		    	codeStream.flush();
		        fileBody = new FileBody(codeFile, "image/jpeg");
		        multipartEntity.addPart("spark[file]", fileBody);
		        tmpFile = codeFile;
	    		break;
	    	case 'L':
	    		Log.v("PostSpark", "PostMultimedia called - Spark is a Link");
	    		File linkSampleDir = Environment.getExternalStorageDirectory();
		    	File linkFile = File.createTempFile("temp_link_screenshot", ".jpeg", linkSampleDir);
		    	FileOutputStream linkStream = new FileOutputStream(linkFile);
		    	Bitmap linkThumbnail = spark.getBitmap();
		    	linkThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, linkStream);
		    	linkStream.close();
		    	linkStream.flush();
		        fileBody = new FileBody(linkFile, "image/jpeg");
		        multipartEntity.addPart("spark[file]", fileBody);
		        tmpFile = linkFile;
	    		break;
	    	case 'V':
	    		Log.v("PostSpark", "PostMultimedia called - Spark is a video");
	    		File videoSampleDir = Environment.getExternalStorageDirectory();
		    	File videoFile = File.createTempFile("temp_youtube_thumbnail", ".jpeg", videoSampleDir);
		    	FileOutputStream videoStream = new FileOutputStream(videoFile);
		    	Bitmap videoThumbnail = spark.getBitmap();
		    	videoThumbnail.compress(Bitmap.CompressFormat.JPEG, 50, videoStream);
		    	videoStream.close();
		    	videoStream.flush();
		        fileBody = new FileBody(videoFile, "image/jpeg");
		        multipartEntity.addPart("spark[file]", fileBody);
		        tmpFile = videoFile;
	    		break;
	    	}
	        StringBody sparkTypeBody = new StringBody(String.valueOf(spark_type));
	        StringBody contentTypeBody = new StringBody(String.valueOf(content_type));
	        StringBody contentBody = new StringBody(content);
	        StringBody usernameBody = new StringBody(user);
	        StringBody tagsBody = new StringBody(tagsString);
	        StringBody tokenBody = new StringBody(token);
	        multipartEntity.addPart("spark[spark_type]", sparkTypeBody);
	        multipartEntity.addPart("spark[content_type]", contentTypeBody);
	        multipartEntity.addPart("spark[content]", contentBody);
	        multipartEntity.addPart("username", usernameBody);
	        multipartEntity.addPart("tags", tagsBody);	
	        multipartEntity.addPart("token", tokenBody);
	        
	        httpPost.setEntity(multipartEntity);
	        try {
				HttpResponse response = httpClient.execute(httpPost, localContext);
				InputStream is = response.getEntity().getContent();
				if (tmpFile != null) {
					tmpFile.delete();
				}
				return readFirstLine(is);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        if (tmpFile != null) {
				tmpFile.delete();
			}
	    	return null;
	    }
	    
	 }
	
	public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
		Cursor cursor = ((Activity) context).managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
