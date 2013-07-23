package org.friendscentral.steamnet;

import java.util.ArrayList;

import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.Activities.SparkDetailActivity;
import org.friendscentral.steamnet.BaseClasses.Spark;

import APIHandlers.PostIdea;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class IdeaBucket {
	final static int DETAIL_VIEW_RETURN = 5;
	
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
			if (ss.getBitmap() != null) {
				imageViews[sparks.size() - 1].setImageBitmap(ss.getBitmap());
			} else {
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
		if (sparks.size() >= 2) {
			Log.v("Report", "more than 2 sparks");
			Button ignite = (Button) mainLayout.findViewById(R.id.ignite_button);
			ignite.setEnabled(true);
		}
		setOnClickListeners();
	}
	
	public void removeSpark(int pos) {
		if (sparks.size() > 0) {
			sparks.remove(pos);
			Log.v("Number of sparks", String.valueOf(sparks.size()));
			for (int i = 0; i < imageViews.length; i++) {
				if (i < sparks.size()) {
					Spark ss = sparks.get(i);
					if (ss.getBitmap() != null) {
						imageViews[i].setImageBitmap(ss.getBitmap());
					} else {
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
						imageViews[i].setImageResource(resource);
					}
				} else {
					imageViews[i].setImageDrawable(null);
				}
			}
		}
		if (sparks.size() < 2) {
			Button ignite = (Button) mainLayout.findViewById(R.id.ignite_button);
			ignite.setEnabled(false);
		}
	}
	
	public void setOnClickListeners() {
		for (int i = 0; i < imageViews.length; i++) {
			if (i < sparks.size()) {
				final int pos = i;
				imageViews[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, SparkDetailActivity.class);
				    	intent.putExtra("id", sparks.get(pos).getId());
				    	((MainActivity) context).startActivityForResult(intent, DETAIL_VIEW_RETURN);
					}
				});
			} else {
				imageViews[i].setOnClickListener(null);
			}
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
				STEAMnetApplication sna = (STEAMnetApplication) ((MainActivity) context).getApplication();
				if (!sna.getReadOnlyMode()) {
					if (sparks.size() > 0) {
						bucketFrag.setVisibility(View.GONE);
						submitFrag.setVisibility(View.VISIBLE);
						updateWeights(0,0,1);
					}
				} else {
					Toast.makeText(context, "Please log in to submit an Idea", Toast.LENGTH_LONG).show();
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
				
				STEAMnetApplication sna = (STEAMnetApplication) ((MainActivity) context).getApplication();
				String userId = "0";
				if (sna.getUserId() != null) {
					userId = sna.getUserId();
				}
				String username = "Anonymous";
				if (sna.getUsername() != null) {
					username = sna.getUsername();
				}
				String token = "";
				if (sna.getToken() != null) {
					token = sna.getToken();
				}
				
				new PostIdea(description, s, tags.split(", "), userId, username, token);
				
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