package org.friendscentral.steamnet;

import android.content.Context;
import android.util.Log;
import android.widget.GridView;

public class IndexGrid {
	GridView gridview;
	Context context;
	JawnAdapter adapter;
	
	private SimpleSpark[] sparks = {
			new SimpleSpark("Laying down!", R.drawable.sample_1, "Picture"),
			new SimpleSpark("In a coat!", R.drawable.sample_2, "Picture"), 
			new SimpleSpark("In a basket!", R.drawable.sample_6, "Picture"), 
			new SimpleSpark("Laying down!", R.drawable.sample_1, "Picture"),
			new SimpleSpark("In a coat!", R.drawable.sample_2, "Picture"), 
			new SimpleSpark("In a basket!", R.drawable.sample_6, "Picture"), 
			new SimpleSpark("Laying down!", R.drawable.sample_1, "Picture"),
			new SimpleSpark("In a coat!", R.drawable.sample_2, "Picture"), 
			new SimpleSpark("In a basket!", R.drawable.sample_6, "Picture"), 
			new SimpleSpark("Laying down!", R.drawable.sample_1, "Picture"),
			new SimpleSpark("In a coat!", R.drawable.sample_2, "Picture"), 
			new SimpleSpark("In a basket!", R.drawable.sample_6, "Picture"), 
			new SimpleSpark("Laying down!", R.drawable.sample_1, "Picture"),
			new SimpleSpark("In a coat!", R.drawable.sample_2, "Picture"), 
			new SimpleSpark("In a basket!", R.drawable.sample_6, "Picture"), 
			new SimpleSpark("Laying down!", R.drawable.sample_1, "Picture"),
			new SimpleSpark("In a coat!", R.drawable.sample_2, "Picture"), 
			new SimpleSpark("In a basket!", R.drawable.sample_6, "Picture"), 
	};
	
	public void initIndexGrid(Jawn[] j, GridView g, Context c) {
		//Use setter methods:
		gridview = g;
    	context = c;
    	adapter = new JawnAdapter(gridview.getContext(), j, 200);
    	
    	gridview.setAdapter(adapter);
    	Log.v("indexGrid", "Just called initIndexGrid! Success!");
	}
	
	public SimpleSpark[] getSparks() {
		return sparks;
	}
	
	public JawnAdapter getAdapter() {
		return adapter;
	}
	
}
