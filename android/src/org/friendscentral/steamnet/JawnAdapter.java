package org.friendscentral.steamnet;

import java.util.ArrayList;

import BaseClasses.Spark;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class JawnAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] mThumbIds;
    private int image_size;
    private Spark[] sparks;

    public JawnAdapter(Context c, Spark[] s, int size) {
        mContext = c;
        mThumbIds = new Integer[s.length];
        image_size = size;
        sparks = s;
        initSparks(s);
    }
    
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
	    if(sparks[position].getContentType() == "I".charAt(0)){
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(image_size, image_size));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	
	        imageView.setImageResource(mThumbIds[position]);
	        return imageView;
	    } else {
	    	TextView textView;
	    	if(convertView == null){
	    		textView = new TextView(mContext);
	    		textView.setLayoutParams(new GridView.LayoutParams(image_size, image_size));
	    		//textView.setScaleType(TextView.ScaleType.CENTER_CROP);
	    		textView.setPadding(8, 8, 8, 8);
	    	} else {
	    		textView = (TextView) convertView;
	    	}
	    	
	    	textView.setText(sparks[position].getContent());
	    	return textView;
	    }
    }
    
    public void initSparks(Spark[] sparks) {
	    for (int i = 0; i < sparks.length; i++) {
	    	mThumbIds[i] = sparks[i].getId();
	    }
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    
    public Integer[] getIds() {
    	return mThumbIds;
    }
    
    public void setIds(Integer[] n) {
    	mThumbIds = n;
    }
    
    public void setSparks(Spark[] s){
    	sparks = s;
    }
    
    public void removeAtPosition(int pos) {
    	if (getIds().length >= 1) {
	    	Integer[] newSet = new Integer[getIds().length - 1];
	    	for (int i = 0; i < pos; i++) {
	    		newSet[i] = getIds()[i];
	    	}
	    	for (int i = pos; i < getIds().length - 1; i++) {
	    		newSet[i] = getIds()[i + 1];
	    	}
	    	setIds(newSet);
    	}
    }
    
    public void addAtPosition(int id, int pos) {
    	Integer[] newSet = new Integer[getIds().length - 1];
    	for (int i = 0; i < pos; i++) {
    		newSet[i] = getIds()[i];
    	}
    	newSet[pos] = id;
    	for (int i = pos + 1; i < getIds().length; i++) {
    		newSet[i] = getIds()[i - 1];
    	}
    	setIds(newSet);
    }
}