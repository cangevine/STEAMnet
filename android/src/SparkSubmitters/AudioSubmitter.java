package SparkSubmitters;

import java.io.File;
import java.io.IOException;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Spark;

import SparkSubmitters.PictureSubmitter.PictureUploader;
import SparkSubmitters.PictureSubmitter.PictureUploader.LoadImagesFromSDCard;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AudioSubmitter extends SparkSubmitter {
	ViewGroup entryForm;
	ViewGroup audioForm;
	Context context;
	Uri recording;
	AudioStreamer audioStreamer;
	MediaPlayer mediaPlayer;
	
	AudioUploader audioUploader;
	AudioRecorder audioRecorder;

	public AudioSubmitter(View v, MainActivity m) {
		super(m);
		
		context = (Context) mainActivity;
		entryForm = (ViewGroup) v;
		audioForm = (ViewGroup) entryForm.findViewById(R.id.audio_form);
		
		assignClickListeners();
	}
	
	public void assignClickListeners() {
		final Button record = (Button) audioForm.findViewById(R.id.audio_record_button);
		final Button stop = (Button) audioForm.findViewById(R.id.audio_stop_button);
		final Button upload = (Button) audioForm.findViewById(R.id.audio_upload_button);
		final Button startPlayback = (Button) audioForm.findViewById(R.id.audio_start_playback_button);
		final Button stopPlayback = (Button) audioForm.findViewById(R.id.audio_stop_playback_button);
		
		upload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				audioUploader = new AudioUploader(AudioSubmitter.this);
				audioForm.findViewById(R.id.Audio_Recording_Buttons).setVisibility(View.GONE);
				audioForm.findViewById(R.id.Audio_Playback_Buttons).setVisibility(View.VISIBLE);
			}
		});
		
		record.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				audioRecorder = new AudioRecorder(record, stop); 
				try {
					audioRecorder.startRecording(null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (audioRecorder != null) {
					audioRecorder.stopRecording(null);
					audioForm.findViewById(R.id.Audio_Recording_Buttons).setVisibility(View.GONE);
					audioForm.findViewById(R.id.Audio_Playback_Buttons).setVisibility(View.VISIBLE);
				}
			}
		});
		
		startPlayback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (audioStreamer != null && mediaPlayer != null) {
					audioStreamer.play();
					stopPlayback.setEnabled(true);
					startPlayback.setEnabled(false);
				}
			}
		});
		
		stopPlayback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (audioStreamer != null && mediaPlayer != null) {
					audioStreamer.stop();
					stopPlayback.setEnabled(false);
					startPlayback.setEnabled(true);
				}
			}
		});
	}

	@Override
	public Spark getNewSpark(char sparkType) {
		Spark newSpark = new Spark(sparkType, 'A', "Is audio");
		if (recording != null) {
			newSpark.setMultimedia(recording);
		}
		EditText tagsForm = (EditText) entryForm.findViewById(R.id.tag_entry_form);
		String tags = tagsForm.getText().toString();
		newSpark.setTags(tags);
		return newSpark;
	}
	
	public AudioUploader getAudioUploader() {
		return audioUploader;
	}
	
	public class AudioUploader {
		final static int UPLOAD_AUDIO_ACTIVITY_REQUEST_CODE = 3;
		
		AudioSubmitter audioSubmitter;
	    Uri audioUri = null;
	    AudioUploader audioUploader = null;
		
		public AudioUploader(AudioSubmitter a) {
			audioSubmitter = a;
	        audioUploader = this;
	        Intent intent = new Intent();
	        intent.setType("audio/*");
	        intent.setAction(Intent.ACTION_GET_CONTENT);
	        mainActivity.startActivityForResult(Intent.createChooser(intent, "Select Audio"), UPLOAD_AUDIO_ACTIVITY_REQUEST_CODE);
		}
		
		public void onActivityResult( int requestCode, int resultCode, Intent data) {
            if ( requestCode == UPLOAD_AUDIO_ACTIVITY_REQUEST_CODE) {
                if ( resultCode == mainActivity.RESULT_OK) {
                    audioUri = data.getData();
                    if (audioUri != null) {
                    	recording = audioUri;
                    	audioStreamer = new AudioStreamer(recording);
                    }
                } else if ( resultCode == mainActivity.RESULT_CANCELED) {
                	Log.v("Problem - AudioUploader", "Audio was not uploaded");
                } else {
                	Log.v("Problem - AudioUploader", "Audio was not uploaded");
                }
            }
        }
	}
	
	public class AudioRecorder {

		  MediaRecorder recorder;
		  File audiofile = null;
		  private static final String TAG = "SoundRecordingActivity";
		  private View startButton;
		  private View stopButton;

		  public AudioRecorder(Button start, Button stop) {
			  Log.v(TAG, "Created");
			  startButton = start;
			  stopButton = stop;
		  }

		  public void startRecording(View view) throws IOException {
		    startButton.setEnabled(false);
		    stopButton.setEnabled(true);

		    File sampleDir = Environment.getExternalStorageDirectory();
		    try {
		      audiofile = File.createTempFile("sound", ".3gp", sampleDir);
		    } catch (IOException e) {
		      Log.e(TAG, "sdcard access error");
		      return;
		    }
		    recorder = new MediaRecorder();
		    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		    recorder.setOutputFile(audiofile.getAbsolutePath());
		    recorder.prepare();
		    recorder.start();
		  }

		  public void stopRecording(View view) {
		    startButton.setEnabled(true);
		    stopButton.setEnabled(false);
		    recorder.stop();
		    recorder.release();
		    addRecordingToMediaLibrary();
		  }

		  protected void addRecordingToMediaLibrary() {
		    ContentValues values = new ContentValues(4);
		    long current = System.currentTimeMillis();
		    values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
		    values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
		    values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
		    values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
		    ContentResolver contentResolver = mainActivity.getContentResolver();

		    Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		    Uri newUri = contentResolver.insert(base, values);
		    recording = newUri;

		    mainActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
		   	Log.v(TAG, "Added File " + newUri);
		   	
		   	audioStreamer = new AudioStreamer(recording); 
		  }
	}
	
	public class AudioStreamer {
		Uri uri;
		boolean prepared;
		
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
					audioForm.findViewById(R.id.audio_stop_playback_button).setEnabled(false);
					audioForm.findViewById(R.id.audio_start_playback_button).setEnabled(true);
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
