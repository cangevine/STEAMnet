package org.friendscentral.steamnet;

import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class JawnAdapter extends BaseAdapter {
    private static final int VERTICAL = 1;
	private Context mContext;
    private Jawn[] jawns;
    private int image_size;

    /**
     * 
     * @param Context
     * @param Jawn[]
     * @param int - image size
     */
    public JawnAdapter(Context c, Jawn[] j, int size) {
        mContext = c;
        Log.v("LENGTH", Integer.toString(j.length));
        image_size = size;
        //initSparks(s);
        jawns = j;
    }

	// create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	View v = null;
    	//									== 'S'
    	if (getJawnAt(position).getType() == "S".charAt(0)) {
	    	Spark spark = getJawnAt(position).getSelfSpark();
	    	char con = spark.getContentType();
	    	View contentView = null;
	    	LinearLayout sparkInfo = new LinearLayout(mContext);
	    	sparkInfo.setOrientation(VERTICAL);
	    	sparkInfo.setBackgroundColor(Color.WHITE);
	    	sparkInfo.setPadding(8, 8, 8, 8);
	    	
	    	//Prelim stuff for the spark:
	    	LinearLayout layout = new LinearLayout(mContext);
    		layout.setOrientation(VERTICAL);
	    	
    		LinearLayout header = new LinearLayout(mContext);
	    	TextView headerTitle;
	        headerTitle = new TextView(mContext);
	        headerTitle.setTextSize(20);
	        headerTitle.setWidth(0);
	        LayoutParams titleParams = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 9f);
	        headerTitle.setLayoutParams(titleParams);
	        header.addView(headerTitle);
	        
	        layout.addView(header);
	    	
	    	if (con == "P".charAt(0)) {
		        headerTitle.setText("Picture");
	    		
		        ImageView imageView;
		        imageView = new ImageView(mContext);
		        imageView.setLayoutParams(new GridView.LayoutParams(image_size, image_size));
		        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		        
		        /*
		         * This part needs to be updated with the imgur API
		         * Something like imageView.setImageResource(imgurAPI.getImage())
		         * Might run into some trouble because its not a drawable?
		         */
		        imageView.setImageResource(R.drawable.symbol_image);
		        
		        layout.addView(imageView);
		        
		        contentView = layout;
		        
	    	} else if (con == "V".charAt(0)) {
		        headerTitle.setText("Video");
	    		
	    		/*
	    		 * Normally within an app, embedded Youtube videos either begin playing or launch the Youtube app on click
	    		 * Within the spark grid, we don't need that functionality
	    		 * Therefore the manifestation of the video will just be a thumbnail (in the grid)
	    		 * The thumbnail will need to be received from the Youtube API, however
	    		 * In the Detail View, it will be a normal, dynamic video
	    		 */
	    		
		        ImageView imageView;
		        imageView = new ImageView(mContext);
		        imageView.setLayoutParams(new GridView.LayoutParams(image_size, image_size));
		        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		        imageView.setPadding(8, 8, 8, 8);
		        /*
		         * This part needs to be updated with the youtube API
		         * Something like setImageResource(youtubeAPI.getThumbnail())
		         */
		        imageView.setImageResource(R.drawable.symbol_video);
		        
		        layout.addView(imageView);
		        contentView = layout;
		        
	    	} else if (con == "A".charAt(0)) {
	    		/*
	    		 * Audio is pretty much the same deal as the Video.
	    		 * It's a little different in that most of the soundcloud generated thumbnails are gonna be pretty similar.
	    		 * 
	    		 * First make the title:
	    		 * Uncomment this and clean it up once it's working:
	    		 * String audioTitle = soundcloudAPI.getTrackTitle();
	    		 * headerTitle.setText("Audio: "+audioTitle, 0, Math.min(audioTitle.length(), 100);
	    		 */
	    		String audioTitle = "unknown";
		        headerTitle.setText("Audio");
	    		
	    		/*
	    		 * Then add the thumbnail:
	    		 */
	    		ImageView imageView;
		        imageView = new ImageView(mContext);
		        imageView.setLayoutParams(new GridView.LayoutParams(image_size, image_size));
		        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		        imageView.setPadding(8, 8, 8, 8);
		        	
		        /*
	    		 * Placeholder until Soundcloud API is working
	    		 */
		        imageView.setImageResource(R.drawable.symbol_link);
		        layout.addView(imageView);
		        
	    		contentView = layout;
	    		
	    	} else if (con == "T".charAt(0) /* DIRTY FIX!!!! */ || con == "Q".charAt(0) || con == "P".charAt(0)) {
	    		headerTitle.setText("Text");
	    		
	    		TextView textview;
		        textview = new TextView(mContext);
		        textview.setLayoutParams(new GridView.LayoutParams(image_size, image_size));
		        textview.setPadding(8, 8, 8, 8);
		        textview.setTextSize(20);
	    		
	    		String content = spark.getContent();
	    		textview.setText(content.toCharArray(), 0, Math.min(200, content.length()));
	    		
	    		layout.addView(textview);
	    		
	    		contentView = layout;
	    		
	    	} else if (con == "C".charAt(0)) {
	    		headerTitle.setText("Code snippet");
	    		
	    		/*
	    		 * At the moment, code snippets are handled just like text. Eventually they will make use of Github 
	    		 */
	    		TextView textview;
		        textview = new TextView(mContext);
		        textview.setLayoutParams(new GridView.LayoutParams(image_size, image_size));
		        textview.setPadding(8, 8, 8, 8);
		        textview.setTextSize(20);
	    		
	    		String content = spark.getContent();
	    		textview.setText(content.toCharArray(), 0, Math.min(200, content.length()));
	    		
	    		layout.addView(textview);
	    		
	    		contentView = layout;
	    		
	    	} else if (con == "L".charAt(0)) {
	    		/*
	    		 * Not sure how to handle this one
	    		 * In the mockup, Colin had it as a screenshot of the page.
	    		 * IF that proves too difficult to generate, we can also use the fav_icon with the website
	    		 * Either way, it looks like it'll turn out similar to the soundcloud implementation
	    		 */
	    		
	    		/*
	    		 * First make the title:
	    		 * Get the page title
	    		 * something like "Wikipedia - Kittens"
	    		 */

	    		headerTitle.setText("Link");

	    		
	    		/*
	    		 * Then add the thumbnail:
	    		 */
	    		ImageView imageView;
		        imageView = new ImageView(mContext);
		        imageView.setLayoutParams(new GridView.LayoutParams(image_size, image_size));

		        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		        imageView.setPadding(8, 8, 8, 8);
		        /*
	    		 * This is what will be set to the favicon or the screenshot:
	    		 */
		        imageView.setImageResource(R.drawable.btn_blue_audio);
		        layout.addView(imageView);
		        
	    		contentView = layout;
	    		
	    	}
	    	
	    	if (contentView != null) {
	    		//Attach content:
		    	sparkInfo.addView(contentView);
		    	
		    	//Attach user info:
		    	String user = "by ";
		    	if (!spark.getUser().equals("")) {
		    		user += spark.getUser();
		    	} else {
		    		user += "an unknown user";
		    	}
		    	TextView userInfo = new TextView(mContext);
		    	userInfo.setText(user);
		    	sparkInfo.addView(userInfo);
		    	
		    	//Attach date info:
		    	String date = spark.getDate();
		    	TextView dateInfo = new TextView(mContext);
		    	dateInfo.setText(date);
		    	sparkInfo.addView(dateInfo);
		    	
		    	//Attach ribbon:
		    	ImageView ribbon = new ImageView(mContext);
		    	//LayoutParams ribbonParams = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
		    	LayoutParams ribbonParams = new TableRow.LayoutParams(30, 30, 0f);
		        ribbon.setLayoutParams(ribbonParams);
		    	char sparkType = spark.getSparkType();
		    	if (sparkType == "W".charAt(0)) {
		    		ribbon.setImageResource(R.drawable.ribbon_whatif);
		    	} else if (sparkType == "P".charAt(0)) {
		    		ribbon.setImageResource(R.drawable.ribbon_problem);
		    	} else if (sparkType == "I".charAt(0)) {
		    		ribbon.setImageResource(R.drawable.ribbon_inspiration);
		    	}
		    	header.addView(ribbon);
		    	
		    	v = sparkInfo;
	    	} else {
	    		TextView t = new TextView(mContext);
	    		t.setText("Error with this one");
	    		Log.v("Content Type", String.valueOf(spark.getContentType()));
	    		v = t;
	    	}
	    	
    	} else if (getJawns()[position].getType() == "I".charAt(0)) {
    		Idea idea = getJawnAt(position).getSelfIdea();
	    	@SuppressWarnings("unused")
			View contentView = null;
	    	LinearLayout ideaInfo = new LinearLayout(mContext);
	    	
	    	int numSparks = idea.getSparkIds().length;
	    	String num = ""+numSparks;
	    	TextView numInfo = new TextView(mContext);
	    	numInfo.setText(num.toCharArray(), 0, num.length());
	    	ideaInfo.addView(numInfo);
    		
    		String user = "by "+idea.getUser();
	    	TextView userInfo = new TextView(mContext);
	    	userInfo.setText(user.toCharArray(), 0, user.length());
	    	ideaInfo.addView(userInfo);
	    	
	    	String date = idea.getCreatedAt();
	    	TextView dateInfo = new TextView(mContext);
	    	dateInfo.setText(date);
	    	ideaInfo.addView(dateInfo);
    		
	    	v = ideaInfo;
    	}
    	
    	return v;
    }

    public int getCount() {
        return jawns.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    
    public Jawn[] getJawns() {
    	return jawns;
    }
    
    public Jawn getJawnAt(int pos) {
    	return jawns[pos];
    }
    
    public void setJawns(Jawn[] j) {
    	jawns = j;
    }
    
    public void removeAtPosition(int pos) {
    	if (getJawns().length >= 1) {
	    	Jawn[] newSet = new Jawn[getJawns().length - 1];
	    	for (int i = 0; i < pos; i++) {
	    		newSet[i] = getJawns()[i];
	    	}
	    	for (int i = pos; i < getJawns().length - 1; i++) {
	    		newSet[i] = getJawns()[i + 1];
	    	}
	    	setJawns(newSet);
    	}
    }
    
    public void addAtPosition(Jawn j, int pos) {
    	Jawn[] newSet = new Jawn[getJawns().length - 1];
    	for (int i = 0; i < pos; i++) {
    		newSet[i] = getJawns()[i];
    	}
    	newSet[pos] = j;
    	for (int i = pos + 1; i < getJawns().length; i++) {
    		newSet[i] = getJawns()[i - 1];
    	}
    	setJawns(newSet);
    }
}