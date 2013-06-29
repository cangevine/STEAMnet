package org.friendscentral.steamnet;

import android.content.Context;
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
		LinearLayout comment = new LinearLayout(context);
		String username = comments[position].getUser();
		String commentContent = comments[position].getContent();
		
		TextView user = new TextView(context);
		user.setTextSize(20);
		user.setText(username);
		TextView content = new TextView(context);
		content.setText(commentContent);
		
		comment.addView(user);
		comment.addView(content);
		
		return comment;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
