package APIHandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.STEAMnetApplication;
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.squareup.okhttp.OkHttpClient;

public class MultimediaLoader {
	IndexGrid indexgrid;
	JawnAdapter jAdapter;
	STEAMnetApplication sna;
	boolean snaExists;
	int incriment = 3;
	
	public MultimediaLoader(IndexGrid ig, JawnAdapter j, STEAMnetApplication s) {
		indexgrid = ig;
		jAdapter = j;
		sna = s;
		snaExists = true;
		
		for (int i = 0; i < incriment; i++)
			loadMultimedia(i);
	}
	
	public MultimediaLoader(IndexGrid ig, JawnAdapter j) {
		indexgrid = ig;
		jAdapter = j;
		snaExists = false;
		
		for (int i = 0; i < incriment; i++)
			loadMultimedia(i);
	}
	
	public void loadMultimedia(int pos) {
		if (pos < jAdapter.getJawns().length) {
			MultimediaHelper m = new MultimediaHelper(pos, MultimediaLoader.this);
			if (snaExists)
				sna.setCurrentMultimediaTask(m);
		}
	}
	
	public class MultimediaHelper extends AsyncTask<String, Void, InputStream> {
		MultimediaLoader mLoader;
		int position;
		Jawn jawn;
		Spark spark;
		Idea idea;
		//Integer[] savedIndices = { null, null, null, null };
		int savedIndex = -1;
		ArrayList<String> ideaSparksJSON;
		ArrayList<Integer> unloadedThumbs;
		boolean edited = false;
		
		public MultimediaHelper(int n, MultimediaLoader m) {
			mLoader = m;
			position = n;
			jawn = jAdapter.getJawns()[n];
			this.execute();
		}
		
