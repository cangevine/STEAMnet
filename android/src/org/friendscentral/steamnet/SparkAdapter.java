/*
 * Oops, also antiquated
 */

package org.friendscentral.steamnet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SparkAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] mThumbIds;
    private Spark[] sparks;
    private int image_size;

    public SparkAdapter(Context c, Spark[] s, int size) {
        mContext = c;
        //mThumbIds = new Integer[s.length];
        image_size = size;
        //initSparks(s);
        sparks = s;
    }
    
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	Spark spark = getSparks()[position];
    	char con = spark.getContentType();
    	View v = null;
    	
    	if (con == "I".charAt(0)) {
    		
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(image_size, image_size));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        /*
	         * This part needs to be updated with the imgur API
	         * Something like imageView.setImageResource(imgurAPI.getImage())
	         * Might run into some trouble because its not a drawable?
	         */
	        imageView.setImageResource(R.drawable.btn_blue_picture);
	        v = imageView;
	        
    	} else if (con == "V".charAt(0)) {
    		/*
    		 * Normally within an app, embedded Youtube videos either begin playing or launch the Youtube app on click
    		 * Within the spark grid, we don't need that functionality
    		 * Therefore the manifestation of the video will just be a thumbnail (in the grid)
    		 * The thumbnail will need to be received from the Youtube API, however
    		 * In the Detail View, it will be a normal, dynamic video
    		 */
    		
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(image_size, image_size));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        /*
	         * This part needs to be updated with the youtube API
	         * Something like setImageResource(youtubeAPI.getThumbnail())
	         */
	        imageView.setImageResource(R.drawable.btn_blue_picture);
	        v = imageView;
	        
    	} else if (con == "A".charAt(0)) {
    		/*
    		 * Audio is pretty much the same deal as the Video.
    		 * It's a little different in that most of the soundcloud generated thumbnails are gonna be pretty similar.
    		 * For that reason, this view is actually a layout, housing the Title of the spark and the thumbnail
    		 */
    		
    		LinearLayout layout = new LinearLayout(mContext);
    		
    		/*
    		 * First make the title:
    		 */
    		TextView title;
    		if (convertView == null) {  // if it's not recycled, initialize some attributes
	            title = new TextView(mContext);
	            title.setLayoutParams(new GridView.LayoutParams(50, image_size));
	            title.setPadding(8, 8, 8, 8);
	        } else {
	            title = (TextView) convertView;
	        }
    		
    		/*
    		 * Uncomment this once it's working:
    		 */
    		//title.setText(soundcloudAPI.getTrackTitle, 0, 200);
    		layout.addView(title);
    		
    		/*
    		 * Then add the thumbnail:
    		 */
    		ImageView imageView;
	        if (convertView == null) {
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(image_size - 50, image_size - 50));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        /*
    		 * Placeholder until Soundcloud API is working
    		 */
	        imageView.setImageResource(R.drawable.btn_blue_audio);
	        layout.addView(imageView);
	        
    		v = layout;
    		
    	} else if (con == "T".charAt(0)) {
    		
    		TextView textview;
    		if (convertView == null) {  // if it's not recycled, initialize some attributes
	            textview = new TextView(mContext);
	            textview.setLayoutParams(new GridView.LayoutParams(image_size, image_size));
	            textview.setPadding(8, 8, 8, 8);
	        } else {
	            textview = (TextView) convertView;
	        }
    		
    		textview.setText(spark.getContent().toCharArray(), 0, 200);
    		v = textview;
    		
    	} else if (con == "C".charAt(0)) {
    		
    		/*
    		 * At the moment, code snippets are handled just like text. Eventually they will make use of Github 
    		 */
    		TextView textview;
    		if (convertView == null) {  // if it's not recycled, initialize some attributes
	            textview = new TextView(mContext);
	            textview.setLayoutParams(new GridView.LayoutParams(image_size, image_size));
	            textview.setPadding(8, 8, 8, 8);
	        } else {
	            textview = (TextView) convertView;
	        }
    		
    		textview.setText(spark.getContent().toCharArray(), 0, 200);
    		v = textview;
    		
    	} else if (con == "L".charAt(0)) {
    		/*
    		 * Not sure how to handle this one
    		 * In the mockup, Colin had it as a screenshot of the page.
    		 * IF that proves too difficult to generate, we can also use the fav_icon with the website
    		 * Either way, it looks like it'll turn out similar to the soundcloud implementation
    		 */
    		
    		LinearLayout layout = new LinearLayout(mContext);
    		
    		/*
    		 * First make the title:
    		 */
    		TextView title;
    		if (convertView == null) {  // if it's not recycled, initialize some attributes
	            title = new TextView(mContext);
	            title.setLayoutParams(new GridView.LayoutParams(50, image_size));
	            title.setPadding(8, 8, 8, 8);
	        } else {
	            title = (TextView) convertView;
	        }
    		
    		/*
    		 * Get the page title
    		 * something like "Wikipedia - Kittens"
    		 */
    		//title.setText(soundcloudAPI.getTrackTitle, 0, 200);
    		layout.addView(title);
    		
    		/*
    		 * Then add the thumbnail:
    		 */
    		ImageView imageView;
	        if (convertView == null) {
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(image_size - 50, image_size - 50));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        /*
    		 * This is what will be set to the favicon or the screenshot:
    		 */
	        imageView.setImageResource(R.drawable.btn_blue_audio);
	        layout.addView(imageView);
	        
    		v = layout;
    		
    	}
    	
    	return v;
    }
    
    /* public void initSparks(Spark[] s) {
	    for (int i = 0; i < s.length; i++) {
	    	mThumbIds[i] = s[i].getId();
	    }
    } */

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
    
    public Spark[] getSparks() {
    	return sparks;
    }
    
    public void setSparks(Spark[] s) {
    	sparks = s;
    }
    
    public void removeAtPosition(int pos) {
    	if (getSparks().length >= 1) {
	    	Spark[] newSet = new Spark[getSparks().length - 1];
	    	for (int i = 0; i < pos; i++) {
	    		newSet[i] = getSparks()[i];
	    	}
	    	for (int i = pos; i < getSparks().length - 1; i++) {
	    		newSet[i] = getSparks()[i + 1];
	    	}
	    	setSparks(newSet);
    	}
    }
    
    public void addAtPosition(Spark s, int pos) {
    	Spark[] newSet = new Spark[getSparks().length - 1];
    	for (int i = 0; i < pos; i++) {
    		newSet[i] = getSparks()[i];
    	}
    	newSet[pos] = s;
    	for (int i = pos + 1; i < getSparks().length; i++) {
    		newSet[i] = getSparks()[i - 1];
    	}
    	setSparks(newSet);
    }
}