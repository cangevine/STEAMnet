package org.friendscentral.steamnet;

import java.util.ArrayList;

import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JawnAdapter extends BaseAdapter {
	private Context mContext;
    private Jawn[] jawns;
    private int image_size;
    ArrayList<Boolean> multimediaLoaded;

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
        multimediaLoaded = new ArrayList<Boolean>(j.length);
        for (int i = 0; i < j.length; i++) {
        	multimediaLoaded.add(i, false);
        }
        jawns = j;
    }

	// create a new ImageView for each item referenced by the Adapter
    @SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {
    	final int SPARK_INFO_ID = 4;
		
    	RelativeLayout v = new RelativeLayout(mContext);
    	GridView.LayoutParams r = new GridView.LayoutParams(232, 270);
    	v.setLayoutParams(r);
    	v.setPadding(8, 0, 8, 8);
    	v.setGravity(Gravity.CENTER);
    	if (position < jawns.length) {
	    	if (getJawnAt(position).getType() == 'S') {
		    	Spark spark = getJawnAt(position).getSelfSpark();
		    	char con = spark.getContentType();
		    	View contentView = null;
			    LinearLayout sparkInfo = new LinearLayout(mContext);
			    sparkInfo.setOrientation(LinearLayout.VERTICAL);
			    sparkInfo.setBackgroundColor(Color.WHITE);
			    sparkInfo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
			    sparkInfo.setGravity(Gravity.CENTER);
			    sparkInfo.setPadding(8, 0, 8, 8);
			    sparkInfo.setId(SPARK_INFO_ID);
		    	
		    	if (con == "P".charAt(0)) {
			        FrameLayout frameLayout = new FrameLayout(mContext);
			        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(image_size, image_size));
			        Drawable imageSymbol = mContext.getResources().getDrawable(R.drawable.symbol_image);
			        imageSymbol.setAlpha(155);
			        frameLayout.setForeground(imageSymbol);
			        frameLayout.setForegroundGravity(Gravity.CENTER);
			        frameLayout.setBackgroundResource(R.drawable.spark_content_bg);
			        frameLayout.setPadding(4, 4, 4, 4);
		    		
			        ImageView imageView;
			        imageView = new ImageView(mContext);
			        imageView.setLayoutParams(new FrameLayout.LayoutParams(image_size, image_size));
			        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			        
			        
			        /*
			         * This part needs to be updated with the S3 link:
			         * Get spark bitmap and set it as resource
			         */
			        if (spark.getBitmap() != null) {
			        	multimediaLoaded.set(position, true);
			        	imageView.setImageBitmap(spark.getBitmap());
			        	frameLayout.addView(imageView);
			        } else {
			        	ProgressBar pb = new ProgressBar(mContext);
			        	pb.setPadding(50, 50, 50, 50);
			        	frameLayout.addView(pb);
			        }
			        
			        contentView = frameLayout;
			        
		    	} else if (con == "V".charAt(0)) {
			        FrameLayout frameLayout = new FrameLayout(mContext);
			        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(image_size, image_size));
			        Drawable imageSymbol = mContext.getResources().getDrawable(R.drawable.symbol_video);
			        imageSymbol.setAlpha(155);
			        frameLayout.setForeground(imageSymbol);
			        frameLayout.setForegroundGravity(Gravity.CENTER);
			        frameLayout.setBackgroundResource(R.drawable.spark_content_bg);
			        frameLayout.setPadding(4, 4, 4, 4);
		    		
		    		/*
		    		 * Normally within an app, embedded Youtube videos either begin playing or launch the Youtube app on click
		    		 * Within the spark grid, we don't need that functionality
		    		 * Therefore the manifestation of the video will just be a thumbnail (in the grid)
		    		 * The thumbnail will need to be received from the Youtube API, however
		    		 * In the Detail View, it will be a normal, dynamic video
		    		 */
		    		
			        ImageView imageView;
			        imageView = new ImageView(mContext);
			        imageView.setLayoutParams(new FrameLayout.LayoutParams(image_size, image_size));
			        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			        
			        /*
			         * This part needs to be updated with the youtube API
			         * Something like setImageResource(youtubeAPI.getThumbnail())
			         */
			        
			        if (spark.getBitmap() != null) {
			        	multimediaLoaded.set(position, true);
			        	imageView.setImageBitmap(spark.getBitmap());
			        	frameLayout.addView(imageView);
			        } else {
			        	ProgressBar pb = new ProgressBar(mContext);
			        	pb.setPadding(50, 50, 50, 50);
			        	frameLayout.addView(pb);
			        }
			        
			        contentView = frameLayout;
			        
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
		    		String audioTitle = spark.getContent();
		    		
		    		FrameLayout frameLayout = new FrameLayout(mContext);
			        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(image_size, image_size));
			        Drawable imageSymbol = mContext.getResources().getDrawable(R.drawable.symbol_video);
			        imageSymbol.setAlpha(155);
			        frameLayout.setForeground(imageSymbol);
			        frameLayout.setForegroundGravity(Gravity.CENTER);
			        frameLayout.setBackgroundResource(R.drawable.spark_content_bg);
			        frameLayout.setPadding(4, 4, 4, 4);
		    		
		    		/*
		    		 * Then add the thumbnail:
		    		 */
		    		/*ImageView imageView;
			        imageView = new ImageView(mContext);
			        imageView.setLayoutParams(new FrameLayout.LayoutParams(image_size, image_size));
			        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			        frameLayout.addView(imageView);
			        frameLayout.setBackgroundResource(R.drawable.spark_content_bg);
			        frameLayout.setPadding(4, 4, 4, 4);*/
			        	
			        /*
		    		 * Placeholder until Soundcloud API is working
		    		 */
			       // imageView.setImageResource(R.drawable.btn_blue_audio);
			        TextView t = new TextView(mContext);
			        t.setText(spark.getContent());
			        t.setGravity(Gravity.CENTER_HORIZONTAL);
			        t.setPadding(4, 4, 4, 4);
			        t.setTextSize(15f);
			        frameLayout.addView(t);
			        
			        multimediaLoaded.set(position, true);
		    		contentView = frameLayout;
		    		
		    	} else if (con == "T".charAt(0)) {
		    		TextView textview;
			        textview = new TextView(mContext);
			        textview.setLayoutParams(new FrameLayout.LayoutParams(image_size, image_size));
			        textview.setPadding(8, 8, 8, 8);
			        textview.setTextSize(20);
			        textview.setBackgroundResource(R.drawable.spark_content_bg);
		    		
		    		String content = spark.getContent();
		    		textview.setText(content.toCharArray(), 0, Math.min(200, content.length()));
		    		
		    		multimediaLoaded.set(position, true);
		    		contentView = textview;
		    		
		    	} else if (con == "C".charAt(0)) {
		    		/*
		    		 * At the moment, code snippets are handled just like text. Eventually they will make use of Github 
		    		 */
		    		FrameLayout frameLayout = new FrameLayout(mContext);
			        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(image_size, image_size));
			        frameLayout.setBackgroundResource(R.drawable.spark_content_bg);
			        frameLayout.setPadding(4, 4, 4, 4);
		    		
			        ImageView imageView;
			        imageView = new ImageView(mContext);
			        imageView.setLayoutParams(new FrameLayout.LayoutParams(image_size, image_size));
			        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			        
			        
			        if (spark.getBitmap() != null) {
			        	multimediaLoaded.set(position, true);
			        	imageView.setImageBitmap(spark.getBitmap());
			        	frameLayout.addView(imageView);
			        } else {
			        	ProgressBar pb = new ProgressBar(mContext);
			        	pb.setPadding(50, 50, 50, 50);
			        	frameLayout.addView(pb);
			        }
	
		    		contentView = frameLayout;
		    		
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
	
		    		FrameLayout frameLayout = new FrameLayout(mContext);
			        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(image_size, image_size));
			        Drawable imageSymbol = mContext.getResources().getDrawable(R.drawable.symbol_link);
			        imageSymbol.setAlpha(155);
			        frameLayout.setForeground(imageSymbol);
			        frameLayout.setForegroundGravity(Gravity.CENTER);
			        frameLayout.setBackgroundResource(R.drawable.spark_content_bg);
			        frameLayout.setPadding(4, 4, 4, 4);
		    		
			        ImageView imageView;
			        imageView = new ImageView(mContext);
			        imageView.setLayoutParams(new FrameLayout.LayoutParams(image_size, image_size));
			        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			        
			        /*
		    		 * This is what will be set to the favicon or the screenshot:
		    		 */
			        if (spark.getBitmap() != null) {
			        	multimediaLoaded.set(position, true);
			        	imageView.setImageBitmap(spark.getBitmap());
			        	frameLayout.addView(imageView);
			        } else {
			        	ProgressBar pb = new ProgressBar(mContext);
			        	pb.setPadding(50, 50, 50, 50);
			        	frameLayout.addView(pb);
			        }
	
		    		contentView = frameLayout;
		    		
		    	}
		    	
		    	if (contentView != null) {
		    		//Attach content:
			    	sparkInfo.addView(contentView);
			    	
			    	//Attach user info:
			    	String user = "by ";
			    	if (spark.getUser() != null) {
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
			    	char sparkType = spark.getSparkType();
			    	if (sparkType == "W".charAt(0)) {
			    		ribbon.setImageResource(R.drawable.ribbon_whatif);
			    		sparkInfo.setBackgroundResource(R.drawable.spark_what_if_bg);
			    	} else if (sparkType == "P".charAt(0)) {
			    		ribbon.setImageResource(R.drawable.ribbon_problem);
			    		sparkInfo.setBackgroundResource(R.drawable.spark_problem_bg);
			    	} else if (sparkType == "I".charAt(0)) {
			    		ribbon.setImageResource(R.drawable.ribbon_inspiration);
			    		sparkInfo.setBackgroundResource(R.drawable.spark_inspiration_bg);
			    	}
			    	
			    	RelativeLayout.LayoutParams ribbonPosition = new RelativeLayout.LayoutParams(30, 40);
			    	ribbonPosition.addRule(RelativeLayout.ALIGN_TOP, SPARK_INFO_ID);
			    	ribbonPosition.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			    	ribbon.setLayoutParams(ribbonPosition);
			    	
			    	v.addView(sparkInfo);	
			    	v.addView(ribbon);
		    	} else {
		    		TextView t = new TextView(mContext);
		    		t.setText("Error with this one");
		    		Log.v("Content Type", String.valueOf(spark.getContentType()));
		    		v.addView(t);
		    	}
		    	
	    	} else if (getJawns()[position].getType() == 'I') {
	    		multimediaLoaded.set(position, true);

	    		Idea idea = getJawnAt(position).getSelfIdea();
		    	View contentView = null;
		    	LinearLayout ideaInfo = new LinearLayout(mContext);
		    	ideaInfo.setOrientation(LinearLayout.VERTICAL);
		    	ideaInfo.setBackgroundColor(Color.WHITE);
		    	ideaInfo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
			    ideaInfo.setGravity(Gravity.CENTER);
			    
			    FrameLayout frameLayout = new FrameLayout(mContext);
		        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(image_size, image_size));
		        frameLayout.setForegroundGravity(Gravity.CENTER);
		        frameLayout.setBackgroundResource(R.drawable.spark_content_bg);
		        
		        LayoutInflater inflater = LayoutInflater.from(mContext);
				LinearLayout grid = (LinearLayout) inflater.inflate(R.layout.idea_grid, null);
		    	
				if (idea.getBitmaps() != null) {
					frameLayout.addView(grid);
					if (idea.getBitmap(0) != null)
						((ImageView) grid.findViewById(R.id.idea_grid_top_left)).setImageBitmap(idea.getBitmap(0));
					if (idea.getBitmap(1) != null)
						((ImageView) grid.findViewById(R.id.idea_grid_top_right)).setImageBitmap(idea.getBitmap(1));
					if (idea.getBitmap(2) != null)
						((ImageView) grid.findViewById(R.id.idea_grid_bottom_left)).setImageBitmap(idea.getBitmap(2));
					if (idea.getBitmap(3) != null)
						((ImageView) grid.findViewById(R.id.idea_grid_bottom_right)).setImageBitmap(idea.getBitmap(3));
		        } else  {
		        	ProgressBar pb = new ProgressBar(mContext);
		        	pb.setPadding(50, 50, 50, 50);
		        	frameLayout.addView(pb);
		        }

				
		        contentView = frameLayout;
		        
		        //Attach content
				ideaInfo.addView(contentView);
				
				//Attach user info:
				TextView userInfo = new TextView(mContext);
				String user = "by an unknown user";
				if (idea.getUser() != null) {
					// TODO update:
					user = "max";
				}
				userInfo.setText("by "+user);
				userInfo.setPadding(8, 0, 8, 0);
				ideaInfo.addView(userInfo);
				
				//Attach date info:
				String date = idea.getCreatedAt();
				String a = "a";
				TextView dateInfo = new TextView(mContext);
				dateInfo.setText(date);
				dateInfo.setPadding(8, 0, 8, 0);
				ideaInfo.addView(dateInfo);
				
				ideaInfo.setBackgroundResource(R.drawable.idea_bg);
				
				v.addView(ideaInfo);
	    	}
    	} else {
    		// SUPER dirty way of seeing if your in the main activity rather then idea detail view
    		if (jawns.length > 8) {
    			MainActivity ma = (MainActivity) mContext;
    			if (ma.getIndexGrid() != null) {
    				if (ma.getIndexGrid().getScrollListener() != null) {
    					if (!ma.getIndexGrid().getScrollListener().hasReachedEnd()) {
    						v.addView(new ProgressBar(mContext));
    					}
    				}
    			}
    		}
    	}
    	return v;
    }

    public int getCount() {
        return jawns.length + 1;
    }

    public Jawn getItem(int position) {
    	if (position < jawns.length) {
    		return jawns[position];
    	}
    	return null;
    }

    public long getItemId(int position) {
        if (position < 4) {
        	return 0;
        } else if (position < 8) {
        	return 1;
        } else if (position < 12) {
        	return 2;
        } else if (position < 16) {
        	return 3;
        }
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
    
    public ArrayList<Boolean> getLoadedList() {
    	return multimediaLoaded;
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
	    	multimediaLoaded.remove(pos);
	    	setJawns(newSet);
    	}
    }
    
    public void addAtPosition(Jawn j, int pos) {
    	Jawn[] newSet = new Jawn[getJawns().length + 1];
    	for (int i = 0; i < pos; i++) {
    		newSet[i] = getJawns()[i];
    	}
    	newSet[pos] = j;
    	multimediaLoaded.add(pos, false);
    	for (int i = pos + 1; i < getJawns().length; i++) {
    		newSet[i] = getJawns()[i - 1];
    	}
    	setJawns(newSet);
    }
    
    public void setJawn(int pos, Jawn j) {
    	if (pos < jawns.length) {
    		jawns[pos] = j;
    	}
    }
    
    public void append(Jawn j) {
    	
    }
    
    public void shuffleJawns(Jawn[] arr) {
    	ArrayList<Jawn> ls = new ArrayList<Jawn>();
		for (int i = 0; i < arr.length; i++ ){
			ls.add(arr[i]);
		}
		Jawn[] res = new Jawn[arr.length];
		double randNum;
		int randInd;
		int q = 0;
		while (ls.size() != 0) {
			randNum = Math.random()*ls.size();
			randInd = (int)Math.floor(randNum);
			res[q] = ls.get(randInd);
			ls.remove(randInd);
			q++;
		}
		setJawns(res);
    }
}