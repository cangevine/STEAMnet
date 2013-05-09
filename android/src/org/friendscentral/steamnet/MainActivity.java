/*
 * 
 * Current problem:
 * *Drag listener terminates on the "end" case 
 * *The x and y of the coordinates cannot be acquired once the case is ended
 * *trying to continually get the x and y coordinates at the beginning of each drag event
 * *Suggested way to do this is to use a Touch listener, but not sure where to apply it and which view to append it to
 * 
 */

package org.friendscentral.steamnet;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    static final String EXTRA_MESSAGE = null;
    
    IdeaBucket ideaBucket;
    SparkWizard sparkWizard;
    LinearLayout mainLayout;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.MainLayout);
        
        //Initialize the grid of Sparks:
        initializeIndexGridLayout();
        
        //Initialize Idea Bucket:
        initIdeaBucket();
        
        //Initialize the Wizard Fragment:
        FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		SparkTypeChooser stc = new SparkTypeChooser();
		ft.add(R.id.WizardSection, stc);
		ft.commit();
		
		sparkWizard = new SparkWizard(mainLayout, getFragmentManager());
    }
	
	public void openDetailView(SimpleSpark s) {
		Bundle b = new Bundle();
		b.putString("Name", s.getName());
		b.putInt("Id", s.getId());
    	Intent intent = new Intent(this, DetailActivity.class);
    	intent.putExtra(EXTRA_MESSAGE, b);
    	startActivity(intent);
    }
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void initializeIndexGridLayout() {
    	final View indexGridLayout = findViewById(R.id.IndexGrid);
    	final GridView gridview = (GridView) indexGridLayout.findViewById(R.id.SparkGrid);
    	final IndexGrid ig = new IndexGrid();
    	ig.initIndexGrid(gridview, MainActivity.this);
    	
    	gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //openDetailView(ig.getSparks()[position]);
            	ig.getAdapter().removeAtPosition(position);
            	ig.getAdapter().notifyDataSetChanged();
            }
        });
    	
    	gridview.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> adapterView, View view,
					int pos, long id) {
				ClipData data = ClipData.newPlainText("", "");
		        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		        view.startDrag(data, shadowBuilder, view, 0);
		        
		        SparkDragListener sdl = new SparkDragListener();
		        sdl.setPos(pos);
		        sdl.setContext(ig);
		        
		        findViewById(R.id.Sidebar).setOnDragListener(sdl);
		        
				return true;
			}
    	});

    }
    
    private class SparkDragListener implements OnDragListener {
    	int pos;
    	IndexGrid indexGridContext;
    	
		@Override
		public boolean onDrag(View view, DragEvent dragEvent) {
			switch (dragEvent.getAction()) {
		      	case DragEvent.ACTION_DRAG_STARTED:
		      		//do nothing
		      		break;
		      	case DragEvent.ACTION_DRAG_ENTERED:
		      		findViewById(R.id.IdeaBucket).setBackgroundResource(R.drawable.idea_bucket_drop_background);
		      		break;
		      	case DragEvent.ACTION_DRAG_EXITED:
		      		findViewById(R.id.IdeaBucket).setBackgroundResource(0);
		      		break;
		      	case DragEvent.ACTION_DROP:
		      		findViewById(R.id.IdeaBucket).setBackgroundResource(0);
		      		ideaBucket.addSpark(indexGridContext.getSparks()[pos]);
		    		break;
		      	case DragEvent.ACTION_DRAG_ENDED:
		      		//nothing
		      	default:
		      		break;
		      }
			return false;
		}
		
		public void setPos(int n) {
			pos = n;
		}
		
		public void setContext(IndexGrid i) {
			indexGridContext = i;
		}
 
    }
    
    public void initIdeaBucket() {
    	ideaBucket = new IdeaBucket();
        View ideaBucketLayout = findViewById(R.id.IdeaBucket);
    	LinearLayout ideaGrid = (LinearLayout) ideaBucketLayout.findViewById(R.id.idea_bucket_linear);
        ideaBucket.initIdeaGrid(ideaGrid, MainActivity.this);
        
        for (int i = 0; i < ideaBucket.getImageViews().length; i++) {
        	IdeaBucketSparkListener newSparkListener = new IdeaBucketSparkListener();
        	newSparkListener.setPos(i);
        	ideaBucket.getImageViews()[i].setOnLongClickListener(newSparkListener);
        }
    }
    
    private class IdeaBucketSparkListener implements OnLongClickListener  {
    	int pos = -1;
    	
    	@Override
    	public boolean onLongClick(View view) {
    		ClipData data = ClipData.newPlainText("", "");
	        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	        view.startDrag(data, shadowBuilder, view, 0);
	        
	        FragmentTransaction ft = getFragmentManager().beginTransaction();
    		TrashCan tc = new TrashCan();
    		ft.replace(R.id.WizardSection, tc);
    		ft.addToBackStack(null);
    		ft.commit();
    		
    		TrashCanListener tcl = new TrashCanListener();
	        tcl.setPos(pos);
	        
	        findViewById(R.id.Sidebar).setOnDragListener(tcl);
    		
    		return false;
    	}
    	
    	public void setPos(int p) {
    		pos = p;
    	}

    }
    
    private class TrashCanListener implements OnDragListener {
    	int pos;
    	
		@Override
		public boolean onDrag(View view, DragEvent dragEvent) {
			//TextView tv = (TextView) findViewById(R.id.trashCanTextView);
			
			switch (dragEvent.getAction()) {
		      	case DragEvent.ACTION_DRAG_STARTED:
		      		//do nothing
		      		break;
		      	case DragEvent.ACTION_DRAG_ENTERED:
		      		//tv.setTextColor(Color.WHITE);
		      		break;
		      	case DragEvent.ACTION_DRAG_EXITED:
		      		//tv.setTextColor(Color.BLACK);
		      		break;
		      	case DragEvent.ACTION_DROP:
		      		ideaBucket.removeSpark(pos);
		      		
		      		FragmentTransaction ft = getFragmentManager().beginTransaction();
		    		SparkTypeChooser stc = new SparkTypeChooser();
		    		ft.replace(R.id.WizardSection, stc);
		    		ft.addToBackStack(null);
		    		ft.commit();
		    		
		    		break;
		      	case DragEvent.ACTION_DRAG_ENDED:
		      		//nothing
		      	default:
		      		break;
		      }
			return false;
		}
		
		public void setPos(int n) {
			pos = n;
		}
 
    }
    
    public void updateWeights(float sp, float fs, float ib) {
    	findViewById(R.id.WizardSection).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, sp));
		findViewById(R.id.FilterSettings).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, fs));
		findViewById(R.id.IdeaBucket).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, ib));
    }
    
	public void updateFragment(Fragment f) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.WizardSection, f);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public void openContentTypeChooser(View v) {
		sparkWizard.openContentTypeChooser(v);
	}
	
	public void revertWizard(View v) {
		sparkWizard.revertWizard(v);
	}
	
	public void openContentEntry(View v) {
		sparkWizard.openContentEntry(v);
	}
	
	public void submitSpark(View v) {
		sparkWizard.submitSpark(v);
	}
	
	public SparkWizard getSparkWizard() {
		return sparkWizard;
	}
	
}
