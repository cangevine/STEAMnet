package org.friendscentral.steamnet.EventHandlers;

import org.friendscentral.steamnet.FilterSettings;
import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.Activities.MainActivity;

import APIHandlers.AddXIdeas;
import APIHandlers.AddXJawns;
import APIHandlers.AddXSparks;
import APIHandlers.RefreshXJawns;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EndlessScroller extends ListActivity implements OnScrollListener {
	FilterSettings filterSettings;
	GridView gridview;
	IndexGrid indexgrid;
	Context context;
	ActionBar actionBar;
	MainActivity main;
	
	boolean reachedEnd;
	boolean refreshable;
	boolean refreshOnRelease;
	boolean isRefreshing;
	int previousLastItem;
	
	int jawnsInDB;
	
	float y;
	float dy;
	
	public EndlessScroller(FilterSettings f, GridView g, IndexGrid i, Context c, int jidb) {
		Log.v("EndlessScroller", "EndlessScroller attached to GridView");
		
		reachedEnd = false;
		filterSettings = f;
		gridview = g;
		indexgrid = i;
		context = c;
		main = (MainActivity) context;
		actionBar = main.getActionBar();
		jawnsInDB = jidb;
		
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
	    
	    
	    boolean loadMore = firstVisible + visibleCount + 14 >= totalCount;
	    if (loadMore && !isRefreshing) {
			if (totalCount < jawnsInDB) {
				isRefreshing = true;
				boolean sparks = filterSettings.getSparkBoxVal();
				boolean ideas = filterSettings.getIdeaBoxVal();
				if (sparks && ideas) {
					new AddXJawns(16, gridview, indexgrid, totalCount, EndlessScroller.this); 
				} else if (sparks && !ideas) {
					new AddXSparks(16, gridview, indexgrid, totalCount, EndlessScroller.this); 
				} else if (!sparks && ideas) {
					new AddXIdeas(16, gridview, indexgrid, totalCount, EndlessScroller.this);
				}
			} else {
				reachedEnd = true;
			}
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
			LinearLayout customBar = (LinearLayout) actionBar.getCustomView();
			TextView header = (TextView) customBar.findViewById(R.id.activity_name);
			switch (motionEvent.getAction()) {
				case MotionEvent.ACTION_UP:
					boolean refreshed = false;
					header.setText("STEAMnet");
					dy = motionEvent.getY();
					if (dy - y > 250) {
						//filterSettings.sparkBoxChange('Q');
						new RefreshXJawns(50, gridview, indexgrid, context, filterSettings.getSparkBoxVal(), filterSettings.getIdeaBoxVal()); 
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
						header.setText(abTitle);
						return true;
					} else {
						header.setText("STEAMnet");
					}
					break;
			}
			
			return false;
		}
		
	}
	
	public void doneRefreshing() {
		isRefreshing = false;
	}
	
	public boolean hasReachedEnd() {
		return reachedEnd;
	}

}