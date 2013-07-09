package org.friendscentral.steamnet.SparkWizardFragments;

import org.friendscentral.steamnet.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class ContentTypeChooser extends Fragment {
	int pic_src = 0;
	int vid_src = 0;
	int link_src = 0;
	int aud_src = 0;
	int code_src = 0;
	int text_src = 0;
	String type = "Inspiration";
	
	@SuppressLint("ValidFragment")
	public ContentTypeChooser(char t) {
		switch (t) {
			case 'I':
				type = "Inspiration";
				pic_src = R.drawable.btn_blue_picture;
				vid_src = R.drawable.btn_blue_video;
				link_src = R.drawable.btn_blue_link;
				aud_src = R.drawable.btn_blue_audio;
				code_src = R.drawable.btn_blue_code;
				text_src = R.drawable.btn_blue_text;
				break;
			case 'W':
				type = "What-if?";
				pic_src = R.drawable.btn_green_picture;
				vid_src = R.drawable.btn_green_video;
				link_src = R.drawable.btn_green_link;
				aud_src = R.drawable.btn_green_audio;
				code_src = R.drawable.btn_green_code;
				text_src = R.drawable.btn_green_text;
				break;
			case 'P':
				type = "Problem!";
				pic_src = R.drawable.btn_red_picture;
				vid_src = R.drawable.btn_red_video;
				link_src = R.drawable.btn_red_link;
				aud_src = R.drawable.btn_red_audio;
				code_src = R.drawable.btn_red_code;
				text_src = R.drawable.btn_red_text;
				break;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.content_type_chooser, container, false); 
		TextView header = (TextView) v.findViewById(R.id.ContentTypeChooserHeader); 
		header.setText(type + " - Choose type:");
		v.findViewById(R.id.picture_button).setBackgroundResource(pic_src);
		v.findViewById(R.id.video_button).setBackgroundResource(vid_src);
		v.findViewById(R.id.link_button).setBackgroundResource(link_src);
		v.findViewById(R.id.audio_button).setBackgroundResource(aud_src);
		v.findViewById(R.id.code_button).setBackgroundResource(code_src);
		v.findViewById(R.id.text_button).setBackgroundResource(text_src);
		return v;
	}
}
