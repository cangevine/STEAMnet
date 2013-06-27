package org.friendscentral.steamnet;

import java.util.ArrayList;

import org.friendscentral.steamnet.BaseClasses.Spark;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class IdeaBucket {
	LinearLayout layout;
	Context context;
	LinearLayout mainLayout;
	ArrayList<Spark> sparks;
	ImageView[] imageViews;
	
	public void initIdeaGrid(LinearLayout l, Context c, LinearLayout m) {
		layout = l;
		context = c;
		mainLayout = m;
		sparks = new ArrayList<Spark>(4);
		Button ignite = (Button) mainLayout.findViewById(R.id.ignite_button);
		ignite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				for (int i = sparks.size() - 1; i >= 0; i--) {
					removeSpark(i);
				}
			}
		});
		
		imageViews = new ImageView[4];
		imageViews[0] =	(ImageView) layout.findViewById(R.id.first_image);
		imageViews[1] = (ImageView) layout.findViewById(R.id.second_image);
		imageViews[2] = (ImageView) layout.findViewById(R.id.third_image);
		imageViews[3] = (ImageView) layout.findViewById(R.id.fourth_image);
	}
	
	public void addSpark(Spark ss) {
		if (sparks.size() < 4) {
			sparks.add(ss);
			int resource = 0;
			if (ss.getContentType() == "P".charAt(0)) {
				resource = R.drawable.symbol_image;
			} else if (ss.getContentType() == "V".charAt(0)) {
				resource = R.drawable.symbol_video;
			} else  if (ss.getContentType() == "L".charAt(0)) {
				resource = R.drawable.symbol_link;
			} else if (ss.getContentType() == "A".charAt(0)) {
				resource = R.drawable.symbol_link;
			} else if (ss.getContentType() == "T".charAt(0)) {
				resource = R.drawable.btn_green_text;
			} else if (ss.getContentType() == "C".charAt(0)) {
				resource = R.drawable.btn_green_code;
			}
			imageViews[sparks.size() - 1].setImageResource(resource);
		}
	}
	
	public void removeSpark(int pos) {
		if (sparks.size() > 0) {
			sparks.remove(pos);
			for (int i = pos; i < imageViews.length - 1; i++) {
				imageViews[i].setImageDrawable(imageViews[i + 1].getDrawable());
			}
			//imageViews[3].setImageResource(0);
			/*
			 * Go through, and if the resource is 0, make it invisible.
			 * Set the resource to some virtually blank 1px image?
			 */
		}
	}
	
	public ImageView[] getImageViews() {
		return imageViews;
	}
}