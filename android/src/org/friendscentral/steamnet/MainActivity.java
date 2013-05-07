package org.friendscentral.steamnet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

public class MainActivity extends Activity {

    static final String EXTRA_MESSAGE = null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeIndexGridLayout();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void initializeIndexGridLayout() {
    	View indexGridLayout = findViewById(R.id.IndexGrid);
    	final GridView gridview = (GridView) indexGridLayout.findViewById(R.id.SparkGrid);
    	final IndexGrid ig = new IndexGrid();
    	ig.initIndexGrid(gridview, MainActivity.this, false);
    	
    	gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                openDetailView(ig.getSparks()[position]);
            }
        });
    	
    	
    	
    	Button forwardButton = (Button) findViewById(R.id.Forward_button);
    	Button reverseButton = (Button) findViewById(R.id.Reverse_button);
    	
    	forwardButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ig.initIndexGrid(gridview, MainActivity.this, false);
			}
    	});
    	
    	reverseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ig.initIndexGrid(gridview, MainActivity.this, true);
			}
    	});
    }
    
    public void openDetailView(SimpleSpark s) {
		Bundle b = new Bundle();
		b.putString("Name", s.getName());
		b.putInt("Id", s.getId());
    	Intent intent = new Intent(this, DetailActivity.class);
    	intent.putExtra(EXTRA_MESSAGE, b);
    	startActivity(intent);
    }
    
}
