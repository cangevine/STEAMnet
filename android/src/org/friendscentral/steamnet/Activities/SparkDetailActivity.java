package org.friendscentral.steamnet.Activities;

import org.friendscentral.steamnet.CommentAdapter;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.BaseClasses.Comment;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.friendscentral.steamnet.DetailViewFillers.AudioFiller;
import org.friendscentral.steamnet.DetailViewFillers.CodeFiller;
import org.friendscentral.steamnet.DetailViewFillers.DetailFiller;
import org.friendscentral.steamnet.DetailViewFillers.LinkFiller;
import org.friendscentral.steamnet.DetailViewFillers.PictureFiller;
import org.friendscentral.steamnet.DetailViewFillers.TextFiller;
import org.friendscentral.steamnet.DetailViewFillers.VideoFiller;

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
	DetailFiller filler;
	private static final String TAG = "SparkDetailView";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spark_detail);
		
		findViewById(R.id.DummyFocusCommentSection).setFocusableInTouchMode(true);
        findViewById(R.id.DummyFocusCommentSection).requestFocus();
		
		Intent intent = getIntent();
		Spark spark = (Spark) intent.getSerializableExtra("spark");
		
		switch (spark.getContentType()) {
		case 'T':
			filler = new TextFiller(spark, (LinearLayout) findViewById(R.id.TextData), SparkDetailActivity.this);
			break;
		case 'A':
			filler = new AudioFiller(spark, (LinearLayout) findViewById(R.id.AudioData), SparkDetailActivity.this);
			break;
		case 'L':
			filler = new LinkFiller(spark, (LinearLayout) findViewById(R.id.LinkData), SparkDetailActivity.this);
			break;
		case 'P':
			filler = new PictureFiller(spark, (LinearLayout) findViewById(R.id.PictureData), SparkDetailActivity.this);
			break;
		case 'V':
			filler = new VideoFiller(spark, (LinearLayout) findViewById(R.id.VideoData), SparkDetailActivity.this);
			break;
		case 'C':
			filler = new CodeFiller(spark, (LinearLayout) findViewById(R.id.CodeData), SparkDetailActivity.this);
			break;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

}
