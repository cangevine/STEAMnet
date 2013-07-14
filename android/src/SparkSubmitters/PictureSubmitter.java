package SparkSubmitters;

import java.io.IOException;
import java.util.Date;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.app.Activity;
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


public class PictureSubmitter extends SparkSubmitter {
	ViewGroup entryForm;
	ViewGroup pictureForm;
	ImageView imageThumb;
	Bitmap image;
	Uri imageURI;
	PictureTaker PictureTaker;
	PictureUploader PictureUploader;
	Context context;
	
	public PictureSubmitter(View v, MainActivity m) {
		super(m);
		
		context = (Context) mainActivity;
		entryForm = (ViewGroup) v;
		pictureForm = (ViewGroup) entryForm.findViewById(R.id.picture_form);
		
		imageThumb = (ImageView) v.findViewById(R.id.picture_thumbnail);
		assignClickListeners();
	}
	
	@Override
	public Spark getNewSpark(char sparkType) {
		Spark newSpark = new Spark(sparkType, 'P', "Is a picture");
		if (imageURI != null) {
			newSpark.setMultimedia(imageURI);
		}
		EditText tagsForm = (EditText) entryForm.findViewById(R.id.tag_entry_form);
		String tags = tagsForm.getText().toString();
		newSpark.setTags(tags);
		return newSpark;
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
	                    //Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
	                	Log.v("Problem - Picturetaker", "Picture was not taken");
	                } else {
	                    //Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
	                	Log.v("Problem - Picture taker", "Picture was not taken");
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
	            Log.v("Picture taker", ""+imageID);
	            return ""+imageID;
	        }
	     
	     public class LoadImagesFromSDCard  extends AsyncTask<String, Void, Void> {
	            //private ProgressDialog Dialog = new ProgressDialog(pictureSubmitter);
	            Bitmap mBitmap;
	            Bitmap mScaledBitmap;
	             
	            protected void onPreExecute() {
	                // Progress Dialog
	                //Dialog.setMessage(" Loading image from Sdcard..");
	                //Dialog.show();
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
	                    } catch (IOException e) {
	                        cancel(true);
	                    }
	                return null;
	            }
	             
	            protected void onPostExecute(Void unused) {
	                // Close progress dialog
	                  //Dialog.dismiss();
	                if(mBitmap != null) {
	                  // Set Image to ImageView
	                   image = mBitmap;
	                   imageURI = imageUri;
	                   setThumbnail(mScaledBitmap);
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
	                    new LoadImagesFromSDCard().execute();
	                } else if ( resultCode == mainActivity.RESULT_CANCELED) {
	                	Log.v("Problem - PictureUploader", "Picture was not uploaded");
	                } else {
	                	Log.v("Problem - PictureUploader", "Picture was not uploaded");
	                }
	            }
	        }
	     
	     public class LoadImagesFromSDCard  extends AsyncTask<String, Void, Void> {
	            //private ProgressDialog Dialog = new ProgressDialog(pictureSubmitter);
	            Bitmap mBitmap;
	            Bitmap mScaledBitmap;
	             
	            protected void onPreExecute() {
	                /****** NOTE: You can call UI Element here. *****/
	                // Progress Dialog
	                //Dialog.setMessage(" Loading image from Sdcard..");
	                //Dialog.show();
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
	                  //Dialog.dismiss();
	                if(mBitmap != null) {
	                   //showImg.setImageBitmap(mBitmap);
	                   image = mBitmap;
	                   imageURI = imageUri;
	                   setThumbnail(mScaledBitmap);
	                }      
	            }    
	        }        
	}
}
