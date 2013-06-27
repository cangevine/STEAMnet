package org.friendscentral.steamnet;

import java.util.ArrayList;

import org.friendscentral.steamnet.BaseClasses.Jawn;
import APIHandlers.RetrieveDataTaskGetXSparks;

import android.content.Context;
import android.util.Log;
import android.widget.GridView;

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
	
	public void initIndexGrid(GridView g, Context c) {
		//Use setter methods:
		gridview = g;
    	context = c;
    	
    	gridview.setAdapter(new SpinnerAdapter(context));
    	
    	@SuppressWarnings("unused")
    	RetrieveDataTaskGetXSparks task = new RetrieveDataTaskGetXSparks(15, gridview, this);
    	
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
