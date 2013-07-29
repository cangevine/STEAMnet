package APIHandlers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.friendscentral.steamnet.CommentAdapter;
import org.friendscentral.steamnet.BaseClasses.Comment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

public class PostComment{
	ProgressDialog dialog;
	int commentableId;
	char commentableTypeChar;
	String commentableType;
	String commentText;
	String username;
	int userID;
	String token;
	CommentAdapter commentAdapter;
	
	public PostComment(int id, char t, String c, int userID, String u, String tok, CommentAdapter ca) {
		commentableId = id;
		commentableTypeChar = t;
		commentText = c;
		commentableType = "";
		username = u;
		token = tok;
		commentAdapter = ca;
		
		OkHTTPTask task = new OkHTTPTask();
		if(t == 'S'){
			commentableType = "sparks";
		} else if (t == 'I'){
			commentableType = "ideas";
		}
		dialog = new ProgressDialog(commentAdapter.getContext());
		dialog.setMessage("Posting comment...");
		dialog.show();
		task.execute("http://steamnet.herokuapp.com/api/v1/"+commentableType+"/"+commentableId+"/comments.json");
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
	        	
	        	
	        	String postData = "&comment[comment_text]="+commentText+"&username="+username+"&token="+token;
	        	Log.v(TAG, postData);
	        	
	        	//return get(new URL(urls[0]));
	        	
	        	return post(new URL(urls[0]), postData.getBytes());
	        	
	        } catch (Exception e) {
	            this.exception = e;
	            Log.e(TAG, "Exception: "+e);
	            return null;
	        }
	    }
	
	    protected void onPostExecute(String data) {
			Comment newComment = new Comment(userID, commentText, username);
			commentAdapter.addComment(newComment);
			if (commentAdapter.getComments()[0].getUserId() == 0) {
				commentAdapter.removeComment(0);
			}
			commentAdapter.notifyDataSetChanged();
			dialog.dismiss();
	    }
	    
	    
	    /*
	     * TRYING TO MAKE POST FROM HERE DOWN
	     */
	    
	    String post(URL url, byte[] body) throws IOException {
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
	      }
	    
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
	    
	 }

}