		@Override
		protected InputStream doInBackground(String... args) {
			if (jawn.getType() == 'S') {
				spark = jawn.getSelfSpark();
				if (spark.getBitmap() == null && spark.getContentType() != 'T' && spark.getContentType() != 'A') {
					if (spark.getCloudLink() != null) {
						try {
							String url = spark.getCloudLink();
							InputStream is = (InputStream) new URL(url).getContent();
							Bitmap bitmap = BitmapFactory.decodeStream(is);
							float aspectRatio = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
							Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (aspectRatio * 200), 200, true);
							spark.setBitmap(scaledBitmap);
							
							edited = true;
							return null;
						} catch (MalformedURLException e) {
							Tracker myTracker = EasyTracker.getTracker(); 
			                myTracker.sendException(e.getMessage(), false);
							e.printStackTrace();
						} catch (IOException e) {
							Tracker myTracker = EasyTracker.getTracker(); 
			                myTracker.sendException(e.getMessage(), false);
							e.printStackTrace();
						}
					}
				}
			} else if (jawn.getType() == 'I') {
				final String SPARKS = "sparks";
				final String ID = "id";
				final String CONTENT_TYPE = "content_type";
				final String SPARK_TYPE = "spark_type";
				final String CONTENT = "content";
				final String FILE = "file";
				
				idea = jawn.getSelfIdea();
				unloadedThumbs = new ArrayList<Integer>();
				ideaSparksJSON = new ArrayList<String>();
				
				String fullIdeaUrl = "http://steamnet.herokuapp.com/api/v1/ideas/"+idea.getId()+".json";
				try {
					JSONObject fullIdeaJSON = new JSONObject(get(new URL(fullIdeaUrl)));
					JSONArray sparkArray = fullIdeaJSON.getJSONArray(SPARKS);
					Spark[] sparks = new Spark[sparkArray.length()];
					for (int i = 0; i < sparkArray.length(); i++) {
						JSONObject sparkJSON = sparkArray.getJSONObject(i); 
						int sparkId = sparkJSON.getInt(ID);
						String content = sparkJSON.getString(CONTENT);
						char sparkType = sparkJSON.getString(SPARK_TYPE).charAt(0);
						char contentType = sparkJSON.getString(CONTENT_TYPE).charAt(0);
						Spark newSpark = new Spark(sparkId, sparkType, contentType, content);
						
						if (sparkJSON.has(FILE)) {
							String file = sparkJSON.getString(FILE);
							newSpark.setCloudLink(file);
						}
						
						sparks[i] = newSpark;
					}
					idea.setSparks(sparks);
					
					int index = 0;
					for (Spark spark : idea.getSparks()) {
						for (int i = 0; i < jAdapter.getJawns().length; i++) {
							Spark compareSpark = jAdapter.getJawns()[i].getSelfSpark(); 
							if (compareSpark != null) {
								if (compareSpark.getId() == spark.getId()) {
									if (compareSpark.getBitmap() != null) {
										spark.setBitmap(compareSpark.getBitmap());
										idea.setSpark(index, spark);
										edited = true;
										break;
									} else {
										savedIndex = i;
									}
								}
							}
						}
						if (spark.getBitmap() == null && spark.getContentType() != 'T' && spark.getContentType() != 'A') {
							if (spark.getCloudLink() != null) {
								try {
									String url = spark.getCloudLink();
									InputStream is = (InputStream) new URL(url).getContent();
									Bitmap bitmap = BitmapFactory.decodeStream(is);
									float aspectRatio = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
									Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (aspectRatio * 160), 160, true);
									spark.setBitmap(scaledBitmap);
									
									if (jAdapter.getJawns().length > 0) {
										idea.setSpark(index, spark);
										edited = true;
									}
									
									if (savedIndex != -1 && savedIndex < jAdapter.getJawns().length) {
										Spark savedSpark = (Spark) jAdapter.getJawns()[savedIndex];
										jAdapter.setJawn(savedIndex, savedSpark);
									}
								} catch (MalformedURLException e) {
									Tracker myTracker = EasyTracker.getTracker(); 
					                myTracker.sendException(e.getMessage(), false);
									e.printStackTrace();
								} catch (IOException e) {
									Tracker myTracker = EasyTracker.getTracker(); 
					                myTracker.sendException(e.getMessage(), false);
									e.printStackTrace();
								}
							}
						}
						index++;
					}
				} catch (MalformedURLException e1) {
					Tracker myTracker = EasyTracker.getTracker(); 
	                myTracker.sendException(e1.getMessage(), false);
					e1.printStackTrace();
				} catch (JSONException e1) {
					Tracker myTracker = EasyTracker.getTracker(); 
	                myTracker.sendException(e1.getMessage(), false);
					e1.printStackTrace();
				} catch (IOException e1) {
					Tracker myTracker = EasyTracker.getTracker(); 
	                myTracker.sendException(e1.getMessage(), false);
					e1.printStackTrace();
				}
			}
			return null;
		}
		
		protected void onPostExecute(InputStream is) {
			if (edited && spark != null) {
				mLoader.setJawn(position, spark);
			} else if (edited && idea != null) {
				mLoader.setJawn(position, idea);
			}
			//if (ideaSparksJSON != null && jawn.getType() == 'I') {
				//new IdeaThumbLoader(idea, position, unloadedThumbs, ideaSparksJSON, jAdapter, indexgrid, savedIndices);
			//}
			if (position + incriment < jAdapter.getJawns().length) {
				mLoader.loadMultimedia(position + incriment);
			}
		}	
		
		String get(URL url) throws IOException {
			HttpURLConnection connection = new OkHttpClient().open(url);
	        InputStream in = null;
	        try {
	        	// Read the response.
	        	in = connection.getInputStream();
	        	byte[] response = readFully(in);
	        	return new String(response, "UTF-8");
      		} finally {
      			if (in != null) in.close();
      		}
		}
	        
        byte[] readFully(InputStream in) throws IOException {
        	ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
            	out.write(buffer, 0, count);
            }
            return out.toByteArray();
        }
	}
	
	public void setJawn(int pos, Jawn jawn) {
		jAdapter.setJawn(pos, jawn);
		if (indexgrid != null) 
			indexgrid.setJawns(jAdapter.getJawns());
		
		jAdapter.notifyDataSetChanged();
	}
}
