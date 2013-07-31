package org.friendscentral.steamnet.Activities;

import java.lang.reflect.InvocationTargetException;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.friendscentral.steamnet.DetailViewFillers.AudioFiller;
import org.friendscentral.steamnet.DetailViewFillers.CodeFiller;
import org.friendscentral.steamnet.DetailViewFillers.DetailFiller;
import org.friendscentral.steamnet.DetailViewFillers.LinkFiller;
import org.friendscentral.steamnet.DetailViewFillers.PictureFiller;
import org.friendscentral.steamnet.DetailViewFillers.TextFiller;
import org.friendscentral.steamnet.DetailViewFillers.VideoFiller;

import APIHandlers.GetSparkForDetail;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class SparkDetailActivity extends Activity {
	final static int GET_AUTH_ACTIVITY_REQUEST_CODE = 4;
	DetailFiller filler;
	Spark spark;
	private static final String TAG = "SparkDetailView";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spark_detail);
		
		findViewById(R.id.DummyFocusCommentSection).setFocusableInTouchMode(true);
        findViewById(R.id.DummyFocusCommentSection).requestFocus();
		
		Intent intent = getIntent();
		int sparkId = intent.getExtras().getInt("id");
		new GetSparkForDetail(sparkId, SparkDetailActivity.this);
		
	}
	
	public void setFiller(Spark s) {
		spark = s;
		switch (spark.getContentType()) {
		case 'T':
			filler = new TextFiller(spark, (LinearLayout) findViewById(R.id.TextData), SparkDetailActivity.this);
			break;
		case 'A':
			filler = new AudioFiller(spark, (LinearLayout) findViewById(R.id.AudioData), SparkDetailActivity.this);
			break;
		case 'L':
			filler = new LinkFiller(spark, (LinearLayout) findViewById(R.id.LinkData), SparkDetailActivity.this);
			break;
		case 'P':
			filler = new PictureFiller(spark, (LinearLayout) findViewById(R.id.PictureData), SparkDetailActivity.this);
			break;
		case 'V':
			filler = new VideoFiller(spark, (LinearLayout) findViewById(R.id.VideoData), SparkDetailActivity.this);
			break;
		case 'C':
			filler = new CodeFiller(spark, (LinearLayout) findViewById(R.id.CodeData), SparkDetailActivity.this);
			break;
		}
	}
	
	public void submitComment(View v) {
		if (filler != null) {
			filler.submitComment(v);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
    	actionBar.setCustomView(R.layout.log_in_action_bar);
    	STEAMnetApplication sna = (STEAMnetApplication) getApplication();
    	if (!sna.getReadOnlyMode()) {
    		Button logButton = (Button) actionBar.getCustomView().findViewById(R.id.log_in_button);
    		logButton.setText("Log out");
    		
    		logButton.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				logOut();
    			}
    		});
    		TextView logInInfo = (TextView) actionBar.getCustomView().findViewById(R.id.log_in_info); 
    		logInInfo.setText("Logged in as "+sna.getUsername());
    	} else {
    		actionBar.getCustomView().findViewById(R.id.log_in_button).setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				logIn();
    			}
        	});
    	}
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}
	
	public void logIn() {
		Intent intent = new Intent(SparkDetailActivity.this, AuthActivity.class);
		startActivityForResult(intent, GET_AUTH_ACTIVITY_REQUEST_CODE);
	}
	
	public void logOut() {
		STEAMnetApplication sna = (STEAMnetApplication) getApplication();
		sna.logOut();
		ActionBar actionBar = getActionBar();
    	actionBar.setCustomView(R.layout.log_in_action_bar);
    	actionBar.getCustomView().findViewById(R.id.log_in_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logIn();
			}
    	});
	}
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case GET_AUTH_ACTIVITY_REQUEST_CODE:
			Log.v("Spark Detail activity", "Should be here if you have just logged in though OAuth");
			STEAMnetApplication sna = (STEAMnetApplication) getApplication();
	    	if (!sna.getReadOnlyMode()) {
	    		ActionBar actionBar = getActionBar();
	    		Button logButton = (Button) actionBar.getCustomView().findViewById(R.id.log_in_button);
	    		logButton.setText("Log out");
	    		
	    		logButton.setOnClickListener(new OnClickListener() {
	    			public void onClick(View v) {
	    				logOut();
	    			}
	    		});
	    		TextView logInInfo = (TextView) actionBar.getCustomView().findViewById(R.id.log_in_info); 
	    		logInInfo.setText("Logged in as "+sna.getUsername());
	    	}
	    	break;
		}
	}
	
	protected void onDestroy() {
		super.onDestroy();
		if (filler != null && spark.getContentType() == 'A') {
			if (((AudioFiller) filler).getAudioStreamer() != null) {
				((AudioFiller) filler).getAudioStreamer().stop();
			}
		} if (filler != null && spark.getContentType() == 'V') {
			WebView video = ((VideoFiller) filler).getEmbededVideo();
			try {
				Class.forName("android.webkit.WebView")
				    .getMethod("onPause", (Class[]) null)
				     	.invoke(video, (Object[]) null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Starting and stopping Google Analytics code:
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

}
