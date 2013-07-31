package org.friendscentral.steamnet.Activities;

import org.friendscentral.steamnet.AttachScrollListener;
import org.friendscentral.steamnet.FilterSettings;
import org.friendscentral.steamnet.IdeaBucket;
import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.SparkWizard;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.friendscentral.steamnet.EventHandlers.IdeaBucketEventHandler;
import org.friendscentral.steamnet.EventHandlers.SparkEventHandler;
import org.friendscentral.steamnet.SparkSubmitters.AudioSubmitter;
import org.friendscentral.steamnet.SparkSubmitters.PictureSubmitter;
import org.friendscentral.steamnet.SparkWizardFragments.SparkTypeChooser;

import com.google.analytics.tracking.android.EasyTracker;

import CachingHandlers.JawnsDataSource;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	Spark newSpark;
	char sparkType = "I".charAt(0);
	char contentType = "T".charAt(0);

    static final String EXTRA_MESSAGE = null;
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    final static int UPLOAD_IMAGE_ACTIVITY_REQUEST_CODE = 2;
    final static int UPLOAD_AUDIO_ACTIVITY_REQUEST_CODE = 3;
    final static int GET_AUTH_ACTIVITY_REQUEST_CODE = 4;
	final static int DETAIL_VIEW_RETURN = 5;
    
    SparkWizard sparkWizard;
    SparkEventHandler sparkEventHandler;
    IdeaBucketEventHandler bucketHandler;
    LinearLayout wizardSection;
    
    IdeaBucket ideaBucket;
    LinearLayout mainLayout;
    GridView gridView;
    IndexGrid indexGrid;
    FilterSettings filterSettings;
    
    JawnsDataSource jawnsDataSource;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mainLayout = (LinearLayout) findViewById(R.id.MainLayout);
        
        //Fixes autofocus problem:
        findViewById(R.id.DummyFocus).setFocusableInTouchMode(true);
        findViewById(R.id.DummyFocus).requestFocus();
        
        //Initialize the Wizard Fragment:
        wizardSection = (LinearLayout) findViewById(R.id.WizardSection);
        FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		SparkTypeChooser stc = new SparkTypeChooser();
		ft.add(R.id.WizardSection, stc);
		ft.commit();
		
        //Initialize the grid of Sparks:
        initializeIndexGridLayout();
        
        //Initialize Idea Bucket:
        initIdeaBucket();

		sparkWizard = new SparkWizard(mainLayout, getFragmentManager(), MainActivity.this);
		bucketHandler = new IdeaBucketEventHandler(MainActivity.this, ideaBucket, mainLayout, getFragmentManager());
		
		//Init filter settings
		LinearLayout fsettings = (LinearLayout) findViewById(R.id.filterSettingsLayout);
		filterSettings = new FilterSettings(fsettings, mainLayout, this, gridView, indexGrid);
    }
    
    public void initializeIndexGridLayout() {
    	jawnsDataSource = new JawnsDataSource(this);
    	jawnsDataSource.open();
    	
    	final View indexGridLayout = findViewById(R.id.IndexGrid);
    	gridView = (GridView) indexGridLayout.findViewById(R.id.SparkGrid);
    	indexGrid = new IndexGrid();
    	indexGrid.initIndexGrid(gridView, MainActivity.this, jawnsDataSource, false);
    }
    
    public void initIdeaBucket() {
    	ideaBucket = new IdeaBucket();
        View ideaBucketLayout = findViewById(R.id.IdeaBucket);
    	LinearLayout ideaGrid = (LinearLayout) ideaBucketLayout.findViewById(R.id.idea_bucket_linear);
        ideaBucket.initIdeaGrid(ideaGrid, MainActivity.this, (LinearLayout) findViewById(R.id.MainLayout));
    }
    
    public void updateWeights(float sp, float fs, float ib) {
    	findViewById(R.id.WizardSection).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, sp));
		findViewById(R.id.FilterSettings).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, fs));
		findViewById(R.id.IdeaBucket).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, ib));
    }
	
	public void updateWizard(View v) {
		String tag = (String) v.getTag();
		
		String content;
		Log.d("openContentTypeChooser", tag);
		if (tag.equals("openContentTypeChooser")) {
			
			if (v.getId() == R.id.Inspiration_button) {
				sparkType = 'I';
			} else if (v.getId() == R.id.What_if_button) {
				sparkType = 'W';
			} else if (v.getId() == R.id.Problem_button) {
				sparkType = 'P';
			}
						
			sparkWizard.openContentTypeChooser(v, sparkType);
			
		} else if (tag.equals("revertWizard")) {
			
			sparkWizard.revertWizard(v);
			
		} else if (tag.equals("openContentEntry")) {
			
			if (v.getId() == R.id.picture_button) {
				contentType = 'P';
			} else if (v.getId() == R.id.video_button) {
				contentType = 'V';
			} else if (v.getId() == R.id.text_button) {
				contentType = 'T';
			} else if (v.getId() == R.id.code_button) {
				contentType = 'C';
			} else if (v.getId() == R.id.audio_button) {
				contentType = 'A';
			} else if (v.getId() == R.id.link_button) {
				contentType = 'L';
			}
						
			sparkWizard.openContentEntry(v, contentType);
			
		} else if (tag.equals("submitSpark")) {
			
			newSpark = sparkWizard.getContentEntry().getSparkSubmitter().getNewSpark(sparkType);
			sparkWizard.submitSpark(v, newSpark, gridView, indexGrid);
			
		}
	}
	
	public void filterSettingsFunction(View v) {
		String tag = (String) v.getTag();
		filterSettings.call(v, tag);
	}
	
	public void setScrollListener() {
		new AttachScrollListener(filterSettings, gridView, indexGrid, MainActivity.this);
	}
	 
	public IndexGrid getIndexGrid() {
		return indexGrid;
	}
	
	public SparkEventHandler getSparkEventHandler() {
		return sparkEventHandler;
	}
	
	public void setSparkEventHandlers() {
		sparkEventHandler = new SparkEventHandler(MainActivity.this, mainLayout, ideaBucket, gridView, indexGrid); 
	}
	
	public SparkWizard getSparkWizard() {
		return sparkWizard;
	}
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		PictureSubmitter p;
		AudioSubmitter a;
		switch (requestCode) {
		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			Log.v("Main Activity", "Should be here if you are taking a picture");
			p = (PictureSubmitter) sparkWizard.getContentEntry().getSparkSubmitter(); 
			p.getPictureTaker().onActivityResult(requestCode, resultCode, data);
			break;
		case UPLOAD_IMAGE_ACTIVITY_REQUEST_CODE:
			Log.v("Main Activity", "Should be here if you are uploading an image");
			p = (PictureSubmitter) sparkWizard.getContentEntry().getSparkSubmitter(); 
			p.getPictureUploader().onActivityResult(requestCode, resultCode, data);
			break;
		case UPLOAD_AUDIO_ACTIVITY_REQUEST_CODE:
			Log.v("Main Activity", "Should be here if you are uploading an image");
			a = (AudioSubmitter) sparkWizard.getContentEntry().getSparkSubmitter(); 
			a.getAudioUploader().onActivityResult(requestCode, resultCode, data);
			break;
		case GET_AUTH_ACTIVITY_REQUEST_CODE:
			Log.v("Main activity", "Should be here if you have just logged in though OAuth");
			STEAMnetApplication sna = (STEAMnetApplication) getApplication();
	    	if (sna.getUsername() != null) {
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
		case DETAIL_VIEW_RETURN:
			Log.v("Main activity", "Returned from detail view");
			STEAMnetApplication sn = (STEAMnetApplication) getApplication();
			ActionBar ab = getActionBar();
			Button logB = (Button) ab.getCustomView().findViewById(R.id.log_in_button);
	    	//Search for conflict:
			if (sn.getUserId() == null && logB.getText().equals("Log out")) {
				logOut();
			} else if (sn.getUserId() != null && logB.getText().equals("Log in")) {
	    		logB.setText("Log out");
	    		
	    		logB.setOnClickListener(new OnClickListener() {
	    			public void onClick(View v) {
	    				logOut();
	    			}
	    		});
	    		TextView logInInfo = (TextView) ab.getCustomView().findViewById(R.id.log_in_info); 
	    		logInInfo.setText("Logged in as "+sn.getUsername());
			}
	    	break;
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
		Intent intent = new Intent(MainActivity.this, AuthActivity.class);
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
	
	@Override
	protected void onResume() {
	    jawnsDataSource.open();
	    super.onResume();
	}

	@Override
	protected void onPause() {
	    jawnsDataSource.close();
	    super.onPause();
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
