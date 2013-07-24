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
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;

public class MultimediaLoader {
	IndexGrid indexgrid;
	JawnAdapter jAdapter;
	
	public MultimediaLoader(IndexGrid i, JawnAdapter j) {
		indexgrid = i;
		jAdapter = j;
		
		loadMultimedia(0);
	}
	
	public void loadMultimedia(int pos) {
		if (pos < jAdapter.getJawns().length) {
			new MultimediaHelper(pos, MultimediaLoader.this);
		}
	}
	
	public class MultimediaHelper extends AsyncTask<String, Void, InputStream> {
		MultimediaLoader mLoader;
		int position;
		Jawn jawn;
		Spark spark;
		Idea idea;
		Integer[] savedIndices = { null, null, null, null };
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
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else if (jawn.getType() == 'I') {
				final String SPARKS = "sparks";
				final String ID = "id";
				
				idea = jawn.getSelfIdea();
				unloadedThumbs = new ArrayList<Integer>();
				ideaSparksJSON = new ArrayList<String>();
				
				String fullIdeaUrl = "http://steamnet.herokuapp.com/api/v1/ideas/"+idea.getId()+".json";
				try {
					JSONObject fullIdeaJSON = new JSONObject(get(new URL(fullIdeaUrl)));
					JSONArray sparkArray = fullIdeaJSON.getJSONArray(SPARKS);
					int[] sparkIds = new int[sparkArray.length()];
					for (int i = 0; i < sparkArray.length(); i++) {
						JSONObject specificSpark = (JSONObject) sparkArray.get(i);
						sparkIds[i] = specificSpark.getInt(ID);
					}
					idea.setIds(sparkIds);
					idea.setBitmaps(new Bitmap[sparkIds.length]);
					
					int index = 0;
					for (int sparkId : idea.getIds()) {
						for (int i = 0; i < jAdapter.getJawns().length; i++) {
							Spark compareSpark = jAdapter.getJawns()[i].getSelfSpark(); 
							if (compareSpark != null) {
								if (compareSpark.getId() == sparkId) {
									if (compareSpark.getBitmap() != null) {
										idea.setBitmap(compareSpark.getBitmap(), index);
									} else {
										savedIndices[index] = i;
									}
									break;
								}
							}
						}
						index++;
					}
					
					index = 0;
					for (Bitmap bitmap : idea.getBitmaps()) {
						if (bitmap == null) {
							if (unloadedThumbs.size() < 4) {
								unloadedThumbs.add(index);
								String url = "http://steamnet.herokuapp.com/api/v1/sparks/"+idea.getIds()[index]+".json";
								try {
									ideaSparksJSON.add(get(new URL(url)));
								} catch (MalformedURLException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}	
							}
						}
						index++;
					}
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			return null;
		}
		
		protected void onPostExecute(InputStream is) {
			if (edited) {
				mLoader.setJawn(position, spark);
			}
			if (ideaSparksJSON != null && jawn.getType() == 'I') {
				new IdeaThumbLoader(idea, position, unloadedThumbs, ideaSparksJSON, jAdapter, indexgrid, savedIndices);
			}
			if (position + 1 < jAdapter.getJawns().length) {
				mLoader.loadMultimedia(position + 1);
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
	
	public void setJawn(int pos, Spark spark) {
		jAdapter.setJawn(pos, spark);
		indexgrid.setJawns(jAdapter.getJawns());
		
		jAdapter.notifyDataSetChanged();
	}
}
