package org.friendscentral.steamnet.Activities;

import org.friendscentral.steamnet.IdeaBucket;
import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.Jawn;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.Spark;
import org.friendscentral.steamnet.SparkWizard;
import org.friendscentral.steamnet.EventHandlers.IdeaBucketEventHandler;
import org.friendscentral.steamnet.EventHandlers.SparkEventHandler;
import org.friendscentral.steamnet.SparkWizardFragments.SparkTypeChooser;

import APIHandlers.RetrieveDataTask;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	Spark newSpark;

    static final String EXTRA_MESSAGE = null;
    
    SparkWizard sparkWizard;
    SparkEventHandler sparkEventHandler;
    IdeaBucketEventHandler bucketHandler;
    
    IdeaBucket ideaBucket;
    LinearLayout mainLayout;
    GridView gridview;
    IndexGrid indexGrid;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.MainLayout);
        
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
		sparkEventHandler = new SparkEventHandler(MainActivity.this, mainLayout, ideaBucket, gridview, indexGrid);
		bucketHandler = new IdeaBucketEventHandler(MainActivity.this, ideaBucket, mainLayout, getFragmentManager());
    }
    
    public void initializeIndexGridLayout() {
    	final View indexGridLayout = findViewById(R.id.IndexGrid);
    	gridview = (GridView) indexGridLayout.findViewById(R.id.SparkGrid);
    	indexGrid = new IndexGrid();
    	
    	/*
    	 * HERE
    	 * RIGHT HERE
    	 * LOOK
    	 * HERE
    	 * Ok there needs to be an API call here to get the Jawns
    	 * 
    	 * Until then, Jawns will be a blank array
    	 */
    	Jawn[] j = new Jawn[0];
    	
    	indexGrid.initIndexGrid(j, gridview, MainActivity.this);
    }
    
    public void initIdeaBucket() {
    	ideaBucket = new IdeaBucket();
        View ideaBucketLayout = findViewById(R.id.IdeaBucket);
    	LinearLayout ideaGrid = (LinearLayout) ideaBucketLayout.findViewById(R.id.idea_bucket_linear);
        ideaBucket.initIdeaGrid(ideaGrid, MainActivity.this);
    }
    
    public void updateWeights(float sp, float fs, float ib) {
    	findViewById(R.id.WizardSection).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, sp));
		findViewById(R.id.FilterSettings).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, fs));
		findViewById(R.id.IdeaBucket).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, ib));
    }
	
	public void updateWizard(View v) {
		String tag = (String) v.getTag();
		if (tag.equals("openContentTypeChooser")) {
			
			char sparkType = 0;
			String user = "Placeholder_admin_dude";
			
			if (v.getId() == R.id.Inspiration_button) {
				sparkType = "I".charAt(0);
			} else if (v.getId() == R.id.What_if_button) {
				sparkType = "W".charAt(0);
			} else if (v.getId() == R.id.Problem_button) {
				sparkType = "P".charAt(0);
			}
			
			newSpark = new Spark(sparkType, user);
			
			sparkWizard.openContentTypeChooser(v);
			
		} else if (tag.equals("revertWizard")) {
			
			sparkWizard.revertWizard(v);
			newSpark = null;
			
		} else if (tag.equals("openContentEntry")) {
			
			char contentType = 0;
			
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
			
			newSpark.setContentType(contentType);
			
			sparkWizard.openContentEntry(v);
			
		} else if (tag.equals("submitSpark")) {
			
			EditText entryForm = (EditText) findViewById(R.id.content_entry_form);
			String content = entryForm.getText().toString();
			
			EditText tagsForm = (EditText) findViewById(R.id.tag_entry_form);
			String[] tags = tagsForm.getText().toString().split(", ");
			
			newSpark.setContent(content, tags);
			
			sparkWizard.submitSpark(v, newSpark);
			
			newSpark = null;
			
		}
	}
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
}
