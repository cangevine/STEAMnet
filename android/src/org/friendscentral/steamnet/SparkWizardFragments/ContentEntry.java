package org.friendscentral.steamnet.SparkWizardFragments;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.SparkSubmitters.AudioSubmitter;
import org.friendscentral.steamnet.SparkSubmitters.LinkSubmitter;
import org.friendscentral.steamnet.SparkSubmitters.PictureSubmitter;
import org.friendscentral.steamnet.SparkSubmitters.SparkSubmitter;
import org.friendscentral.steamnet.SparkSubmitters.TextSubmitter;
import org.friendscentral.steamnet.SparkSubmitters.VideoSubmitter;
import org.friendscentral.steamnet.SparkSubmitters.CodeSubmitter;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

@SuppressLint("ValidFragment")
public class ContentEntry extends Fragment {
	char type;
	SparkSubmitter a;
	MainActivity mainActivity;
	
	public ContentEntry(char t, MainActivity m) {
		type = t;
		mainActivity = m;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.content_entry, container, false);
		switch (type) {
		case 'T':
			v.findViewById(R.id.text_form).setVisibility(View.VISIBLE);
			a = new TextSubmitter(v, mainActivity);
			break;
		case 'A':
			v.findViewById(R.id.audio_form).setVisibility(View.VISIBLE);
			a = new AudioSubmitter(v, mainActivity);
			break;
		case 'P':
			v.findViewById(R.id.picture_form).setVisibility(View.VISIBLE);
			a = new PictureSubmitter(v, mainActivity);
			break;
		case 'V':
			v.findViewById(R.id.video_form).setVisibility(View.VISIBLE);
			a = new VideoSubmitter(v, mainActivity);
			break;
		case 'C':
			v.findViewById(R.id.code_form).setVisibility(View.VISIBLE);
			a = new CodeSubmitter(v, mainActivity);
			break;
		case 'L':
			v.findViewById(R.id.link_form).setVisibility(View.VISIBLE);
			a = new LinkSubmitter(v, mainActivity);
			break;
		}
		return v;
	}
	
	public SparkSubmitter getSparkSubmitter() {
		return a;
	}
}
