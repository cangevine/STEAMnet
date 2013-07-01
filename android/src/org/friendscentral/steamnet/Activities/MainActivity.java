package org.friendscentral.steamnet.Activities;

import org.friendscentral.steamnet.IdeaBucket;
import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.SparkWizard;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.friendscentral.steamnet.EventHandlers.IdeaBucketEventHandler;
import org.friendscentral.steamnet.EventHandlers.SparkEventHandler;
import org.friendscentral.steamnet.SparkWizardFragments.SparkTypeChooser;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	Spark newSpark;
	char sparkType = "I".charAt(0);
	char contentType = "T".charAt(0);

    static final String EXTRA_MESSAGE = null;
    
    SparkWizard sparkWizard;
    SparkEventHandler sparkEventHandler;
    IdeaBucketEventHandler bucketHandler;
    
    IdeaBucket ideaBucket;
    LinearLayout mainLayout;
    GridView gridView;
    IndexGrid indexGrid;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.MainLayout);
        
        //Fixes autofocus problem:
        findViewById(R.id.DummyFocus).setFocusableInTouchMode(true);
        findViewById(R.id.DummyFocus).requestFocus();
        
        //Initialize the Wizard Fragment:
        FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		SparkTypeChooser stc = new SparkTypeChooser();
		ft.add(R.id.WizardSection, stc);
		ft.commit();
		
        
        //Initialize the grid of Sparks:
        initializeIndexGridLayout();
        
        //Initialize Idea Bucket:
        initIdeaBucket();
        

		sparkWizard = new SparkWizard(mainLayout, getFragmentManager());
		sparkEventHandler = new SparkEventHandler(MainActivity.this, mainLayout, ideaBucket, gridView, indexGrid);
		bucketHandler = new IdeaBucketEventHandler(MainActivity.this, ideaBucket, mainLayout, getFragmentManager());
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
				sparkType = "I".charAt(0);
			} else if (v.getId() == R.id.What_if_button) {
				sparkType = "W".charAt(0);
			} else if (v.getId() == R.id.Problem_button) {
				sparkType = "P".charAt(0);
			}
						
			sparkWizard.openContentTypeChooser(v);
			
		} else if (tag.equals("revertWizard")) {
			
			sparkWizard.revertWizard(v);
			newSpark = null;
			
		} else if (tag.equals("openContentEntry")) {
			
			if (v.getId() == R.id.picture_button) {
				contentType = "P".charAt(0);
			} else if (v.getId() == R.id.video_button) {
				contentType = "V".charAt(0);
			} else if (v.getId() == R.id.text_button) {
				contentType = "T".charAt(0);
			} else if (v.getId() == R.id.code_button) {
				contentType = "C".charAt(0);
			} else if (v.getId() == R.id.audio_button) {
				contentType = "A".charAt(0);
			} else if (v.getId() == R.id.link_button) {
				contentType = "L".charAt(0);
			}
						
			sparkWizard.openContentEntry(v);
			
		} else if (tag.equals("submitSpark")) {
			
			findViewById(R.id.content_entry_form);
			EditText tagsForm = (EditText) findViewById(R.id.tag_entry_form);
			tagsForm.getText().toString().split(", ");
			
			TextView sparkContentView = (TextView) findViewById(R.id.content_entry_form);
			content = sparkContentView.getText().toString();
			
			
			newSpark = new Spark(sparkType, contentType, content);
			sparkWizard.submitSpark(v, newSpark, gridView, indexGrid);
			
			newSpark = null;
			
		}
	}
	
	public void randomizeJawns(View v) {
		String className = indexGrid.getAdapter().getClass().getName(); 
		// TODO Conditional throws an error if it's NOT  the JawnAdapter
		if (className.equals("org.friendscentral.steamnet.JawnAdapter")) {
			JawnAdapter ja = indexGrid.getAdapter();
			ja.shuffleJawns(indexGrid.getJawns());
			ja.notifyDataSetChanged();
		}
	}
	
	public void sortRecent(View v) {
		indexGrid.initIndexGrid(gridView, MainActivity.this, false);
	}
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
}