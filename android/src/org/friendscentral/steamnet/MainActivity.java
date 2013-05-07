package org.friendscentral.steamnet;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    static final String EXTRA_MESSAGE = null;
	private static final int INVISIBLE = 4;
	private static final int GONE = 8;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Initialize the grid of Sparks:
        initializeIndexGridLayout();
        
        //Initialize the Wizard Fragment:
        FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		SparkTypeChooser stc = new SparkTypeChooser();
		ft.add(R.id.WizardSection, stc);
		ft.commit();
        
        //Set up the auto-weighting for each Sidebar componant:
			//Needs updating, to include descendants...
        findViewById(R.id.WizardSection).setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
				updateWeights(5, 3, 1);
				//Set things invisible
			}
        });
        
        findViewById(R.id.IdeaBucket).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateWeights(1, 3, 5);
				//Set things invisible
			}
        });
        
        findViewById(R.id.FilterSettings).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateWeights(1, 3, 1);
				//Set things invisible
			}
        });
    }
	
	//Methods for the Wizard Fragments:
		//Very similar, could probably be consolidated
	public void openInspirationWizard(View v) {
		updateWeights(5, 3, 1);
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ContentTypeChooser ctc = new ContentTypeChooser();
		ft.replace(R.id.WizardSection, ctc);
		ft.addToBackStack(null);
		ft.commit();
	}
	public void revertWizard(View v) {
		updateWeights(1, 3, 1);
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		SparkTypeChooser stc = new SparkTypeChooser();
		ft.replace(R.id.WizardSection, stc);
		ft.addToBackStack(null);
		ft.commit();
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void initializeIndexGridLayout() {
    	View indexGridLayout = findViewById(R.id.IndexGrid);
    	final GridView gridview = (GridView) indexGridLayout.findViewById(R.id.SparkGrid);
    	final IndexGrid ig = new IndexGrid();
    	ig.initIndexGrid(gridview, MainActivity.this);
    	
    	gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                openDetailView(ig.getSparks()[position]);
            }
        });
    }
    
    public void openDetailView(SimpleSpark s) {
		Bundle b = new Bundle();
		b.putString("Name", s.getName());
		b.putInt("Id", s.getId());
    	Intent intent = new Intent(this, DetailActivity.class);
    	intent.putExtra(EXTRA_MESSAGE, b);
    	startActivity(intent);
    }
    
    public void updateWeights(float sp, float fs, float ib) {
    	findViewById(R.id.WizardSection).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, sp));
		findViewById(R.id.FilterSettings).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, fs));
		findViewById(R.id.IdeaBucket).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, ib));
    }
    
}
