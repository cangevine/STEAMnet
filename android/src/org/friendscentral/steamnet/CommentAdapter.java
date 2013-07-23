package org.friendscentral.steamnet;

import java.util.ArrayList;
import java.util.Arrays;

import org.friendscentral.steamnet.BaseClasses.Comment;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
		comment.setOrientation(LinearLayout.HORIZONTAL);
		comment.setGravity(Gravity.CENTER_VERTICAL);
		
		ImageView contactPicture = new ImageView(context);
		contactPicture.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
		contactPicture.setImageResource(R.drawable.user_icon_bg);
		contactPicture.setPadding(8, 8, 8, 8);
		comment.addView(contactPicture);
			
		LinearLayout commentInfo = new LinearLayout(context);
		commentInfo.setPadding(8, 8, 8, 8);
		commentInfo.setOrientation(LinearLayout.VERTICAL);
		
		int userID = comments[position].getUserId();
		String commentContent = comments[position].getContent();
		String username = comments[position].getUsername();
			
		TextView user = new TextView(context);
		user.setPadding(2, 0, 2, 0);
		user.setTextSize(20);
		user.setTypeface(null, Typeface.ITALIC);

		user.setText(username+" says...");
			
		TextView content = new TextView(context);
		content.setPadding(2, 0, 2, 0);
		content.setTextSize(15);
		content.setText(commentContent);
			
			
		commentInfo.addView(user);
		commentInfo.addView(content);
		
		comment.addView(commentInfo);
			
		comment.setBackgroundResource(R.drawable.comment_bg);
		
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
	
	public void removeComment(int position) {
		ArrayList<Comment> cList = new ArrayList<Comment>(Arrays.asList(comments));
		if (position < cList.size()) {
			cList.remove(position);
			Comment[] newComments = new Comment[cList.size()];
			for (int i = 0; i < cList.size(); i++) {
				newComments[i] = cList.get(i);
			}
			comments = newComments;
		}
	}
	
	public Comment[] getComments() {
		return comments;
	}

}
