package org.friendscentral.steamnet.SparkSubmitters;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.SparkWizard;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CodeSubmitter extends SparkSubmitter {
	View entryForm;
	SparkWizard sparkWizard;
	
	public CodeSubmitter(View v, MainActivity m) {
		super(m);
		
		entryForm = v;
	}

	@Override
	public Spark getNewSpark(char sparkType) {
		EditText contentForm = (EditText) entryForm.findViewById(R.id.code_content_entry_form);
		String content = contentForm.getText().toString();
		
		EditText tagsForm = (EditText) entryForm.findViewById(R.id.tag_entry_form);
		String tags = tagsForm.getText().toString();
		
		String verifier = content.toLowerCase();
		
		/*
		 * TODO: 
		 * Place this verifier into the "test" function rather than the
		 * Submit function
		 */
		if (verifier.contains("gist.github")) {
			Spark newSpark = new Spark(sparkType, 'C', content);
			newSpark.setTags(tags);
			return newSpark;
		} else {
			Toast.makeText(mainActivity, "Not a link to a GitHub gist. Try again", Toast.LENGTH_LONG).show();
			contentForm.setText("");
		}
		
		return null;
	}
}
