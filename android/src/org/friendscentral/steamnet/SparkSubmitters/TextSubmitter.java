package org.friendscentral.steamnet.SparkSubmitters;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.SparkWizard;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TextSubmitter extends SparkSubmitter {
	View entryForm;
	SparkWizard sparkWizard;
	
	public TextSubmitter(View v, MainActivity m) {
		super(m);
		
		entryForm = v;
	}

	@Override
	public Spark getNewSpark(char sparkType) {
		EditText tagsForm = (EditText) entryForm.findViewById(R.id.tag_entry_form);
		String tags = tagsForm.getText().toString();
		
		EditText contentForm = (EditText) entryForm.findViewById(R.id.text_content_entry_form);
		String content = contentForm.getText().toString();
		
		if (content.equals("")) {
			Toast.makeText(mainActivity, "Please enter some content", Toast.LENGTH_LONG).show();
			return null;
		}
		
		STEAMnetApplication sna = (STEAMnetApplication) mainActivity.getApplication();
		String userId = "0";
		if (sna.getUserId() != null) {
			userId = sna.getUserId();
		}
		Spark newSpark = new Spark(sparkType, 'T', content, userId, tags);
		newSpark.setTags(tags);
		
		return newSpark;
	}
}
