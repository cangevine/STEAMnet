package org.friendscentral.steamnet.DetailViewFillers;

import org.friendscentral.steamnet.CommentAdapter;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.Activities.SparkDetailActivity;
import org.friendscentral.steamnet.BaseClasses.Comment;
import org.friendscentral.steamnet.BaseClasses.Spark;

import APIHandlers.PostComment;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public abstract class DetailFiller {
	abstract void fillData();
	
	LinearLayout detailView;
	Context context;
	
	Spark spark;
	int id;
	String content;
	String sparkType;
	String createdAt;
	String contentType;
	Comment[] comments;
	
	public DetailFiller(Spark s, String ct, LinearLayout l, Context c) {
		Log.v("DetailFiller called", "Using the polymorphic detail filler! Celebrate!");
		
		detailView = l;
		context = c;
		l.setVisibility(View.VISIBLE);
		
		spark = s;
		content = spark.getContent();
		id = spark.getId();
		sparkType = spark.getSparkTypeString();
		createdAt = spark.getCreatedAt();
		contentType = ct;
		comments = spark.getComments();
		
		if(createdAt == null){
			createdAt = "Date unknown";
		}
		
		if(sparkType.equals("I")){
			sparkType = "Inspiration";
			((SparkDetailActivity) context).findViewById(R.id.complete_spark_data).setBackgroundResource(R.drawable.spark_inspiration_bg);
		} else if(sparkType.equals("P")){
			sparkType = "Problem";
			((SparkDetailActivity) context).findViewById(R.id.complete_spark_data).setBackgroundResource(R.drawable.spark_problem_bg);
		} else if(sparkType.equals("W")){
			sparkType = "What If";
			((SparkDetailActivity) context).findViewById(R.id.complete_spark_data).setBackgroundResource(R.drawable.spark_what_if_bg);
		}
		
		fillSparkTypes();
		//fillTags();
		fillComments();
	}
	
	public void fillSparkTypes() {
		TextView sparkTypeView = (TextView) ((SparkDetailActivity) context).findViewById(R.id.SparkTypeTextView);
		sparkTypeView.setText(sparkType + " - " + contentType);
	}
	
	/*public void fillTags() {
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
	}*/
	
	public void fillComments() {
		ListView commentSection = (ListView) ((SparkDetailActivity) context).findViewById(R.id.CommentList);
		CommentAdapter commentAdapter = new CommentAdapter(((SparkDetailActivity) context), comments);
		if (comments.length == 0) {
			Comment c = new Comment(0, "No comments on this Spark, be the first!", "STEAMnet");
			commentAdapter.addComment(c);
			commentAdapter.notifyDataSetChanged();
		}
		commentSection.setAdapter(commentAdapter);
	}
	
	public void submitComment(View v) {
		EditText editText = (EditText) ((SparkDetailActivity) context).findViewById(R.id.CommentEditText);
		String content = editText.getText().toString();
		((SparkDetailActivity) context).findViewById(R.id.DummyFocusCommentSection).requestFocus();
		
		int userID = 0;
		STEAMnetApplication sna = (STEAMnetApplication) ((SparkDetailActivity) context).getApplication();
		if (sna.getUserId() != null) {
			userID = Integer.valueOf(sna.getUserId());
		}
		if (sna.getUsername() != null) {
			String username = sna.getUsername();
			new PostComment(id, "S".charAt(0), content, userID, username, sna.getToken());
			
			ListView commentSection = (ListView) (((SparkDetailActivity) context).findViewById(R.id.spark_social_section)).findViewById(R.id.CommentList);
			CommentAdapter c = (CommentAdapter) commentSection.getAdapter();
			Comment newComment = new Comment(userID, content, username);
			c.addComment(newComment);
			if (c.getComments()[0].getUserId() == 0) {
				c.removeComment(0);
			}
			c.notifyDataSetChanged();
			editText.setText("");
		} else {
			Toast.makeText(context, "Please log in to submit a comment", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void initSparkButton() {
		Button createIdea = (Button) ((SparkDetailActivity) context).findViewById(R.id.Social_section).findViewById(R.id.create_idea_button); 
		createIdea.setVisibility(View.VISIBLE);
		createIdea.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO do stuff
			}
		});
	}
}
