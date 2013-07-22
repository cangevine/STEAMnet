package org.friendscentral.steamnet.SparkSubmitters;

import java.io.InputStream;
import java.net.URL;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.SparkWizard;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LinkSubmitter extends SparkSubmitter {
	View entryForm;
	ViewGroup linkForm;
	SparkWizard sparkWizard;
	ImageView img;
	Bitmap favicon;
	Bitmap screenshot;
	ProgressDialog dialog;
	
	public LinkSubmitter(View v, MainActivity m) {
		super(m);
		
		entryForm = v;
		linkForm = (ViewGroup) v.findViewById(R.id.link_form);
		img = (ImageView) linkForm.findViewById(R.id.link_favicon);
		
		entryForm.findViewById(R.id.submit_content_entry_button).setEnabled(false);
		linkForm.findViewById(R.id.link_test_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				entryForm.findViewById(R.id.submit_content_entry_button).setEnabled(false);
				
				String url = getAndParseUrl();
				
				WebView preview = (WebView) entryForm.findViewById(R.id.link_content_preview);
				initWebView(preview);
				EditText contentForm = (EditText) entryForm.findViewById(R.id.link_content_entry_form);
				String content = contentForm.getText().toString().trim();
				
				dialog = new ProgressDialog(mainActivity);
                dialog.setMessage("Loading website");
                dialog.show();
				preview.loadUrl(content);
				
				//new LoadImageFromWeb(url);
			}
		});
	}
	

	@Override
	public Spark getNewSpark(char sparkType) {
		
		EditText tagsForm = (EditText) entryForm.findViewById(R.id.tag_entry_form);
		String tags = tagsForm.getText().toString();
		
		EditText contentForm = (EditText) entryForm.findViewById(R.id.link_content_entry_form);
		String content = contentForm.getText().toString().trim();
		
		STEAMnetApplication sna = (STEAMnetApplication) mainActivity.getApplication();
		String userId = "0";
		if (sna.getUserId() != null) {
			userId = sna.getUserId();
		}
		Spark newSpark = new Spark(sparkType, 'L', content, userId, tags);
		
		if (screenshot != null) {
			newSpark.setBitmap(screenshot);
			Log.v("Screenshot attached", "successfully");
		}
		
		return newSpark;
	}
	
	public String getAndParseUrl() {
		EditText e = (EditText) linkForm.findViewById(R.id.link_content_entry_form);
		String link = e.getText().toString().toLowerCase();
		link = link.trim();
		
		if (link.contains("http://") || link.contains("https://")) {
			//do nothing
		} else {
			link = "http://" + link;
		}
		
		String prelink = "http://getfavicon.appspot.com/";
		String faviconLink = prelink + link;
		
		return faviconLink;
	}
	
	public void takeScreenshot() {
		Log.v("takeScreenshot", "Taking screenshot of gist");
		View v = entryForm.findViewById(R.id.link_content_preview);
	    v.setDrawingCacheEnabled(true);
	    v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
	            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	    v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

	    v.buildDrawingCache(true);
	    Bitmap source = loadBitmapFromView(v);
	    int x = 0, y = 0, width = source.getWidth(), height = source.getHeight(); 
	    screenshot = Bitmap.createBitmap(source, x, y, width, height);
	    Log.v("takeScreenshot", "Screenshot saved");
	    
	    Bitmap scaledScreen = Bitmap.createScaledBitmap(screenshot, 250, 187, true);
	    
	    img.setImageBitmap(scaledScreen);
	    img.setVisibility(View.VISIBLE);
	    entryForm.findViewById(R.id.link_content_entry_form).setVisibility(View.GONE);
	    entryForm.findViewById(R.id.link_test_button).setVisibility(View.GONE);
	    dialog.dismiss();
	    entryForm.findViewById(R.id.submit_content_entry_button).setEnabled(true);
	}
	
	public static Bitmap loadBitmapFromView(View v) {
	     Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);                
	     Canvas c = new Canvas(b);
	     v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
	     v.draw(c);
	     return b;
	}
	
	private class LoadImageFromWeb extends AsyncTask<String, Void, Drawable> {
		String url;
		Drawable downloadedImage;
		ProgressDialog dialog;
		
		public LoadImageFromWeb(String u) {
			url = u;
			this.execute();
		}
		
		protected void onPreExecute() {
			 dialog = new ProgressDialog(mainActivity);
			 dialog.setMessage("Fetching website's metadata...");
            dialog.show();
		}
		
		@Override
		protected Drawable doInBackground(String... args) {
			try {
		        Log.v("LoadImageFromWeb", "doInBackground");
		        Log.v("LoadImageFromWeb", url);
				
				String websiteName = url;
				websiteName = websiteName.replace("http://getfavicon.appspot.com/", "");
				websiteName = websiteName.replace("http", "");
				websiteName = websiteName.replace(".", "");
				websiteName = websiteName.replace("/", "");
				websiteName = websiteName.replace(":", "");
				websiteName = websiteName.replace("-", "");
				websiteName += "_favicon";
				
				Log.v("LoadImageFromWeb", "WebsiteName: "+websiteName);
				
				InputStream is = (InputStream) new URL(url).getContent();
		        downloadedImage = Drawable.createFromStream(is, "websiteName");
		        is = (InputStream) new URL(url).getContent();
		        favicon = BitmapFactory.decodeStream(is);
				
				return downloadedImage;
		    } catch (Exception e) {
		        Log.v("LoadImageFromWebOperations", "Exc="+e);
		    }
			return null;
		}
		
		@SuppressWarnings("deprecation")
		protected void onPostExecute(Drawable d) {
			dialog.dismiss();
			
		     if (d != null) {
		    	 img.setImageDrawable(d);
		    	 entryForm.findViewById(R.id.submit_content_entry_button).setEnabled(true);
		     } else {
		    	 Toast.makeText(mainActivity, "Error: Link could not be loaded", Toast.LENGTH_SHORT).show();
		     }
		}
	}
	
	public void initWebView(WebView wv) {
		wv.setWebViewClient(new ScreenshotClient());
		
		WebSettings webViewSettings = wv.getSettings();
		webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webViewSettings.setJavaScriptEnabled(true);
		webViewSettings.setPluginsEnabled(true);
		webViewSettings.setBuiltInZoomControls(true);
		webViewSettings.setPluginState(PluginState.ON);
	}
	
	private class ScreenshotClient extends WebViewClient {
		public void onPageStarted(WebView wv, String url, Bitmap f) {
			Log.v("ScreenshotClient", "Page started loading");
		}
		
		public void onPageFinished(WebView wv, String url) {
			Log.v("ScreenshotClient", "PageFinishedLoading");
			takeScreenshot();
		}
	}
}
