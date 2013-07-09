package org.friendscentral.steamnet;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

public class SpinnerAdapter extends BaseAdapter {
	Context context;
	int num;
	
	public SpinnerAdapter(Context c, int n) {
		context = c;
		num = n;
	}

	// create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	LinearLayout l = new LinearLayout(context);
    	l.setMinimumHeight(250);
    	l.setMinimumWidth(150);
    	ProgressBar s = new ProgressBar(context);
    	l.setGravity(Gravity.CENTER);
    	l.addView(s);
    	
    	l.setFocusable(false);
    	l.setFocusableInTouchMode(false);
    	l.setClickable(false);

    	s.setFocusable(false);
    	s.setFocusableInTouchMode(false);
    	s.setClickable(false);
    	
    	return l;
    }

    public int getCount() {
        return num;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
}