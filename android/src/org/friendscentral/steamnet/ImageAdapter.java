package org.friendscentral.steamnet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c, SimpleSpark[] s, boolean reversed) {
        mContext = c;
        mThumbIds = new Integer[s.length];
        initSparks(s, reversed);
    }
    
    public void initSparks(SimpleSpark[] s, boolean reversed) {
    	if (!reversed) {
	    	for (int i = 0; i < s.length; i++) {
	    		mThumbIds[i] = s[i].getId();
	    	}
    	} else {
    		int q = 0;
    		for (int i = s.length - 1; i >= 0; i--) {
	    		mThumbIds[q] = s[i].getId();
	    		q++;
	    	}
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

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds;/* = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5
    };*/
}