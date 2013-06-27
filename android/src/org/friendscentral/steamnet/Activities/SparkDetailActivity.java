package org.friendscentral.steamnet.Activities;

import org.friendscentral.steamnet.R;
<<<<<<< HEAD
import org.friendscentral.steamnet.BaseClasses.Spark;
=======
>>>>>>> upstream/master

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;
=======
>>>>>>> upstream/master
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SparkDetailActivity extends Activity {

<<<<<<< HEAD
	private static final String TAG = "SparkDetailView";
	Spark spark;
	String content;
	int id;
	String sparkType;
	String createdAt;
	String contentType;
	String creator;
	String[] tags;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spark_detail);
		
		Intent intent = getIntent();
		spark = (Spark) intent.getSerializableExtra("spark");	
		content = spark.getContent();
		id = spark.getId();
		sparkType = spark.getSparkTypeString();
		createdAt = spark.getCreatedAt();
		contentType = spark.getContentTypeString();
		creator = spark.getUser();
		tags = spark.getTags();
		
		if(createdAt == null){
			createdAt = "Date unknown";
		}
		if (creator == null) {
			creator = "--User--";
		}
		
		Log.v(TAG, creator);
		TextView sparkCreator = (TextView) findViewById(R.id.CreatorTextView);
		sparkCreator.setText("Sparked by "+creator);
		
		TextView createdAtView = (TextView) findViewById(R.id.TimestampTextView);
		createdAtView.setText(createdAt);
		
		Log.d(TAG, contentType);
		
		fillSparkData();
		fillSparkTypes();
		fillTags();
	}
	
	public void fillSparkData() {
		LinearLayout sparkData = (LinearLayout) findViewById(R.id.SparkData);
		
		if (contentType.equals("T") || contentType.equals("Q")) {
			TextView textview = new TextView(this);
			textview.setText(content);
			textview.setTextSize(40);
			textview.setMaxLines(50);
			sparkData.addView(textview);
		} else if (contentType.equals("P")) {
			ImageView imgview = new ImageView(this);
			
			/*
			 * NEEDS TO UPDATED WITH IMGUR API. RIGHT NOW JUST FILLED IN WITH GENERIC PICTURE
			 */
			imgview.setImageResource(R.drawable.btn_blue_picture);
			imgview.setMaxHeight(400);
			imgview.setMaxWidth(400);
			sparkData.addView(imgview);
		} else if (contentType.equals("V")) {
			ImageView videoview = new ImageView(this);
			
			/*
			 * NEEDS TO UPDATED WITH YOUTUBE API. RIGHT NOW JUST FILLED IN WITH GENERIC PICTURE
			 */
			videoview.setImageResource(R.drawable.btn_blue_video);
			videoview.setMaxHeight(400);
			videoview.setMaxWidth(400);
			sparkData.addView(videoview);
		} else if (contentType.equals("A")) {
			/*
			 * NEEDS TO UPDATED WITH SOUNDCLOUD API. RIGHT NOW JUST FILLED IN WITH GENERIC PICTURE
			 */
			TextView title = new TextView(this);
			title.setText(content);
			sparkData.addView(title);
			
			ImageView audioview = new ImageView(this);
			audioview.setImageResource(R.drawable.btn_blue_audio);
			audioview.setMaxHeight(400);
			audioview.setMaxWidth(400);
			sparkData.addView(audioview);
		} else if (contentType.equals("C")) {
			/*
			 * WILL EVENTUALLY USE THE GIT API FOR MAKING CODE LOOK PRETTY
			 */
			TextView codeview = new TextView(this);
			codeview.setText(content);
			codeview.setTextSize(40);
			codeview.setMaxLines(50);
			sparkData.addView(codeview);
		} else if (contentType.equals("L")) {
			/*
			 * NEEDS TO BE FILLED IN WITH GENERIC WEBSITE METADATA
			 * MAYBE THERE'S SOME KIND OF GOOGLE API TO FIT THIS PURPOSE?
			 * TO GET PAGE TITLE, DESCRIPTION, ETC
			 */
			TextView linkview = new TextView(this);
			linkview.setText(content);
			linkview.setTextSize(40);
			linkview.setMaxLines(50);
			sparkData.addView(linkview);
		} 
	}
	
	public void fillSparkTypes() {
		TextView sparkTypeView = (TextView) findViewById(R.id.SparkTypeTextView);
		
		if(sparkType.equals("I")){
			sparkType = "Inspiration";
		} else if(sparkType.equals("P")){
			sparkType = "Problem";
		} else if(sparkType.equals("W")){
			sparkType = "What If";
		}
		
		if (contentType.equals("T") || contentType.equals("Q")) {
			contentType = "Text";
		} else if (contentType.equals("A")) {
			contentType = "Audio";
		} else if (contentType.equals("L")) {
			contentType = "Link";
		} else if (contentType.equals("C")) {
			contentType = "Code Snippet";
		} else if (contentType.equals("P")) {
			contentType = "Picture";
		} else if (contentType.equals("V")) {
			contentType = "Video";
		} 
		
		sparkTypeView.setText(sparkType + " - " + contentType);
	}
	
	public void fillTags() {
		TextView sparkTags = (TextView) findViewById(R.id.TagsTextView);
		
		String tagString = "Tags: ";
		if (tags != null) {
			for (int i = 0; i < tags.length; i++) {
				tagString += tags[i];
				if (i != tags.length - 1) {
					tagString += ", ";
				}
			}
		} else {
			tagString = "No tags";
		}
		sparkTags.setText(tagString);
=======
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		Intent intent = getIntent();
		String message = intent.getBundleExtra(MainActivity.EXTRA_MESSAGE).getString("Name");
		int id = intent.getBundleExtra(MainActivity.EXTRA_MESSAGE).getInt("Id");
		String sparkType = intent.getBundleExtra(MainActivity.EXTRA_MESSAGE).getString("Type");
		
		
		
		TextView textview = (TextView) findViewById(R.id.SparkTitleTextView);
		textview.setText(message);
		
		//ImageView imgview = (ImageView) findViewById(R.id.pic);
		
		if (sparkType.equals("Picture")) {
			ImageView imgview = new ImageView(this);
			imgview.setImageResource(id);
			
			LinearLayout detailLayout = (LinearLayout) findViewById(R.id.detail_linear_layout);
			LinearLayout dataSection = (LinearLayout) detailLayout.findViewById(R.id.SparkData);
			dataSection.addView(imgview);
		}
>>>>>>> upstream/master
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

}
