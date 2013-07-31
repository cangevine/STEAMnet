package org.friendscentral.steamnet;

import org.friendscentral.steamnet.Activities.MainActivity;

import APIHandlers.GetXIdeas;
import APIHandlers.GetXJawns;
import APIHandlers.GetXJawnsByTag;
import APIHandlers.GetXRandomJawns;
import APIHandlers.GetXSparks;
import APIHandlers.MultimediaLoader;
import APIHandlers.UserLoader;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


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
	
	GetXJawnsByTag tagFetcher;
	
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
		
		initTagSearching();
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
		} else if (tag.equals("revertTagSearch")) {
			revertTagSearch();
		}
	}
	
	public void initTagSearching() {
		final EditText tagEntry = (EditText) ((Activity) context).findViewById(R.id.tag_search_edit_text);
		final Button tagSubmit = (Button) ((Activity) context).findViewById(R.id.tag_search_button);
		
		tagSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String tagSearch = tagEntry.getText().toString();
				tagSearch = tagSearch.trim();
				tagSearch = tagSearch.toLowerCase();
				if (tagSearch.length() > 0) {
					ActionBar ab = ((Activity) context).getActionBar();
					LinearLayout customLayout = (LinearLayout) ab.getCustomView();
					TextView header = (TextView) customLayout.findViewById(R.id.tag_info);
					header.setVisibility(View.VISIBLE);
					header.setText("Searching for \""+tagSearch+"\"");
					
					Button backButton = (Button) customLayout.findViewById(R.id.tag_back_button);
					backButton.setVisibility(View.VISIBLE);
					backButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							call(view, "revertTagSearch");
						}
					});
					
					tagFetcher = new GetXJawnsByTag(50, gridview, indexgrid, context, tagSearch);
					
					tagEntry.setText("");
				} else {
					Toast.makeText(context, "Please enter a tag", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		//Make button dynamically enabled:
		tagEntry.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) { }
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) { }
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() > 0) {
					tagSubmit.setEnabled(true);
				} else {
					tagSubmit.setEnabled(false);
				}
			}
		});
	}
	
	public void revertTagSearch() {
		ActionBar ab = ((Activity) context).getActionBar();
		LinearLayout customLayout = (LinearLayout) ab.getCustomView();
		customLayout.findViewById(R.id.tag_info).setVisibility(View.INVISIBLE);
		customLayout.findViewById(R.id.tag_back_button).setVisibility(View.INVISIBLE);
		
		STEAMnetApplication sna = (STEAMnetApplication) context.getApplicationContext();
		if (sna.getCurrentTask() != null) {
			sna.getCurrentTask().cancel(true);
			if (sna.getCurrentMultimediaTask() != null)
				sna.getCurrentMultimediaTask().cancel(true);
			if (sna.getCurrentUserTask() != null)
				sna.getCurrentUserTask().cancel(true);
		}
		
		if (tagFetcher != null) {
			if (tagFetcher.getSavedAdapter() != null)
				indexgrid.setAdapter(tagFetcher.getSavedAdapter());
			if (tagFetcher.getSavedJawns() != null)
				indexgrid.setJawns(tagFetcher.getSavedJawns());
			tagFetcher = null;
		}
		
		new LoadMultimediaInBackground(indexgrid);
		new LoadUsersInBackground(indexgrid);
		//new GetXJawns(50, gridview, indexgrid, context);
	}
	
	public void sparkBoxChange(char box) {
		((MainActivity) context).getSparkEventHandler().clearEventHandlers();
		
		sparkCheck = sparkBox.isChecked();
		ideaCheck = ideaBox.isChecked();
		gridview.setAdapter(new SpinnerAdapter(context, 16));
		if (sparkCheck == true && ideaCheck == true) {
			GetXJawns r = new GetXJawns(50, gridview, indexgrid, context); 
			JawnAdapter ja = indexgrid.getAdapter();
			ja.notifyDataSetChanged();
			indexgrid.setJawns(ja.getJawns());
			Log.v(TAG, "Both true");
		} else if (sparkCheck == true && ideaCheck == false) {
			GetXSparks r = new GetXSparks(50, gridview, indexgrid, context); 
			JawnAdapter ja = indexgrid.getAdapter();
			ja.notifyDataSetChanged();
			indexgrid.setJawns(ja.getJawns());
			Log.v(TAG, "Sparks true");
		} else if (sparkCheck == false && ideaCheck == true) {
			GetXIdeas i = new GetXIdeas(50, gridview, indexgrid, context); 
			JawnAdapter ja = indexgrid.getAdapter();
			ja.notifyDataSetChanged();
			indexgrid.setJawns(ja.getJawns());
			Log.v(TAG, "Idea true");
		} else {
			Log.v(TAG, "Both false");
			if (box == 'S') {
				ideaBox.setChecked(true);
				GetXIdeas i = new GetXIdeas(50, gridview, indexgrid, context); 
				JawnAdapter ja = indexgrid.getAdapter();
				ja.notifyDataSetChanged();
				indexgrid.setJawns(ja.getJawns());
			} else if (box == 'I') {
				sparkBox.setChecked(true);
				GetXSparks r = new GetXSparks(50, gridview, indexgrid, context); 
				JawnAdapter ja = indexgrid.getAdapter();
				ja.notifyDataSetChanged();
				indexgrid.setJawns(ja.getJawns());
			}
		}
		
		((MainActivity) context).getSparkEventHandler().initializeIndexGridLayout();
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
	
	public int getCheckedRadioButton() {
		RadioGroup rg = (RadioGroup) mainLayout.findViewById(R.id.sortingRadioButtons);
		return rg.getCheckedRadioButtonId();
	}
	
	public RadioButton getRecentRB() {
		return (RadioButton) mainLayout.findViewById(R.id.recentJawnsRadio);
	}
	
	public void randomizeJawns() {
		new GetXRandomJawns(50, gridview, indexgrid, context, sparkBox.isChecked(), ideaBox.isChecked());
	}
	
	class LoadMultimediaInBackground extends AsyncTask<IndexGrid, Void, Void> {
		
		public LoadMultimediaInBackground(IndexGrid indexGrid) {
			this.execute(indexGrid);
		}
		
		@Override
		protected Void doInBackground(IndexGrid... i) {
			new MultimediaLoader(i[0], i[0].getAdapter(), (STEAMnetApplication) context.getApplicationContext());
			return null;
		}
	}
	
	class LoadUsersInBackground extends AsyncTask<IndexGrid, Void, Void> {
		
		public LoadUsersInBackground(IndexGrid indexGrid) {
			this.execute(indexGrid);
		}
		
		@Override
		protected Void doInBackground(IndexGrid... i) {
			new UserLoader(i[0], i[0].getAdapter(), (STEAMnetApplication) context.getApplicationContext());
			return null;
		}
	}
}
