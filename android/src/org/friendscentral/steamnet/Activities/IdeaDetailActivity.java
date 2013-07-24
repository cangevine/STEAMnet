package org.friendscentral.steamnet.Activities;

import org.friendscentral.steamnet.CommentAdapter;
import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.SpinnerAdapter;
import org.friendscentral.steamnet.BaseClasses.Comment;
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;

import APIHandlers.GetSpark;
import APIHandlers.PostComment;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class IdeaDetailActivity extends Activity {
	final static int GET_AUTH_ACTIVITY_REQUEST_CODE = 4;
	
	Idea idea;
	int id;
	String title;
	int[] sparkIds;
	String date;
	int userID;
	String user;
	Comment[] comments;
	
	TextView titleTextView;
	TextView dateTextView;
	TextView userTextView;
	
	GridView gridView;
	IndexGrid indexGrid;
	JawnAdapter adapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_idea_detail);
		titleTextView = (TextView) findViewById(R.id.IdeaTitleTextView);
		dateTextView = (TextView) findViewById(R.id.TimestampTextView);
		userTextView = (TextView) findViewById(R.id.idea_user_name);
		
		//Fixes autofocus problem:
        findViewById(R.id.DummyFocusCommentSection).setFocusableInTouchMode(true);
        findViewById(R.id.DummyFocusCommentSection).requestFocus();
		
		Intent intent = getIntent();
		idea = (Idea) intent.getSerializableExtra("idea");
		id = idea.getId();
		//title = idea.getTitle();
		sparkIds = idea.getSparkIds();
		date = idea.getCreatedAt();
		userID = idea.getUsers()[0];
		comments = idea.getComments();
		// TODO get user from ID
		user = "--an unknown user--";
		
		//titleTextView.setText(title);
		dateTextView.setText(date);
		userTextView.setText(user);
		
		initializeIndexGridLayout();
		fillComments();
	}
	
	public void initializeIndexGridLayout() {
    	final View indexGridLayout = findViewById(R.id.IndexGridIdeaDetail);
    	gridView = (GridView) indexGridLayout.findViewById(R.id.SparkGrid);
    	gridView.setNumColumns(2);
    	//initial spinners:
    	gridView.setAdapter(new SpinnerAdapter(this, 4));
    	
    	indexGrid = new IndexGrid();
    	indexGrid.initIndexGrid(gridView, this, true);
    	adapter = new JawnAdapter(this, new Jawn[0], sparkIds.length);
    	indexGrid.setAdapter(adapter);
    	indexGrid.setJawns(adapter.getJawns());
    	
    	for (int i = 0; i < sparkIds.length; i++) {
    		 new GetSpark(sparkIds[i], gridView, indexGrid);
    	}
    	
    	adapter.notifyDataSetChanged();
    	
    	gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                openDetailView(indexGrid.getJawnAt(position)); //NEED POLYMORPHIC openDetailView
            }
        });
    }
	
	public void fillComments() {
		ListView commentSection = (ListView) IdeaDetailActivity.this.findViewById(R.id.CommentList);
		CommentAdapter commentAdapter = new CommentAdapter(IdeaDetailActivity.this, comments);
		if (comments.length == 0) {
			Comment c = new Comment(0, "No comments on this Spark, be the first!", "STEAMnet");
			commentAdapter.addComment(c);
			commentAdapter.notifyDataSetChanged();
		}
		commentSection.setAdapter(commentAdapter);
	}
	
	public void submitComment(View v) {
		EditText editText = (EditText) findViewById(R.id.CommentEditText);
		String content = editText.getText().toString();
		editText.setText("");
		findViewById(R.id.DummyFocusCommentSection).requestFocus();
		
		int userID = 0;
		STEAMnetApplication sna = (STEAMnetApplication) getApplication();
		if (sna.getUserId() != null) {
			userID = Integer.valueOf(sna.getUserId());
		}
		if (sna.getUsername() != null) {
			String username = sna.getUsername();
			new PostComment(id, "S".charAt(0), content, userID, username, sna.getToken());
			
			ListView commentSection = (ListView) IdeaDetailActivity.this.findViewById(R.id.spark_social_section).findViewById(R.id.CommentList);
			CommentAdapter c = (CommentAdapter) commentSection.getAdapter();
			Comment newComment = new Comment(userID, content, username);
			c.addComment(newComment);
			if (c.getComments()[0].getUserId() == 0) {
				c.removeComment(0);
			}
			c.notifyDataSetChanged();
			editText.setText("");
		} else {
			Toast.makeText(this, "Please log in to submit a comment", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void openDetailView(Jawn j) {
	    Intent intent = new Intent(this, SparkDetailActivity.class);
	    //intent.putExtra(EXTRA_MESSAGE, b);
	    Spark s = j.getSelfSpark();
	    intent.putExtra("spark", s);
	    this.startActivity(intent);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
    	actionBar.setCustomView(R.layout.log_in_action_bar);
    	STEAMnetApplication sna = (STEAMnetApplication) getApplication();
    	if (sna.getUserId() != null) {
    		Button logButton = (Button) actionBar.getCustomView().findViewById(R.id.log_in_button);
    		logButton.setText("Log out");
    		
    		logButton.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				logOut();
    			}
    		});
    		TextView logInInfo = (TextView) actionBar.getCustomView().findViewById(R.id.log_in_info); 
    		logInInfo.setText("Logged in as "+sna.getUsername());
    	} else {
    		actionBar.getCustomView().findViewById(R.id.log_in_button).setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				logIn();
    			}
        	});
    	}
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}
	
	public void logIn() {
		Intent intent = new Intent(IdeaDetailActivity.this, AuthActivity.class);
		startActivityForResult(intent, GET_AUTH_ACTIVITY_REQUEST_CODE);
	}
	
	public void logOut() {
		STEAMnetApplication sna = (STEAMnetApplication) getApplication();
		sna.setToken(null);
		sna.setUserId(null);
		sna.setUsername(null);
		sna.setReadOnlyMode(true);
		ActionBar actionBar = getActionBar();
    	actionBar.setCustomView(R.layout.log_in_action_bar);
    	actionBar.getCustomView().findViewById(R.id.log_in_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logIn();
			}
    	});
	}
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case GET_AUTH_ACTIVITY_REQUEST_CODE:
			Log.v("Idea Detail activity", "Should be here if you have just logged in though OAuth");
			STEAMnetApplication sna = (STEAMnetApplication) getApplication();
	    	if (!sna.getReadOnlyMode()) {
	    		ActionBar actionBar = getActionBar();
	    		Button logButton = (Button) actionBar.getCustomView().findViewById(R.id.log_in_button);
	    		logButton.setText("Log out");
	    		
	    		logButton.setOnClickListener(new OnClickListener() {
	    			public void onClick(View v) {
	    				logOut();
	    			}
	    		});
	    		TextView logInInfo = (TextView) actionBar.getCustomView().findViewById(R.id.log_in_info); 
	    		logInInfo.setText("Logged in as "+sna.getUsername());
	    	}
	    	break;
		}
	}

}
