package org.friendscentral.steamnet;

import android.content.Context;
import android.util.Log;
import android.widget.GridView;

public class IndexGrid {
	GridView gridview;
	Context context;
	ImageAdapter adapter;
	
	private SimpleSpark[] sparks = {
			new SimpleSpark("Laying down!", R.drawable.sample_1),
			new SimpleSpark("In a coat!", R.drawable.sample_2), 
			new SimpleSpark("In a basket!", R.drawable.sample_6), 
			new SimpleSpark("Laying down!", R.drawable.sample_1),
			new SimpleSpark("In a coat!", R.drawable.sample_2), 
			new SimpleSpark("In a basket!", R.drawable.sample_6), 
			new SimpleSpark("Laying down!", R.drawable.sample_1),
			new SimpleSpark("In a coat!", R.drawable.sample_2), 
			new SimpleSpark("In a basket!", R.drawable.sample_6), 
			new SimpleSpark("Laying down!", R.drawable.sample_1),
			new SimpleSpark("In a coat!", R.drawable.sample_2), 
			new SimpleSpark("In a basket!", R.drawable.sample_6), 
			new SimpleSpark("Laying down!", R.drawable.sample_1),
			new SimpleSpark("In a coat!", R.drawable.sample_2), 
			new SimpleSpark("In a basket!", R.drawable.sample_6), 
			new SimpleSpark("Laying down!", R.drawable.sample_1),
			new SimpleSpark("In a coat!", R.drawable.sample_2), 
			new SimpleSpark("In a basket!", R.drawable.sample_6), 
	};
	
	public void initIndexGrid(GridView g, Context c) {
		//Use setter methods:
		gridview = g;
    	context = c;
    	adapter = new ImageAdapter(gridview.getContext(), sparks, 200);
    	
    	gridview.setAdapter(adapter);
    	Log.v("indexGrid", "Just called initIndexGrid! Success!");
	}
	
	public SimpleSpark[] getSparks() {
		return sparks;
	}
	
	public ImageAdapter getAdapter() {
		return adapter;
	}
	
}
