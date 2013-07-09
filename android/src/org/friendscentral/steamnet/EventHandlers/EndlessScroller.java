package org.friendscentral.steamnet.EventHandlers;

import org.friendscentral.steamnet.FilterSettings;
import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;

import APIHandlers.AddXIdeas;
import APIHandlers.AddXJawns;
import APIHandlers.AddXSparks;
import android.app.ListActivity;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

public class EndlessScroller extends ListActivity implements OnScrollListener {
	FilterSettings filterSettings;
	GridView gridview;
	IndexGrid indexgrid;
	
	public EndlessScroller(FilterSettings f, GridView g, IndexGrid i) {
		filterSettings = f;
		gridview = g;
		indexgrid = i;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisible, int visibleCount, int totalCount) {

	    boolean loadMore = firstVisible + visibleCount >= totalCount;

	    if (loadMore) {
	    	//View c = gridview.getChildAt(0);
			//int scrolly = -c.getTop() + gridview.getFirstVisiblePosition() * c.getHeight();
	    	
	    	int newTotal = totalCount + 16;
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
			
			//gridview.setSelection(scrolly);
	    }
	    
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}