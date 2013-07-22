package org.friendscentral.steamnet.SparkSubmitters;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.SparkWizard;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CodeSubmitter extends SparkSubmitter {
	View entryForm;
	SparkWizard sparkWizard;
	Button submitButton;
	String gistId;
	JavaScriptInterface j;
	Bitmap screenshot;
	WebView preview;
	
	public CodeSubmitter(View v, MainActivity m) {
		super(m);
		
		entryForm = v;
		submitButton = (Button) entryForm.findViewById(R.id.submit_content_entry_button);
		submitButton.setEnabled(false);
		entryForm.findViewById(R.id.code_form_test_button).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				testLink();
			}
		});
	}

	@Override
	public Spark getNewSpark(char sparkType) {
		EditText contentForm = (EditText) entryForm.findViewById(R.id.code_content_entry_form);
		String content = contentForm.getText().toString();
		
		EditText tagsForm = (EditText) entryForm.findViewById(R.id.tag_entry_form);
		String tags = tagsForm.getText().toString();
		
		String verifier = content.toLowerCase();
		
		/*
		 * TODO: 
		 * Place this verifier into the "test" function rather than the
		 * Submit function
		 */
		if (verifier.contains("gist.github")) {
			LayoutParams l = new LinearLayout.LayoutParams(j.getWidth(), j.getHeight());
			preview.setLayoutParams(l);
			Log.v("LayoutParams:", l.toString());
			takeScreenshot();
			
			STEAMnetApplication sna = (STEAMnetApplication) mainActivity.getApplication();
			String userId = "0";
			if (sna.getUserId() != null) {
				userId = sna.getUserId();
			}
			Spark newSpark = new Spark(sparkType, 'C', content, userId, tags);
			if (screenshot != null) {
				newSpark.setBitmap(screenshot);
			}
			ImageView img = (ImageView) entryForm.findViewById(R.id.code_preview_image);
			entryForm.findViewById(R.id.code_content_holder).setVisibility(View.GONE);
			img.setVisibility(View.VISIBLE);
			img.setImageBitmap(screenshot);
			return newSpark;
		} else {
			Toast.makeText(mainActivity, "Not a link to a GitHub gist. Try again", Toast.LENGTH_LONG).show();
			contentForm.setText("");
		}
		
		return null;
	}

	public void testLink() {
		EditText contentForm = (EditText) entryForm.findViewById(R.id.code_content_entry_form);
		String content = contentForm.getText().toString();
		if (content.contains("gist.github")) {
			entryForm.findViewById(R.id.code_form_test_button).setVisibility(View.GONE);
			entryForm.findViewById(R.id.code_content_entry_form).setVisibility(View.GONE);
			preview = (WebView) entryForm.findViewById(R.id.code_content_preview); 
			entryForm.findViewById(R.id.code_content_holder).setVisibility(View.VISIBLE);
			
			initWebView();
			
			gistId = content.substring(content.length() - 7);
			String url = "<script src='" + content + ".js'></script>";
			String embed = "<html><head>"+url+"</head></html>";
			preview.loadDataWithBaseURL("https://gist.github.com/", embed, "text/html", "utf-8", null);
			
			
		} else {
			Toast.makeText(mainActivity, "Not a Github Gist link, try again", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void takeScreenshot() {
		Log.v("takeScreenshot", "Taking screenshot of gist");
		View v = preview;
	    v.setDrawingCacheEnabled(true);
	    v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
	            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	    v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

	    v.buildDrawingCache(true);
	    Bitmap source = loadBitmapFromView(v);
	    int maxSize = 350;
	    int smallerMeasure = Math.min(j.getWidth(), j.getHeight() - 20);
	    int x = 0, y = 0, width = Math.min(maxSize, smallerMeasure), height = Math.min(maxSize, smallerMeasure); 
	    screenshot = Bitmap.createBitmap(source, x, y, width, height);
	    Log.v("takeScreenshot", "Screenshot saved");
	}
	
	public static Bitmap loadBitmapFromView(View v) {
	     Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);                
	     Canvas c = new Canvas(b);
	     v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
	     v.draw(c);
	     return b;
	}
	
	public void initWebView() {
		CustomWebClient c = new CustomWebClient();
		preview.setWebViewClient(c);
		j = new JavaScriptInterface();
		preview.addJavascriptInterface(j, "HTMLOUT"); 
		
		WebSettings webViewSettings = preview.getSettings();
		webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webViewSettings.setJavaScriptEnabled(true);
		webViewSettings.setPluginsEnabled(true);
		webViewSettings.setBuiltInZoomControls(true);
		webViewSettings.setPluginState(PluginState.ON);
	}
	
	private class CustomWebClient extends WebViewClient {
		public void onPageFinished(WebView view, String url) {
			String gistDiv = "gist"+gistId;
			preview.loadUrl("javascript:window.HTMLOUT.setHeight(document.getElementById('"+gistDiv+"').clientHeight);");
			preview.loadUrl("javascript:window.HTMLOUT.setWidth(document.getElementsByClassName('file-data')[0].scrollWidth);");
			submitButton.setEnabled(true);
		}
	}
	
	private class JavaScriptInterface {
		int height;
		int width;
		
		@SuppressWarnings("unused")
		public void setHeight(String html) {
			Log.v("Gist height:", html);
			height = Integer.valueOf(html);
		}
		
		@SuppressWarnings("unused")
		public void setWidth(String html) {
			Log.v("Gist width:", html);
			width = Integer.valueOf(html);
		}
		
		public int getHeight() {
			return height;
		}
		
		public int getWidth() {
			return width;
		}
	}
}
