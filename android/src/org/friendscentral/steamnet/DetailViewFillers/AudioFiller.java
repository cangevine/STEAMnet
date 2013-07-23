package org.friendscentral.steamnet.DetailViewFillers;

import java.io.IOException;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
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
	}
	
	public class AudioStreamer {
		Uri uri;
		boolean prepared;
		MediaPlayer mediaPlayer;
		ProgressDialog dialog; 
		
		public AudioStreamer(Uri u) {
			uri = u;
			prepared = false;
			
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			initMediaPlayer();
		}
		
		public void initMediaPlayer() {
			try {
				mediaPlayer.setDataSource(spark.getCloudLink());
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
			
			dialog = new ProgressDialog(context);
			dialog.setMessage("Loading audio file...");
			dialog.show();
		   	mediaPlayer.prepareAsync();
		   	
		   	mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer mp) {
					dialog.dismiss();
					stopPlayback.setEnabled(false);
					startPlayback.setEnabled(true);
					
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
	
	public AudioStreamer getAudioStreamer() {
		return audioStreamer;
	}
	
}
