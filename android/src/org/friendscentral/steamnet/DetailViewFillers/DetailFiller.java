package org.friendscentral.steamnet.DetailViewFillers;

import org.friendscentral.steamnet.CommentAdapter;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.Activities.SparkDetailActivity;
import org.friendscentral.steamnet.BaseClasses.Comment;
import org.friendscentral.steamnet.BaseClasses.Spark;

import APIHandlers.PostComment;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
		} else if(sparkType.equals("P")){
			sparkType = "Problem";
		} else if(sparkType.equals("W")){
			sparkType = "What If";
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
		if (comments.length == 0) {
			TextView header = (TextView) ((SparkDetailActivity) context).findViewById(R.id.CommentsHeader);
			header.setText("No comments on this Spark. Be the first!");
		} else {
			ListView commentSection = (ListView) ((SparkDetailActivity) context).findViewById(R.id.CommentList);
			
			//CommentAdapter commentAdapter = new CommentAdapter(this, comments);
			CommentAdapter commentAdapter = new CommentAdapter(((SparkDetailActivity) context), comments);
			commentSection.setAdapter(commentAdapter);
		}
	}
	
	public void submitComment(View v) {
		EditText editText = (EditText) ((SparkDetailActivity) context).findViewById(R.id.CommentEditText);
		String content = editText.getText().toString();
		editText.setText("");
		((SparkDetailActivity) context).findViewById(R.id.DummyFocusCommentSection).requestFocus();
		
		// TODO Make the @userID dynamic
		int userID = 0;
		PostComment comment = new PostComment(id, "S".charAt(0), content, userID);
		
		ListView commentSection = (ListView) ((SparkDetailActivity) context).findViewById(R.id.CommentList);
		CommentAdapter c = (CommentAdapter) commentSection.getAdapter();
		Comment newComment = new Comment(userID, content);
		c.addComment(newComment);
		c.notifyDataSetChanged();
	}
}
