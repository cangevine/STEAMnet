package SparkSubmitters;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.SparkWizard;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class VideoSubmitter extends SparkSubmitter {
	View entryForm;
	SparkWizard sparkWizard;
	
	public VideoSubmitter(View v, MainActivity m) {
		super(m);
		
		entryForm = v;
	}

	@Override
	public Spark getNewSpark(char sparkType) {
		EditText contentForm = (EditText) entryForm.findViewById(R.id.video_content_entry_form);
		String content = contentForm.getText().toString();
		
		EditText tagsForm = (EditText) entryForm.findViewById(R.id.tag_entry_form);
		String tags = tagsForm.getText().toString();
		
		String verifier = content.toLowerCase();
		if (verifier.contains("youtube")) {
			Spark newSpark = new Spark(sparkType, 'V', content);
			newSpark.setTags(tags);
			return newSpark;
		} else if (verifier.contains("youtu.be")) {
			Spark newSpark = new Spark(sparkType, 'V', content);
			newSpark.setTags(tags);
			return newSpark;
		} else {
			Toast.makeText(mainActivity, "Not a Youtube link. Try again", Toast.LENGTH_LONG).show();
			contentForm.setText("");
		}
		
		return null;
	}

}
