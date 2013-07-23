package org.friendscentral.steamnet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.friendscentral.steamnet.EventHandlers.EndlessScroller;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.squareup.okhttp.OkHttpClient;

public class AttachScrollListener {
	FilterSettings filterSettings;
	GridView gridView;
	IndexGrid indexGrid;
	Context context;
	
	public AttachScrollListener(FilterSettings f, GridView g, IndexGrid i, Context c) {
		filterSettings = f;
		gridView = g;
		indexGrid = i;
		context = c;
		
		new GetJawnsInDB().execute();
	}
	
	private class GetJawnsInDB extends AsyncTask<String, Void, String> {
		int num;
		OkHttpClient client;
		
		public GetJawnsInDB() {
			client = new OkHttpClient();
		}
		
		@Override
		protected String doInBackground(String... args) {
            try {
				return get(new URL("http://steamnet.herokuapp.com/api/v1/jawns/count.json"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
            return null;
		}
		
		protected void onPostExecute(String data) {
			try {
				final String COUNT = "jawns_count";
				JSONObject json = new JSONObject(data);
				String count = json.getString(COUNT);
				Log.v("Number of Jawns in database:", count);
				num = Integer.parseInt(count);
				EndlessScroller es = new EndlessScroller(filterSettings, gridView, indexGrid, context, num);
				gridView.setOnScrollListener(es);
				indexGrid.setScrollListener(es);
			} catch (JSONException e) {
				e.printStackTrace();
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
