package org.friendscentral.steamnet;

import org.friendscentral.steamnet.BaseClasses.Comment;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	Comment[] comments;
	Context context;
	
	public CommentAdapter(Context c, Comment[] com) {
		context = c;
		comments = com;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	
		//if (comments != null) {
		LinearLayout comment = new LinearLayout(context);
		comment.setPadding(5, 5, 5, 5);
		comment.setOrientation(LinearLayout.VERTICAL);
			
		int userID = comments[position].getUserId();
		String commentContent = comments[position].getContent();
		String username = comments[position].getUsername();
			
		TextView user = new TextView(context);
		user.setPadding(2, 5, 2, 5);
		user.setTextSize(20);
		user.setTypeface(null, Typeface.ITALIC);

		user.setText(username+" says,");
			
		TextView content = new TextView(context);
		content.setPadding(2, 5, 2, 5);
		content.setTextSize(15);
		content.setText(commentContent);
			
			
		comment.addView(user);
		comment.addView(content);
			
		
		return comment;
	}
	
	@Override
	public int getCount() {
		return comments.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void addComment(Comment c) {
		Comment[] newComments = new Comment[comments.length + 1];
		for (int i = 0; i < comments.length; i++) {
			newComments[i] = comments[i];
		}
		newComments[comments.length] = c;
		comments = newComments;
	}

}
