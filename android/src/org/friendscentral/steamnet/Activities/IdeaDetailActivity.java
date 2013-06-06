package org.friendscentral.steamnet.Activities;

import org.friendscentral.steamnet.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IdeaDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		Intent intent = getIntent();
		String message = intent.getBundleExtra(MainActivity.EXTRA_MESSAGE).getString("Name");
		int id = intent.getBundleExtra(MainActivity.EXTRA_MESSAGE).getInt("Id");
		String sparkType = intent.getBundleExtra(MainActivity.EXTRA_MESSAGE).getString("Type");
		
		
		
		TextView textview = (TextView) findViewById(R.id.SparkTitleTextView);
		textview.setText(message);
		
		//ImageView imgview = (ImageView) findViewById(R.id.pic);
		
		if (sparkType.equals("Picture")) {
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
