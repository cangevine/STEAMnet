package org.friendscentral.steamnet;

import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.EventHandlers.EndlessScroller;

import APIHandlers.GetXJawns;
import APIHandlers.LoadJawnsFromCache;
import APIHandlers.MultimediaLoader;
import APIHandlers.UserLoader;
import CachingHandlers.JawnsDataSource;
import android.content.Context;
import android.util.Log;
import android.widget.GridView;

// TODO Remove class
/**
 * 
 * @author sambeckley 
 * @author aqeelphillips
 * 
 */
public class IndexGrid {
	GridView gridview;
	Context context;
	JawnAdapter adapter;
	JawnsDataSource datasource;
	EndlessScroller endlessScroller;
	
	private Jawn[] jawns;
	
	public void initIndexGrid(GridView g, Context c, JawnsDataSource d, boolean isIdeaDetailActivity) {
		Log.v("indexGrid", "Just called initIndexGrid!");
		
		gridview = g;
    	context = c;
    	datasource = d;
    	
    	if (!isIdeaDetailActivity) {
    		if (datasource.getAllJawns() != null && datasource.getAllJawns().length > 0) {
    			Log.v("Proof that isn't null:", datasource.getAllJawns()[0].toString());
    			jawns = new Jawn[0];
    			setAdapter(new JawnAdapter(context, new Jawn[0], 200));
    			new LoadJawnsFromCache(datasource, IndexGrid.this, context);
    		} else {
    			gridview.setAdapter(new SpinnerAdapter(context, 16));
    			new GetXJawns(50, gridview, this, context);
    		}
    	}
	}
	
	public Jawn[] getJawns() {
		return jawns;
	}
	
	public JawnAdapter getAdapter() {
		return adapter;
	}
	
	public void setAdapter(JawnAdapter j){
		adapter = j;
		gridview.setAdapter(adapter);
	}
	
	public void setJawns(Jawn[] j){
		Log.v("IndexGrid", "setJawns called");
		
		jawns = j;
		adapter.setJawns(jawns);
		adapter.notifyDataSetChanged();
		
		datasource.deleteAllJawnsInDb();
		for (Jawn jawn : j)
			datasource.addJawnToDb(jawn);
	}
	
	public Jawn getJawnAt(int pos){
		return jawns[pos];
	}
	
	public void addJawn(Jawn j) {
    	Jawn[] newJawns = new Jawn[jawns.length+1];
    	newJawns[0] = j;
    	for (int i = 1; i < newJawns.length; i++) {
    		newJawns[i] = jawns[i - 1];
    	}
    	jawns = newJawns;
    	adapter.setJawns(newJawns);
    	//adapter.getView((jawns.length - 1), null, null);
    	adapter.notifyDataSetChanged();
    }
	
	public void setScrollListener(EndlessScroller e) {
		endlessScroller = e;
	}
	
	public EndlessScroller getScrollListener() {
		return endlessScroller;
	}
	
	public Context getContext() {
		return context;
	}
	
	public GridView getGridView() {
		return gridview;
	}
}
