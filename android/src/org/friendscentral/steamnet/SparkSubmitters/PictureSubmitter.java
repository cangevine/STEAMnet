package org.friendscentral.steamnet.SparkSubmitters;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;


public class PictureSubmitter extends SparkSubmitter {
	ViewGroup entryForm;
	ViewGroup pictureForm;
	ImageView imageThumb;
	Bitmap image;
	Bitmap thumbImage;
	Uri imageURI;
	PictureTaker PictureTaker;
	PictureUploader PictureUploader;
	Context context;
	
	public PictureSubmitter(View v, MainActivity m) {
		super(m);
		
		context = (Context) mainActivity;
		entryForm = (ViewGroup) v;
		pictureForm = (ViewGroup) entryForm.findViewById(R.id.picture_form);
		entryForm.findViewById(R.id.submit_content_entry_button).setEnabled(false);
		
		imageThumb = (ImageView) v.findViewById(R.id.picture_thumbnail);
		assignClickListeners();
	}
	
	@Override
	public Spark getNewSpark(char sparkType) {
		if (image != null) {			
			Date date = new Date();
	    	SimpleDateFormat s = new SimpleDateFormat("yyyy_MM_dd_HHmm", Locale.US);

			EditText tagsForm = (EditText) entryForm.findViewById(R.id.tag_entry_form);
			String tags = tagsForm.getText().toString();
			
			STEAMnetApplication sna = (STEAMnetApplication) mainActivity.getApplication();
			String userId = "0";
			if (sna.getUserId() != null) {
				userId = sna.getUserId();
			}
			Log.v("PictureSubmitter", "userID: "+userId);
			Spark newSpark = new Spark(sparkType, 'P', "Picture: "+ s.format(date), userId, tags);
			float aspectRatio = ((float) image.getWidth()) / ((float) image.getHeight());
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, (int) (aspectRatio * 500), 500, true);
			newSpark.setBitmap(scaledBitmap);
			newSpark.setUri(imageURI);
			
			return newSpark;
		}
		return null;
	}
	
	public void assignClickListeners() {
		final Button upload = (Button) entryForm.findViewById(R.id.picture_upload_button);
		final Button camera = (Button) entryForm.findViewById(R.id.picture_camera_button);
		
		upload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PictureUploader = new PictureUploader(imageThumb, PictureSubmitter.this);
				View view = null;
				for (int i = 0; i < pictureForm.getChildCount(); i++) {
					view = pictureForm.getChildAt(i);
					if (view.getId() != R.id.picture_thumbnail) {
						view.setVisibility(View.GONE);
					} else {
						LayoutParams layoutParams = new LayoutParams(200, 140);
						view.setLayoutParams(layoutParams);
					}
				}
			}
		});
		
		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PictureTaker = new PictureTaker(imageThumb, PictureSubmitter.this);
				View view = null;
				for (int i = 0; i < pictureForm.getChildCount(); i++) {
					view = pictureForm.getChildAt(i);
					if (view.getId() != R.id.picture_thumbnail) {
						view.setVisibility(View.GONE);
					} else {
						LayoutParams layoutParams = new LayoutParams(150, 150);
						view.setLayoutParams(layoutParams);
					}
				}
			}
		});
	}
	
	public void setThumbnail(Bitmap b) {
		imageThumb.setImageResource(0);
		imageThumb.setImageBitmap(b);
		imageThumb.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
	}
	
	public PictureTaker getPictureTaker() {
		return PictureTaker;
	}
	
	public PictureUploader getPictureUploader() {
		return PictureUploader;
	}
	
	public class PictureTaker {
		 
	    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	     
	    PictureSubmitter pictureSubmitter;
	    Uri imageUri                      = null;
	    ImageView showImg  = null;
	    PictureTaker CameraActivity = null;
	     

	    public PictureTaker(ImageView i, PictureSubmitter p) {
	    	pictureSubmitter = p;
	    	MainActivity m = mainActivity;
	    	
	        CameraActivity = this;
	        showImg = i;
	        Date date = new Date();
	        String fileName = date.toString()+".jpg";
	        ContentValues values = new ContentValues();
	        values.put(MediaStore.Images.Media.TITLE, fileName);
	        values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
	        // imageUri is the current activity attribute, define and save it for later usage  
	        imageUri = mainActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	                 
	        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	        mainActivity.startActivityForResult( intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	    }
	 
	 
	     public void onActivityResult( int requestCode, int resultCode, Intent data) {
	            if ( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {   
	                if ( resultCode == mainActivity.RESULT_OK) {
	                   /*********** Load Captured Image And Data Start ****************/
	                    String imageId = convertImageUriToFile( imageUri, mainActivity);
	                    //  Create and excecute AsyncTask to load capture image
	                    new LoadImagesFromSDCard().execute(""+imageId);
	                     
	                  /*********** Load Captured Image And Data End ****************/
	                } else if ( resultCode == mainActivity.RESULT_CANCELED) {
	                    Toast.makeText(mainActivity, "Error: Picture was not taken or could not be uploaded", Toast.LENGTH_SHORT).show();
	                } else {
	                	Toast.makeText(mainActivity, "Error: Picture was not taken or could not be uploaded", Toast.LENGTH_SHORT).show();
	                }
	            }
	        }
	      
	      
	     /************ Convert Image Uri path to physical path **************/
	      
	     @SuppressWarnings("deprecation")
		public String convertImageUriToFile ( Uri imageUri, Activity activity )  {
	            Cursor cursor = null;
	            int imageID = 0;
	            try {
	                /*********** Which columns values want to get *******/
	                String [] proj={
	                                 MediaStore.Images.Media.DATA,
	                                 MediaStore.Images.Media._ID,
	                                 MediaStore.Images.Thumbnails._ID,
	                                 MediaStore.Images.ImageColumns.ORIENTATION
	                               };
	                cursor = activity.managedQuery(
	                                imageUri,         //  Get data for specific image URI
	                                proj,             //  Which columns to return
	                                null,             //  WHERE clause; which rows to return (all rows)
	                                null,             //  WHERE clause selection arguments (none)
	                                null              //  Order-by clause (ascending by name)    
	                             );    
	                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
	                int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
	                int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	                int size = cursor.getCount();
	                if (size != 0) {
	                    int thumbID = 0;
	                    if (cursor.moveToFirst()) {
	                        imageID     = cursor.getInt(columnIndex);
	                        thumbID     = cursor.getInt(columnIndexThumb);
	                        String Path = cursor.getString(file_ColumnIndex);
	                    }
	                } 
	            } finally {
	                if (cursor != null) {
	                	Log.v("Cursor", "Closing");
	                	mainActivity.stopManagingCursor(cursor);
	                    cursor.close();
	                }
	            }
	            return ""+imageID;
	        }
	     
	     public class LoadImagesFromSDCard  extends AsyncTask<String, Void, Void> {
	            ProgressDialog dialog;
	            Bitmap mBitmap;
	            Bitmap mScaledBitmap;
	             
	            protected void onPreExecute() {
	                // Progress Dialog
	            	dialog = new ProgressDialog(mainActivity);
	                dialog.setMessage("Loading image from SD card...");
	                dialog.show();
	            }
	 
	 
	            // Call after onPreExecute method
	            protected Void doInBackground(String... urls) {
	                Bitmap bitmap = null;
	                Bitmap scaledBitmap = null;
	                Uri uri = null;    
	                    try {
	                        /**  Uri.withAppendedPath Method Description
	                        * Parameters
	                        *    baseUri  Uri to append path segment to
	                        *    pathSegment  encoded path segment to append
	                        * Returns
	                        *    a new Uri based on baseUri with the given segment appended to the path
	                        */
	                        uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + urls[0]);
	                        /**************  Decode an input stream into a bitmap. *********/
	                        bitmap = BitmapFactory.decodeStream(mainActivity.getContentResolver().openInputStream(uri));
	                        mBitmap = bitmap;
	                        if (bitmap != null) {
	                            /********* Creates a new bitmap, scaled from an existing bitmap. ***********/
	                            scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 140, true); 
	                            if (scaledBitmap != null) {
	                                mScaledBitmap = scaledBitmap;
	                            }
	                        }
	                        return null;
	                    } catch (IOException e) {
	                        cancel(true);
	                    }
	                return null;
	            }
	             
	            protected void onPostExecute(Void unused) {
	            	Log.v("PictureTaker", "PostExecute called");
	                dialog.dismiss();
	                if(mBitmap != null) {
	                	float aspectRatio = ((float) mBitmap.getWidth()) / ((float) mBitmap.getHeight());
	                	if (mBitmap.getWidth() > 500 || mBitmap.getHeight() > 500) {
	       					Bitmap smallerBitmap = Bitmap.createScaledBitmap(mBitmap, (int) (aspectRatio * 500), 500, true);
	       					Log.v("PictureTaker", String.valueOf(smallerBitmap.getHeight()));
	       					image = smallerBitmap;
	                   } else {
	                	   	image = mBitmap;
	                   }
	       				imageURI = imageUri;
	       				setThumbnail(mScaledBitmap);
	       				entryForm.findViewById(R.id.submit_content_entry_button).setEnabled(true);
	                } else {
	                	Toast.makeText(mainActivity, "Error: Picture could not be loaded", Toast.LENGTH_SHORT).show();
	                }
	            }    
	        }        
	}
	
	public class PictureUploader {
		 
	    final static int UPLOAD_IMAGE_ACTIVITY_REQUEST_CODE = 2;
	     
	    PictureSubmitter pictureSubmitter;
	    Uri imageUri                      = null;
	    ImageView showImg  = null;
	    PictureUploader PictureUploader = null;
	     

	    public PictureUploader(ImageView i, PictureSubmitter p) {
	    	pictureSubmitter = p;
	        PictureUploader = this;
	        showImg = i;
	        Intent intent = new Intent();
	        intent.setType("image/*");
	        intent.setAction(Intent.ACTION_GET_CONTENT);
	        mainActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), UPLOAD_IMAGE_ACTIVITY_REQUEST_CODE);
	    }
	 
	 
	     public void onActivityResult( int requestCode, int resultCode, Intent data) {
	            if ( requestCode == UPLOAD_IMAGE_ACTIVITY_REQUEST_CODE) {
	                if ( resultCode == mainActivity.RESULT_OK) {
	                    imageUri = data.getData();
	                    Log.v("PICTURESUBMITTER", imageUri.getPath());
	                    new LoadImagesFromSDCard().execute();
	                } else if ( resultCode == mainActivity.RESULT_CANCELED) {
	                	Toast.makeText(mainActivity, "Error: Picture could not be uploaded", Toast.LENGTH_SHORT).show();
	                } else {
	                	Toast.makeText(mainActivity, "Error: Picture could not be uploaded", Toast.LENGTH_SHORT).show();
	                }
	            }
	        }
	     
	     public class LoadImagesFromSDCard  extends AsyncTask<String, Void, Void> {
	            ProgressDialog dialog;
	            Bitmap mBitmap;
	            Bitmap mScaledBitmap;
	             
	            protected void onPreExecute() {
	            	dialog = new ProgressDialog(mainActivity);
	                dialog.setMessage("Loading image from SD card...");
	                dialog.show();
	            }
	            
	            protected Void doInBackground(String... urls) {
	                Bitmap bitmap = null;
	                Bitmap scaledBitmap = null;  
	                    try {
	                        bitmap = BitmapFactory.decodeStream(mainActivity.getContentResolver().openInputStream(imageUri));
	                        mBitmap = bitmap;
	                        if (bitmap != null) {
	                            /********* Creates a new bitmap, scaled from an existing bitmap. ***********/
	                            scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 140, true);
	                            if (scaledBitmap != null) {
	                                mScaledBitmap = scaledBitmap;
	                            }
	                        }
	                    } catch (IOException e) {
	                        cancel(true);
	                    }
	                return null;
	            }
	             
	            protected void onPostExecute(Void unused) {
	                // Close progress dialog
	                dialog.dismiss();
	                if(mBitmap != null) {
	                   //showImg.setImageBitmap(mBitmap);
	                   float aspectRatio = ((float) mBitmap.getWidth()) / ((float) mBitmap.getHeight());
	                   if (mBitmap.getWidth() > 500 || mBitmap.getHeight() > 500) {
	       					Bitmap smallerBitmap = Bitmap.createScaledBitmap(mBitmap, (int) (aspectRatio * 500), 500, true);
	       					image = smallerBitmap;
	                   } else {
	                	   	image = mBitmap;
	                   }
	                   imageURI = imageUri;
	                   setThumbnail(mScaledBitmap);
	                   entryForm.findViewById(R.id.submit_content_entry_button).setEnabled(true);
	                } else {
	                	Toast.makeText(mainActivity, "Error: Picture could not be uploaded", Toast.LENGTH_SHORT).show();
	                }
	            }    
	        }        
	}
}
