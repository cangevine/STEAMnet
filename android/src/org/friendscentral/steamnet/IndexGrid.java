package org.friendscentral.steamnet;

import java.util.ArrayList;

import org.friendscentral.steamnet.BaseClasses.Jawn;

import APIHandlers.GetXJawns;
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
	
	private Jawn[] jawns;
	
	public void initIndexGrid(GridView g, Context c, boolean isIdeaDetailActivity) {
		//Use setter methods:
		gridview = g;
    	context = c;
    	
    	if (!isIdeaDetailActivity) {
    		gridview.setAdapter(new SpinnerAdapter(context, 16));
    	
    		@SuppressWarnings("unused")
    		GetXJawns task = new GetXJawns(50, gridview, this, context);
    	}
    	
    	Log.v("indexGrid", "Just called initIndexGrid! Success!");
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
		jawns = j;
		adapter.setJawns(jawns);
		adapter.notifyDataSetChanged();
	}
	
	public Jawn getJawnAt(int pos){
		return jawns[pos];
	}
	
	public void addJawn(Jawn j){
    	ArrayList<Jawn> jawnsArrayList = new ArrayList<Jawn>();
    	for(int i = 0; i < jawns.length; i++){
    		jawnsArrayList.add(jawns[i]);
    	}
    	jawnsArrayList.add(j);
    	Jawn[] newJawns = new Jawn[jawnsArrayList.size()];
    	for(int i = 0; i < jawnsArrayList.size(); i++){
    		newJawns[i] = jawnsArrayList.get(i);
    	}
    	jawns = newJawns;
    	adapter.setJawns(newJawns);
    	adapter.getView((jawns.length - 1), null, null);
    }
	
}
