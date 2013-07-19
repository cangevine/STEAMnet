package org.friendscentral.steamnet.DetailViewFillers;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextFiller extends DetailFiller {
	TextView text;

	public TextFiller(Spark s, LinearLayout sparkData, Context c) {
		super(s, "Text", sparkData, c);
		text = (TextView) detailView.findViewById(R.id.text_data_textview);
		text.setTextSize(50f);
		
		fillData();
	}

	@Override
	void fillData() {
		text.setText(content);
	}
	
}
