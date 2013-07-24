package APIHandlers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class IdeaThumbLoader {
	Idea idea;
	int position;
	int[] unloadedThumbs;
	String[] ideaSparksJSON;
	JawnAdapter jAdapter;
	IndexGrid indexgrid;
	Integer[] savedIndices;

	public IdeaThumbLoader(Idea i, int pos, ArrayList<Integer> ult, ArrayList<String> isj, JawnAdapter ja, IndexGrid ig, Integer[] si) {
		idea = i;
		position = pos;
		
		//Parallel arrays:
		unloadedThumbs = new int[ult.size()];
		ideaSparksJSON = new String[isj.size()];
		int index = 0;
		for (int thumb : ult) {
			unloadedThumbs[index] = ult.get(index);
			ideaSparksJSON[index] = isj.get(index);
			index++;
		}
		
		jAdapter = ja;
		indexgrid = ig;
		savedIndices = si;
		
		new IdeaMultimediaHelper(IdeaThumbLoader.this);
	}
	
	class IdeaMultimediaHelper extends AsyncTask<String, Void, String> {
		IdeaThumbLoader iLoader;
		ArrayList<Spark> editedSparks = new ArrayList<Spark>();
		ArrayList<Integer> editedSparksIndices = new ArrayList<Integer>();
		
		public IdeaMultimediaHelper(IdeaThumbLoader itl) {
			iLoader = itl;
			this.execute("");
		}
	
		@Override
		protected String doInBackground(String... args) {
			int index = 0;
			for (int unloadedIndex : unloadedThumbs) {
				String jsonString = ideaSparksJSON[index];
				
				final String FILE = "file";
				final String CONTENT_TYPE = "content_type";
				
				try {
					JSONObject j = new JSONObject(jsonString);
					if (!j.getString(CONTENT_TYPE).equals("T") && !j.getString(CONTENT_TYPE).equals("A")) {
						if (j.has(FILE)) {
							String url = j.getString(FILE);
							try {
								InputStream is = (InputStream) new URL(url).getContent();
								Bitmap bitmap = BitmapFactory.decodeStream(is);
								float aspectRatio = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
								Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (aspectRatio * 200), 200, true);
								idea.setBitmap(scaledBitmap, unloadedIndex);
								
								if (savedIndices[unloadedIndex] != null) {
									Log.v("Spark index that corresponds to Idea:", String.valueOf(savedIndices[unloadedIndex]));
									Spark spark = (Spark) jAdapter.getJawns()[savedIndices[unloadedIndex]];
									spark.setBitmap(scaledBitmap);
									editedSparks.add(spark);
									editedSparksIndices.add(savedIndices[unloadedIndex]);
								}
							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else if (j.getString(CONTENT_TYPE).equals("T")) {
						Bitmap bitmapFromRsrc = BitmapFactory.decodeResource(indexgrid.getContext().getResources(), R.drawable.btn_blue_text);
						if (bitmapFromRsrc != null)
							idea.setBitmap(bitmapFromRsrc, unloadedIndex);
					} else if (j.getString(CONTENT_TYPE).equals("A")) {
						Bitmap bitmapFromRsrc = BitmapFactory.decodeResource(indexgrid.getContext().getResources(), R.drawable.btn_blue_audio);
						if (bitmapFromRsrc != null) 
							idea.setBitmap(bitmapFromRsrc, unloadedIndex);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				index++;
			}
			
			return null;
		}
		
		protected void onPostExecute(String s) {
			iLoader.setJawn(position, idea);
			int i = 0;
			for (Spark spark : editedSparks) {
				int index = editedSparksIndices.get(i);
				iLoader.setJawn(index, spark);
				i++;
			}
		}
		
	}
	
	public void setJawn(int pos, Jawn jawn) {
		jAdapter.setJawn(pos, jawn);
		indexgrid.setJawns(jAdapter.getJawns());
		
		jAdapter.notifyDataSetChanged();
	}

}
