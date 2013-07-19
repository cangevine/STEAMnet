package org.friendscentral.steamnet.DetailViewFillers;

import java.io.IOException;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class AudioFiller extends DetailFiller {
	Button startPlayback;
	Button stopPlayback;
	AudioStreamer audioStreamer;
	
	public AudioFiller(Spark s, LinearLayout sparkData, Context c) {
		super(s, "Audio", sparkData, c);
		
		startPlayback = (Button) detailView.findViewById(R.id.audio_data_start_button);
		stopPlayback = (Button) detailView.findViewById(R.id.audio_data_stop_button);
		stopPlayback.setEnabled(false);
		
		fillData();
	}

	@Override
	void fillData() {
		audioStreamer = new AudioStreamer(spark.getUri());
		startPlayback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (audioStreamer != null) {
					audioStreamer.play();
					stopPlayback.setEnabled(true);
					startPlayback.setEnabled(false);
				}
			}
		});
		
		stopPlayback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (audioStreamer != null) {
					audioStreamer.stop();
					stopPlayback.setEnabled(false);
					startPlayback.setEnabled(true);
				}
			}
		});
	}
	
	public class AudioStreamer {
		Uri uri;
		boolean prepared;
		MediaPlayer mediaPlayer;
		
		public AudioStreamer(Uri u) {
			uri = u;
			prepared = false;
			
			mediaPlayer = new MediaPlayer();
			initMediaPlayer();
		}
		
		public void initMediaPlayer() {
			try {
				mediaPlayer.setDataSource(context, uri);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					prepared = true;
				}
		   	});
			
		   	mediaPlayer.prepareAsync();
		   	
		   	mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer mp) {
					stopPlayback.setEnabled(false);
					startPlayback.setEnabled(true);
					mediaPlayer.seekTo(0);
				} 
		   	});
		}
		
		public void play() {
			if (prepared) {
				mediaPlayer.start();
			}
		}
		
		public void stop() {
			if (prepared) {
				mediaPlayer.pause();
				mediaPlayer.seekTo(0);
			}
		}
	}
	
}
