package org.friendscentral.steamnet;

import java.util.ArrayList;

import org.friendscentral.steamnet.BaseClasses.Spark;

import APIHandlers.RetrieveDataTaskPostIdea;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class IdeaBucket {
	LinearLayout layout;
	Context context;
	LinearLayout mainLayout;
	ArrayList<Spark> sparks;
	ImageView[] imageViews;
	LinearLayout bucketFrag;
	LinearLayout submitFrag;
	
	public void initIdeaGrid(LinearLayout l, Context c, LinearLayout m) {
		layout = l;
		context = c;
		mainLayout = m;
		sparks = new ArrayList<Spark>(4);
		
		imageViews = new ImageView[4];
		imageViews[0] =	(ImageView) layout.findViewById(R.id.first_image);
		imageViews[1] = (ImageView) layout.findViewById(R.id.second_image);
		imageViews[2] = (ImageView) layout.findViewById(R.id.third_image);
		imageViews[3] = (ImageView) layout.findViewById(R.id.fourth_image);
		
		bucketFrag = (LinearLayout) mainLayout.findViewById(R.id.bucketFrag);
		submitFrag = (LinearLayout) mainLayout.findViewById(R.id.submitFrag);
		
		addSubmitButtonListener();
		addIgniteButtonListener();
		addCancelButtonListener();
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
		if (sparks.size() >= 2) {
			Log.v("Report", "more than 2 sparks");
			Button ignite = (Button) mainLayout.findViewById(R.id.ignite_button);
			ignite.setEnabled(true);
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
		if (sparks.size() < 2) {
			Button ignite = (Button) mainLayout.findViewById(R.id.ignite_button);
			ignite.setEnabled(false);
		}
	}
	
	public ImageView[] getImageViews() {
		return imageViews;
	}
	
	public void addIgniteButtonListener() {
		Button ignite = (Button) mainLayout.findViewById(R.id.ignite_button);
		ignite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v("Clicked", "Clicked");
				if (sparks.size() > 0) {
					bucketFrag.setVisibility(View.GONE);
					submitFrag.setVisibility(View.VISIBLE);
					updateWeights(0,0,1);
				}
			}
			
		});
	}
	
	public void addSubmitButtonListener() {
		Button ignite = (Button) mainLayout.findViewById(R.id.IdeaSubmitButton);
		ignite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v("Clicked", "Clicked");
				EditText descriptionEditText = (EditText) mainLayout.findViewById(R.id.ideaDescription);
				String description = descriptionEditText.getText().toString();
				EditText tagsEditText = (EditText) mainLayout.findViewById(R.id.ideaTags);
				String tags = tagsEditText.getText().toString();
				
				int[] s = new int[sparks.size()];
				for (int i = 0; i < sparks.size(); i++) {
					s[i] = sparks.get(i).getId();
				}
				
				// TODO not just "max" every time. Spark.getUser 
				RetrieveDataTaskPostIdea r = new RetrieveDataTaskPostIdea(description, s, tags.split(", "), "max");
				
				sparks.clear();
				imageViews[0].setImageResource(0);
				imageViews[1].setImageResource(0);
				imageViews[2].setImageResource(0);
				imageViews[3].setImageResource(0);
				descriptionEditText.setText("");
				tagsEditText.setText("");
				submitFrag.setVisibility(View.GONE);
				bucketFrag.setVisibility(View.VISIBLE);
				updateWeights(2,4,2);
				
				Button ignite = (Button) mainLayout.findViewById(R.id.ignite_button);
				ignite.setEnabled(false);
			}
			
		});
	}
	
	public void addCancelButtonListener() {
		Button cancel = (Button) mainLayout.findViewById(R.id.IdeaCancelButton);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sparks.size() > 0) {
					bucketFrag.setVisibility(View.VISIBLE);
					submitFrag.setVisibility(View.GONE);
					updateWeights(2,4,2);
				}
			}
			
		});
	}
	
	public void updateWeights(float sp, float fs, float ib) {
    	mainLayout.findViewById(R.id.WizardSection).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, sp));
    	mainLayout.findViewById(R.id.FilterSettings).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, fs));
    	mainLayout.findViewById(R.id.IdeaBucket).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, ib));
    }
}