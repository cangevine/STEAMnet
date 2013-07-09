package org.friendscentral.steamnet.EventHandlers;

import org.friendscentral.steamnet.FilterSettings;
import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.Activities.MainActivity;

import APIHandlers.AddXIdeas;
import APIHandlers.AddXJawns;
import APIHandlers.AddXSparks;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

public class EndlessScroller extends ListActivity implements OnScrollListener {
	FilterSettings filterSettings;
	GridView gridview;
	IndexGrid indexgrid;
	Context context;
	ActionBar actionBar;
	MainActivity main;
	
	boolean refreshable;
	boolean refreshOnRelease;
	int previousLastItem;
	
	float y;
	float dy;
	
	public EndlessScroller(FilterSettings f, GridView g, IndexGrid i, Context c) {
		filterSettings = f;
		gridview = g;
		indexgrid = i;
		context = c;
		main = (MainActivity) context;
		actionBar = main.getActionBar();
		
		y = -1;
		dy = -1;
		refreshable = true;
		refreshOnRelease = false;
		previousLastItem = gridview.getLastVisiblePosition();
		
		gridview.setOnTouchListener(new Refresher());
	}

	@Override
	public void onScroll(AbsListView view, int firstVisible, int visibleCount, int totalCount) {
	    
	    View firstChild = gridview.getChildAt(0);
	    if (firstChild != null) {
	    	int scrolly = -firstChild.getTop() + firstVisible * firstChild.getHeight();
	    	refreshable = scrolly <= 50;
	    } else {
	    	refreshable = false;
	    }
	    if (refreshable) 
	    	return;
	    
	    
	    boolean loadMore = firstVisible + visibleCount + 15 >= totalCount;
	    if (loadMore) {
	    	
	    	boolean sparks = filterSettings.getSparkBoxVal();
	    	boolean ideas = filterSettings.getIdeaBoxVal();
	    	if (sparks && ideas) {
	    		AddXJawns a = new AddXJawns(16, gridview, indexgrid, totalCount); 
	    	} else if (sparks && !ideas) {
	    		AddXSparks a = new AddXSparks(16, gridview, indexgrid, totalCount); 
	    	} else if (!sparks && ideas) {
	    		AddXIdeas a = new AddXIdeas(16, gridview, indexgrid, totalCount);
	    	}
	    	JawnAdapter ja = indexgrid.getAdapter();
			ja.notifyDataSetChanged();
			indexgrid.setJawns(ja.getJawns());
			return;
			
			//gridview.setSelection(scrolly);
	    }
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//Nothing
	}
	
	private class Refresher implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (!refreshable) {
				return false;
			}
			//if (!gridview.getAdapter().getClass().getName().equals("org.friendscentral.steamnet.JawnAdapter")) {
			//	Log.v("ADAPTER", "IS A JAWN ADAPTER");
			//}
			
			switch (motionEvent.getAction()) {
				case MotionEvent.ACTION_UP:
					boolean refreshed = false;
					actionBar.setTitle("STEAMnet");
					dy = motionEvent.getY();
					if (dy - y > 250) {
						filterSettings.sparkBoxChange('Q');
						refreshed = true;
					} else if (dy > y) {
						main.getSparkEventHandler().initializeIndexGridLayout();
					}
					y = -1;
					dy = -1;
					return refreshed;
				case MotionEvent.ACTION_DOWN:
					y = motionEvent.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					float ddy = motionEvent.getY();
					//Verify that the user is moving downward:
					if (ddy > y && ddy - y > 25) {
						String abTitle = "Pull down to refresh! ";
						abTitle += Math.min((int) (((ddy - y) / 250)*100), 100);
						abTitle += "%";
						actionBar.setTitle(abTitle);
						return true;
					} else {
						actionBar.setTitle("STEAMnet");
					}
					break;
			}
			
			return false;
		}
		
	}

}