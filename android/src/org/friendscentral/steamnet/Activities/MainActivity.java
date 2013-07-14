package org.friendscentral.steamnet.Activities;

import org.friendscentral.steamnet.FilterSettings;
import org.friendscentral.steamnet.IdeaBucket;
import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.SparkWizard;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.friendscentral.steamnet.EventHandlers.EndlessScroller;
import org.friendscentral.steamnet.EventHandlers.IdeaBucketEventHandler;
import org.friendscentral.steamnet.EventHandlers.SparkEventHandler;
import org.friendscentral.steamnet.SparkWizardFragments.SparkTypeChooser;

import SparkSubmitters.AudioSubmitter;
import SparkSubmitters.PictureSubmitter;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	Spark newSpark;
	char sparkType = "I".charAt(0);
	char contentType = "T".charAt(0);

    static final String EXTRA_MESSAGE = null;
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    final static int UPLOAD_IMAGE_ACTIVITY_REQUEST_CODE = 2;
    final static int UPLOAD_AUDIO_ACTIVITY_REQUEST_CODE = 3;
    
    SparkWizard sparkWizard;
    SparkEventHandler sparkEventHandler;
    IdeaBucketEventHandler bucketHandler;
    LinearLayout wizardSection;
    
    IdeaBucket ideaBucket;
    LinearLayout mainLayout;
    GridView gridView;
    IndexGrid indexGrid;
    FilterSettings filterSettings;

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
    	final View indexGridLayout = findViewById(R.id.IndexGrid);
    	gridView = (GridView) indexGridLayout.findViewById(R.id.SparkGrid);
    	indexGrid = new IndexGrid();
    	indexGrid.initIndexGrid(gridView, MainActivity.this, false);
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
			newSpark = null;
			
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
			
			//TextView sparkContentView = (TextView) findViewById(R.id.content_entry_form);
			//content = sparkContentView.getText().toString();
			content = "Not working right now. Stop.";
			
			
			//newSpark = new Spark(sparkType, contentType, content);
			sparkWizard.submitSpark(v, newSpark, gridView, indexGrid);
			
			newSpark = null;
			
		}
	}
	
	public void filterSettingsFunction(View v) {
		String tag = (String) v.getTag();
		filterSettings.call(v, tag);
	}
	
	public void setScrollListener() {
		gridView.setOnScrollListener(new EndlessScroller(filterSettings, gridView, indexGrid, this));
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
		}
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
}
