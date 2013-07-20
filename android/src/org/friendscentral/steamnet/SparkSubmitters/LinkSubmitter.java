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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LinkSubmitter extends SparkSubmitter {
	View entryForm;
	ViewGroup linkForm;
	SparkWizard sparkWizard;
	ImageView img;
	Bitmap favicon;
	
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
				
				new LoadImageFromWeb(url);		
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
		
		if (favicon != null) {
			newSpark.setBitmap(favicon);
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
			 dialog.setMessage("Fetching YouTube metadata...");
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
		        if (BitmapFactory.decodeStream(is) != null) {
		        	favicon = BitmapFactory.decodeStream(is);
		        }
				
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
}
