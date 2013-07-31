package org.friendscentral.steamnet.Activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.friendscentral.steamnet.CommentAdapter;
import org.friendscentral.steamnet.IdeaDetailAdapter;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.SpinnerAdapter;
import org.friendscentral.steamnet.BaseClasses.Comment;
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;

import APIHandlers.GetIdeaForDetail;
import APIHandlers.PostComment;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
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
	String[] tags;
	String description;
	Spark[] sparks;
	
	TextView titleTextView;
	TextView dateTextView;
	TextView userTextView;
	
	GridView gridView;
	IdeaDetailAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_idea_detail);
		Intent intent = getIntent();
		int ideaId = intent.getExtras().getInt("id");
		
		titleTextView = (TextView) findViewById(R.id.IdeaTitleTextView);
		dateTextView = (TextView) findViewById(R.id.TimestampTextView);
		userTextView = (TextView) findViewById(R.id.idea_user_name);
		
		//Fixes autofocus problem:
        findViewById(R.id.DummyFocusCommentSection).setFocusableInTouchMode(true);
        findViewById(R.id.DummyFocusCommentSection).requestFocus();
		
		new GetIdeaForDetail(ideaId, this);
	}
	
	public void initialize(Idea i) {
		idea = i;
		id = idea.getId();
		//title = idea.getTitle();
		sparks = idea.getSparks();
		date = idea.getCreatedAt();
		user = idea.getUsername();
		comments = idea.getComments();
		description = idea.getDescription();
		tags = idea.getTags();

		titleTextView.setText(description);
		userTextView.setText(user);
		//descriptionTextView.setText(description);
		
		initializeIndexGridLayout();
		fillDate();
		fillComments();
		fillTags();
	}
	
	public void initializeIndexGridLayout() {
    	final View indexGridLayout = findViewById(R.id.IndexGridIdeaDetail);
    	gridView = (GridView) indexGridLayout.findViewById(R.id.SparkGrid);
    	gridView.setNumColumns(2);
    	//initial spinners:
    	gridView.setAdapter(new SpinnerAdapter(this, 4));
    	
    	adapter = new IdeaDetailAdapter(this, sparks);
    	gridView.setAdapter(adapter);
    	
    	gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                openDetailView(adapter.getJawnAt(position)); //NEED POLYMORPHIC openDetailView
            }
        });
    }
	
	public void fillDate() {
		String dateString = date;
		String formattedDate = "";
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString);
			formattedDate = new SimpleDateFormat("h:ma; MMMM dd, yyyy").format(date);
		} catch (ParseException e) {
			formattedDate = "Unknown date";
		}
		dateTextView.setText(formattedDate);
	}
	
	public void fillTags() {
		if (idea.getTags() != null) {
			LinearLayout tagsHolder = (LinearLayout) findViewById(R.id.IdeaDescAndTags);
			
			String[] tags = idea.getTags();
			for (String tag : tags) {
				final Button t = new Button(this);
				t.setText(tag);
				t.setGravity(Gravity.CENTER_HORIZONTAL);
				
				t.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						STEAMnetApplication sna = (STEAMnetApplication) IdeaDetailActivity.this.getApplicationContext();
						sna.setSavedTag((String) t.getText());
						Intent intent = new Intent(IdeaDetailActivity.this, MainActivity.class);
				    	IdeaDetailActivity.this.startActivityForResult(intent, 0);
					}
				});
				
				tagsHolder.addView(t);
			}
			final Button t = new Button(this);
			t.setText("Find similar Sparks and Ideas");
			t.setGravity(Gravity.CENTER_HORIZONTAL);
			
			t.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					STEAMnetApplication sna = (STEAMnetApplication) IdeaDetailActivity.this.getApplicationContext();
					sna.setSavedTags(idea.getTags());
					Intent intent = new Intent(IdeaDetailActivity.this, MainActivity.class);
			    	IdeaDetailActivity.this.startActivityForResult(intent, 0);
				}
			});
			
			tagsHolder.addView(t);
		}
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
		findViewById(R.id.DummyFocusCommentSection).requestFocus();
		
		int userID = 0;
		STEAMnetApplication sna = (STEAMnetApplication) getApplication();
		if (!sna.getReadOnlyMode()) {
			userID = Integer.valueOf(sna.getUserId());
			String username = sna.getUsername();
			ListView commentSection = (ListView) findViewById(R.id.CommentList);
			CommentAdapter c = (CommentAdapter) commentSection.getAdapter();
			new PostComment(id, 'I', content, userID, username, sna.getToken(), c);
			
			editText.setText("");
		} else {
			Toast.makeText(this, "Please log in to submit a comment", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void openDetailView(Jawn j) {
	    Intent intent = new Intent(this, SparkDetailActivity.class);
	    //intent.putExtra(EXTRA_MESSAGE, b);
	    int id = ((Spark) j).getId();
	    intent.putExtra("id", id);
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
		sna.logOut();
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
