package org.friendscentral.steamnet;

import android.content.Context;
import android.util.Log;
import android.widget.GridView;

public class IndexGrid {
	GridView gridview;
	Context context;
	
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
	
	public void initIndexGrid(GridView g, Context c, boolean reversed) {
		//Use setter methods:
		gridview = g;
    	context = c;
    	
    	gridview.setAdapter(new ImageAdapter(gridview.getContext(), sparks, reversed));
    	Log.v("indexGrid", "Just called initIndexGrid! Success!");
	}
	
	public SimpleSpark[] getSparks() {
		return sparks;
	}
	
}
