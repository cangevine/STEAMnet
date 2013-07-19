package org.friendscentral.steamnet.SparkSubmitters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.SparkWizard;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.okhttp.OkHttpClient;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VideoSubmitter extends SparkSubmitter {
	View entryForm;
	SparkWizard sparkWizard;
	
	public VideoSubmitter(View v, MainActivity m) {
		super(m);
		
		entryForm = v;
		
		entryForm.findViewById(R.id.submit_content_entry_button).setEnabled(false);
		
		entryForm.findViewById(R.id.video_test_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText contentForm = (EditText) entryForm.findViewById(R.id.video_content_entry_form);
				String url = contentForm.getText().toString();
				String id = parseYoutubeUrl(url);
				if (id != null) {
					new LoadTitleFromId(id);
					new LoadImageFromId(id);
				}
			}
		});
	}

	@Override
	public Spark getNewSpark(char sparkType) {
		EditText contentForm = (EditText) entryForm.findViewById(R.id.video_content_entry_form);
		String content = contentForm.getText().toString();
		
		EditText tagsForm = (EditText) entryForm.findViewById(R.id.tag_entry_form);
		String tags = tagsForm.getText().toString();
		
		Spark newSpark = new Spark(sparkType, 'V', content);
		newSpark.setTags(tags);
		return newSpark;
	}
	
	public String parseYoutubeUrl(String i) {
		if (!i.toLowerCase().contains("youtube") && !i.toLowerCase().contains("youtu.be")) {
			Toast.makeText(mainActivity, "Not a Youtube link. Try again", Toast.LENGTH_LONG).show();
			return null;
		}
		String id = i;
		//Remove general url filler
		id = id.replace("youtube.com/watch?v=", "");
		//If link has www., remove
		if (id.contains("www.")) {
			id = id.replace("www.", "");
		}
		//If link has http://, remove
		if (id.contains("http://")) {
			id = id.replace("http://", "");
		} else if (id.contains("https://")) {
			id = id.replace("https://", "");
		} 
		//If link has extra data for Youtube analytics, remove
		if (id.contains("&feature=")) {
			String[] tmp = id.split("&feature=");
			id = tmp[0];
		}
		return id;
	}
	
	private class LoadTitleFromId extends AsyncTask<String, Void, String> {
		String id;
		String title;
		OkHttpClient client;
		
		public LoadTitleFromId(String u) {
			id = u;
			client = new OkHttpClient();
			this.execute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String embededURL = "http://gdata.youtube.com/feeds/api/videos/" + id + "?v=2&alt=jsonc";
			try {
				return get(new URL(embededURL));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String s) {
			final String TITLE = "title";
			final String DATA = "data";
			final String DESCRIPTION = "description";
			try {
				JSONObject data = new JSONObject(new JSONObject(s).getString(DATA));
				title = data.getString(TITLE);
				Log.v("onPostExecute", title);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			TextView t = (TextView) entryForm.findViewById(R.id.video_title);
			if (title != null) {
				t.setText(title);
				t.setVisibility(View.VISIBLE);
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
	
	private class LoadImageFromId extends AsyncTask<String, Void, InputStream> {
		String id;
		Drawable d;
		ProgressDialog dialog;
		
		public LoadImageFromId(String i) {
			Log.v("LoadImageFromWebOperations", "Constructor");
			id = i;
			this.execute();
		}
		
		protected void onPreExecute() {
			 dialog = new ProgressDialog(mainActivity);
			 dialog.setMessage("Fetching YouTube metadata...");
             dialog.show();
		}
		
		@Override
		protected InputStream doInBackground(String... arg0) {
			Log.v("LoadImageFromWebOperations", "doInBackgroud");
			String url = "http://img.youtube.com/vi/" + id + "/0.jpg";
			
			try {
		        InputStream is = (InputStream) new URL(url).getContent();
		        d = Drawable.createFromStream(is, "src name");
		        return is;
		    } catch (Exception e) {
		        Log.v("LoadImageFromWebOperations", "Exc="+e);
		    }
			return null;
		}
		
		protected void onPostExecute(InputStream i) {
			Log.v("LoadImageFromWebOperations", "onPostExecute");
			
			if (d != null) {
				dialog.dismiss();
				entryForm.findViewById(R.id.video_content_entry_form).setVisibility(View.GONE);
				entryForm.findViewById(R.id.header_textview).setVisibility(View.GONE);
				entryForm.findViewById(R.id.video_test_button).setVisibility(View.GONE);
				ImageView img = (ImageView) entryForm.findViewById(R.id.video_thumbnail);
				img.setImageDrawable(d);
				img.setVisibility(View.VISIBLE);
				entryForm.findViewById(R.id.submit_content_entry_button).setEnabled(true);
			}
		}
	}

}
