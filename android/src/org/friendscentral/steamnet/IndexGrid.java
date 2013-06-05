package org.friendscentral.steamnet;

import java.util.ArrayList;

import APIHandlers.RetrieveDataTaskGetXSparks;
import BaseClasses.Spark;

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
	
	private Spark[] sparks;
	
	public void initIndexGrid(GridView g, Context c) {
		//Use setter methods:
		gridview = g;
    	context = c;
    	@SuppressWarnings("unused")
    	RetrieveDataTaskGetXSparks task = new RetrieveDataTaskGetXSparks(15, gridview, this);
    	
    	Log.v("indexGrid", "Just called initIndexGrid! Success!");
	}
	
	public Spark[] getSparks() {
		return sparks;
	}
	
	public JawnAdapter getAdapter() {
		return adapter;
	}
	
	public void setAdapter(JawnAdapter j){
		adapter = j;
		gridview.setAdapter(adapter);
	}
	
	public void setSparks(Spark[] s){
		sparks = s;
	}
	
	public void addSpark(Spark s){
    	ArrayList<Spark> sparksArrayList = new ArrayList<Spark>();
    	for(int i = 0; i < sparks.length; i++){
    		sparksArrayList.add(sparks[i]);
    	}
    	sparksArrayList.add(s);
    	Spark[] newSparks = new Spark[sparksArrayList.size()];
    	for(int i = 0; i < sparksArrayList.size(); i++){
    		newSparks[i] = sparksArrayList.get(i);
    	}
    	sparks = newSparks;
    	adapter.setSparks(newSparks);
    	adapter.getView((sparks.length - 1), null, null);
    }
	
}
