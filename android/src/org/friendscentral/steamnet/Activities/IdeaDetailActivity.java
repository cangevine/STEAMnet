package org.friendscentral.steamnet.Activities;

import org.friendscentral.steamnet.CommentAdapter;
import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.SpinnerAdapter;
import org.friendscentral.steamnet.BaseClasses.Comment;
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;

import APIHandlers.PostComment;
import APIHandlers.RetrieveDataTaskGetSpark;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class IdeaDetailActivity extends Activity {
	private static final String TAG = "IdeaDetailActivity";
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
		userTextView = (TextView) findViewById(R.id.CreatorTextView);
		
		//Fixes autofocus problem:
        findViewById(R.id.DummyFocusCommentSection).setFocusableInTouchMode(true);
        findViewById(R.id.DummyFocusCommentSection).requestFocus();
		
		Intent intent = getIntent();
		idea = (Idea) intent.getSerializableExtra("idea");
		id = idea.getId();
		//title = idea.getTitle();
		sparkIds = idea.getSparkIds();
		date = idea.getCreatedAt();
		userID = idea.getUser();
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
    	
    	RetrieveDataTaskGetSpark r = null;
    	for (int i = 0; i < sparkIds.length; i++) {
    		 r = new RetrieveDataTaskGetSpark(sparkIds[i], gridView, indexGrid);
    	}
    	
    	adapter.notifyDataSetChanged();
    	
    	gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                openDetailView(indexGrid.getJawnAt(position)); //NEED POLYMORPHIC openDetailView
            }
        });
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
		findViewById(R.id.DummyFocusCommentSection).requestFocus();
		
		// TODO Make the @userID dynamic
		int userID = 0;
		PostComment comment = new PostComment(id, 'I', content, userID);
		
		if (comments.length > 0) {
			ListView commentSection = (ListView) findViewById(R.id.CommentList);
			CommentAdapter c = (CommentAdapter) commentSection.getAdapter();
			Comment newComment = new Comment(userID, content);
			c.addComment(newComment);
			c.notifyDataSetChanged();
		} else {
			ListView commentSection = (ListView) findViewById(R.id.CommentList);
			CommentAdapter commentAdapter = new CommentAdapter(this, comments);
			commentSection.setAdapter(commentAdapter);
			Comment newComment = new Comment(userID, content);
			CommentAdapter c = (CommentAdapter) commentSection.getAdapter();
			c.addComment(newComment);
			c.notifyDataSetChanged();
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

}
