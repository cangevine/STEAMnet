package org.friendscentral.steamnet.Activities;

import org.friendscentral.steamnet.CommentAdapter;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.BaseClasses.Comment;
import org.friendscentral.steamnet.BaseClasses.Spark;

import APIHandlers.PostComment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SparkDetailActivity extends Activity {

	private static final String TAG = "SparkDetailView";
	Spark spark;
	String content;
	int id;
	String sparkType;
	String createdAt;
	String contentType;
	String creator;
	String[] tags;
	Comment[] comments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spark_detail);
		
		//Fixes autofocus problem:
        findViewById(R.id.DummyFocusSparkDetail).setFocusableInTouchMode(true);
        findViewById(R.id.DummyFocusSparkDetail).requestFocus();
		
		Intent intent = getIntent();
		spark = (Spark) intent.getSerializableExtra("spark");	
		content = spark.getContent();
		id = spark.getId();
		sparkType = spark.getSparkTypeString();
		createdAt = spark.getCreatedAt();
		contentType = spark.getContentTypeString();
		creator = spark.getUser();
		tags = spark.getTags();
		comments = spark.getComments();
		
		if(createdAt == null){
			createdAt = "Date unknown";
		}
		if (creator.equals("")) {
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
		fillComments();
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
			imgview.setImageResource(R.drawable.symbol_image);
			imgview.setMaxHeight(400);
			imgview.setMaxWidth(400);
			
			TextView contentView = new TextView(this);
			contentView.setTextSize(20);
			contentView.setText(content);
			contentView.setGravity(Gravity.CENTER_HORIZONTAL);
			
			sparkData.addView(contentView);
			sparkData.addView(imgview);
		} else if (contentType.equals("V")) {
			ImageView videoview = new ImageView(this);
			
			/*
			 * NEEDS TO UPDATED WITH YOUTUBE API. RIGHT NOW JUST FILLED IN WITH GENERIC PICTURE
			 */
			videoview.setImageResource(R.drawable.symbol_video);
			videoview.setMaxHeight(400);
			videoview.setMaxWidth(400);
			
			TextView contentView = new TextView(this);
			contentView.setTextSize(20);
			contentView.setText(content);
			contentView.setGravity(Gravity.CENTER_HORIZONTAL);
			
			sparkData.addView(contentView);
			sparkData.addView(videoview);
		} else if (contentType.equals("A")) {
			/*
			 * NEEDS TO UPDATED WITH SOUNDCLOUD API. RIGHT NOW JUST FILLED IN WITH GENERIC PICTURE
			 */
			
			ImageView audioview = new ImageView(this);
			audioview.setImageResource(R.drawable.symbol_link);
			audioview.setMaxHeight(400);
			audioview.setMaxWidth(400);
			
			TextView contentView = new TextView(this);
			contentView.setTextSize(20);
			contentView.setText(content);
			contentView.setGravity(Gravity.CENTER_HORIZONTAL);
			
			sparkData.addView(contentView);
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
			sparkType = "What If?";
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
	}
	
	public void fillComments() {
		if (comments.length == 0) {
			TextView header = (TextView) findViewById(R.id.CommentsHeader);
			header.setText("No comments on this Spark. Be the first!");
		} else {
			ListView commentSection = (ListView) findViewById(R.id.CommentList);
			
			//CommentAdapter commentAdapter = new CommentAdapter(this, comments);
			CommentAdapter commentAdapter = new CommentAdapter(this, comments);
			commentSection.setAdapter(commentAdapter);
		}
	}
	
	public void submitComment(View v) {
		EditText editText = (EditText) findViewById(R.id.CommentEditText);
		String content = editText.getText().toString();
		editText.setText("");
		findViewById(R.id.DummyFocusSparkDetail).requestFocus();
		
		// TODO Make the @userID dynamic
		int userID = 0;
		PostComment comment = new PostComment(id, "S".charAt(0), content, userID);
		
		ListView commentSection = (ListView) findViewById(R.id.CommentList);
		CommentAdapter c = (CommentAdapter) commentSection.getAdapter();
		Comment newComment = new Comment(userID, content);
		c.addComment(newComment);
		c.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

}
