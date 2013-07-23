package org.friendscentral.steamnet.DetailViewFillers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;

@SuppressLint("SetJavaScriptEnabled")
public class VideoFiller extends DetailFiller {
	TextView videoHeader;
	TextView videoDescription;
	WebView video;

	public VideoFiller(Spark s, LinearLayout sparkData, Context c) {
		super(s, "Video", sparkData, c);
		videoHeader = (TextView) detailView.findViewById(R.id.video_data_information);
		videoDescription = (TextView) detailView.findViewById(R.id.video_data_description);
		video = (WebView) detailView.findViewById(R.id.video_data_webview);
		
		fillData();
	}

	@Override
	void fillData() {
		final String videoId = parseYoutubeUrl(content);
		if (videoId != null) {
			new LoadDataFromId(videoId);
			setWebView(videoId);
			detailView.findViewById(R.id.video_data_launch_button).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + videoId));
					context.startActivity(intent);
				}
			});
		}
		
		// TODO Add button that launches in the Youtube app (calls an intent or something)
	}
	
	public void setWebView(String id) {
		String webViewContent = "<center><iframe padding='0' width='560' height='315' src='//www.youtube.com/embed/" + id + "' frameborder='0' allowfullscreen></iframe></center>";
		Log.v("HTML String:", webViewContent);
		
		video.setWebChromeClient(new WebChromeClient());
		video.setWebViewClient(new CustomWebClient());
		
		WebSettings webViewSettings = video.getSettings();
		webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webViewSettings.setJavaScriptEnabled(true);
		//webViewSettings.setPluginsEnabled(true);
		//webViewSettings.setBuiltInZoomControls(true);
		//webViewSettings.setPluginState(PluginState.ON);
		
		video.loadDataWithBaseURL("http://www.youtube.com/", webViewContent, "text/html", "utf-8", null);
	}
	
	private class LoadDataFromId extends AsyncTask<String, Void, String> {
		String id;
		String title;
		String description;
		String uploader;
		String uploaded;
		OkHttpClient client;
		ProgressDialog dialog;
		
		public LoadDataFromId(String u) {
			id = u;
			client = new OkHttpClient();
			dialog = new ProgressDialog(context);
			dialog.setMessage("Loading YouTube metadata...");
			dialog.show();
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
			final String UPLOADER = "uploader";
			final String DATE = "uploaded";
			try {
				JSONObject data = new JSONObject(new JSONObject(s).getString(DATA));
				title = data.getString(TITLE);
				description = data.getString(DESCRIPTION);
				uploader = data.getString(UPLOADER);
				uploaded = data.getString(DATE);
				Log.v("onPostExecute", title);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if (title != null) {
				videoHeader.setText(title);
				videoHeader.setTextSize(25f);
			}
			if (uploader != null) {
				String dataString = "Uploaded by: "+uploader;
				videoDescription.setText(dataString);
			}
			dialog.dismiss();
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
	
	private class CustomWebClient extends WebViewClient {
		ProgressDialog dialog;
		
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			dialog = new ProgressDialog(context);
			dialog.setMessage("Loading YouTube video...");
			dialog.show();
		}
		
		public void onPageFinished(WebView view, String url) {
			dialog.dismiss();
		}
	}
	
	public String parseYoutubeUrl(String i) {
		/*
		 * Takes any permutation of a youtube url and strips out the ID
		 * ..theoretically
		 */
		
		if (!i.toLowerCase().contains("youtube") && !i.toLowerCase().contains("youtu.be")) {
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
	
}
