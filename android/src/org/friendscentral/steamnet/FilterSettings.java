package org.friendscentral.steamnet;

import APIHandlers.GetXIdeas;
import APIHandlers.RetrieveDataTaskGetXJawns;
import APIHandlers.RetrieveDataTaskGetXSparks;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;


public class FilterSettings {
	private static final String TAG = "FilterSettings";
	LinearLayout layout;
	LinearLayout mainLayout;
	Context context;
	GridView gridview;
	IndexGrid indexgrid;
	CheckBox sparkBox;
	CheckBox ideaBox;
	boolean sparkCheck;
	boolean ideaCheck;
	
	public FilterSettings(LinearLayout l, LinearLayout ml, Context c, GridView g, IndexGrid i) {
		layout = l;
		mainLayout = ml;
		context = c;
		gridview = g;
		indexgrid = i;
		
		sparkBox = (CheckBox) mainLayout.findViewById(R.id.sparkCheckBox);
		ideaBox = (CheckBox) mainLayout.findViewById(R.id.ideaCheckBox);
		sparkCheck = sparkBox.isChecked();
		ideaCheck = ideaBox.isChecked();
	}
	
	public void call(View v, String tag) {
		if (tag.equals("sortRecent")) {
			sortRecent();
		} else if (tag.equals("randomizeJawns")) {
			randomizeJawns();
		} else if (tag.equals("checkBoxChange")) {
			if (v.getId() == R.id.sparkCheckBox) {
				sparkBoxChange('S');
			} else {
				sparkBoxChange('I');
			}
		}
	}
	
	public void sparkBoxChange(char box) {
		sparkCheck = sparkBox.isChecked();
		ideaCheck = ideaBox.isChecked();
		gridview.setAdapter(new SpinnerAdapter(context, 16));
		if (sparkCheck == true && ideaCheck == true) {
			RetrieveDataTaskGetXJawns r = new RetrieveDataTaskGetXJawns(16, gridview, indexgrid); 
			JawnAdapter ja = indexgrid.getAdapter();
			ja.notifyDataSetChanged();
			indexgrid.setJawns(ja.getJawns());
			Log.v(TAG, "Both true");
		} else if (sparkCheck == true && ideaCheck == false) {
			RetrieveDataTaskGetXSparks r = new RetrieveDataTaskGetXSparks(16, gridview, indexgrid); 
			JawnAdapter ja = indexgrid.getAdapter();
			ja.notifyDataSetChanged();
			indexgrid.setJawns(ja.getJawns());
			Log.v(TAG, "Sparks true");
		} else if (sparkCheck == false && ideaCheck == true) {
			GetXIdeas i = new GetXIdeas(16, gridview, indexgrid); 
			JawnAdapter ja = indexgrid.getAdapter();
			ja.notifyDataSetChanged();
			indexgrid.setJawns(ja.getJawns());
			Log.v(TAG, "Idea true");
		} else {
			Log.v(TAG, "Both false");
			if (box == 'S') {
				ideaBox.setChecked(true);
				GetXIdeas i = new GetXIdeas(16, gridview, indexgrid); 
				JawnAdapter ja = indexgrid.getAdapter();
				ja.notifyDataSetChanged();
				indexgrid.setJawns(ja.getJawns());
			} else if (box == 'I') {
				sparkBox.setChecked(true);
				RetrieveDataTaskGetXSparks r = new RetrieveDataTaskGetXSparks(16, gridview, indexgrid); 
				JawnAdapter ja = indexgrid.getAdapter();
				ja.notifyDataSetChanged();
				indexgrid.setJawns(ja.getJawns());
			}
		}
	}
	
	public void sortRecent() {
		sparkBoxChange('Q');
	}
	
	public boolean getSparkBoxVal() {
		return sparkBox.isChecked();
	}
	
	public boolean getIdeaBoxVal() {
		return ideaBox.isChecked();
	}
	
	public void randomizeJawns() {
		String className = indexgrid.getAdapter().getClass().getName(); 
		// TODO Conditional throws an error if it's NOT  the JawnAdapter
		if (className.equals("org.friendscentral.steamnet.JawnAdapter")) {
			JawnAdapter ja = indexgrid.getAdapter();
			ja.shuffleJawns(indexgrid.getJawns());
			ja.notifyDataSetChanged();
			indexgrid.setJawns(ja.getJawns());
			
			//Not necessary but might be:
			//sparkEventHandler.clearEventHandlers();
			//sparkEventHandler.initializeIndexGridLayout();
		}
	}
}
