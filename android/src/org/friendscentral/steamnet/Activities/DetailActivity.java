package org.friendscentral.steamnet.Activities;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class DetailActivity extends Activity {
	
	final String TAG = "DetailView";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		LinearLayout sparkData = (LinearLayout) findViewById(R.id.SparkData);
		
		Intent intent = getIntent();
		Spark spark = (Spark) intent.getSerializableExtra("spark");	
		String content = spark.getContent();
		int id = spark.getId();
		String sparkType = spark.getSparkTypeString();
		String createdAt = spark.getCreatedAt();
		String contentType = spark.getContentTypeString();
		
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
		
		Log.d(TAG, contentType);
		if(contentType.equals("T")){
			TextView sparkContentView = (TextView) findViewById(R.id.SparkTitleTextView);
			sparkContentView.setText(content);
			
			TextView sparkTypeView = (TextView) findViewById(R.id.SparkTypeTextView);
			sparkTypeView.setText(sparkType);
			
			TextView createdAtView = (TextView) findViewById(R.id.TimestampTextView);
			createdAtView.setText(createdAt);
			
		}
		//ImageView imgview = (ImageView) findViewById(R.id.pic);
		
		if (contentType.equals("P")) {
			ImageView imgview = new ImageView(this);
			imgview.setImageResource(id);
			
			LinearLayout detailLayout = (LinearLayout) findViewById(R.id.detail_linear_layout);
			LinearLayout dataSection = (LinearLayout) detailLayout.findViewById(R.id.SparkData);
			dataSection.addView(imgview);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

}
