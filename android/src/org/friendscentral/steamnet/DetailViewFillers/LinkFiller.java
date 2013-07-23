package org.friendscentral.steamnet.DetailViewFillers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;

public class LinkFiller extends DetailFiller {
	TextView pageTitle;
	ImageView faviconView;
	WebView screenshotView;
	InputStream is;

	public LinkFiller(Spark s, LinearLayout sparkData, Context c) {
		super(s, "Link", sparkData, c);
		
		pageTitle = (TextView) detailView.findViewById(R.id.link_data_page_title);
		faviconView = (ImageView) detailView.findViewById(R.id.link_data_favicon);
		screenshotView = (WebView) detailView.findViewById(R.id.link_data_page_iframe);
		
		fillData();
	}

	@Override
	void fillData() {
		new GetFavicon(content);
		new getPageTitle(content);
		loadIframe();
		//getScreenshot();
	}
	
	public void loadIframe() {
		final String url = content;
		String webViewContent = "<html><body><center><iframe src='"+url+"'></iframe></center></body></html>";
		Log.v("HTML String:", webViewContent);
		
		screenshotView.setWebViewClient(new CustomWebClient());
		WebSettings webViewSettings = screenshotView.getSettings();
		webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webViewSettings.setJavaScriptEnabled(true);
		//webViewSettings.setPluginsEnabled(true);
		//webViewSettings.setBuiltInZoomControls(true);
		//webViewSettings.setPluginState(PluginState.ON);
		
		screenshotView.loadUrl(url);
		
		detailView.findViewById(R.id.link_data_launch_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				context.startActivity(i);
			}
		});
	}
	
	private class getPageTitle extends AsyncTask<String, Void, String> {
		String url;
		ProgressDialog dialog;
		OkHttpClient client;
		
		public getPageTitle(String s) {
			url = s;
			client = new OkHttpClient();
			this.execute();
		}
		
		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setMessage("Fetching Site metadata...");
            dialog.show();
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			try {
				return get(new URL(url));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String s) {
			String begin = "<title>";
			String end = "</title>";
			String title = s.substring(s.indexOf(begin) + begin.length(), s.indexOf(end));
			Log.v("LinkFiller", title);
			pageTitle.setText(title);
			pageTitle.setTextSize(25f);
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
	
	private class GetFavicon extends AsyncTask<String, Void, InputStream> {
		String url;
		Drawable d;
		
		public GetFavicon(String u) {
			Log.v("LoadImageFromWebOperations", "Constructor");
			String prelink = "http://getfavicon.appspot.com/";
			String url = prelink + u;
			this.execute();
		}
		
		@Override
		protected InputStream doInBackground(String... arg0) {
			Log.v("LoadImageFromWebOperations", "doInBackgroud");
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
				faviconView.setImageDrawable(d);
			}
		}
	}

	private class CustomWebClient extends WebViewClient {
		ProgressDialog dialog;
		
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			dialog = new ProgressDialog(context);
			dialog.setMessage("Loading webpage...");
			dialog.show();
		}
		
		public void onPageFinished(WebView view, String url) {
			dialog.dismiss();
		}
	}
	
}
