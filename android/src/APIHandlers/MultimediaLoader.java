package APIHandlers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

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
		boolean edited = false;
		
		public MultimediaHelper(int n, MultimediaLoader m) {
			mLoader = m;
			position = n;
			jawn = jAdapter.getJawns()[n];
			this.execute();
		}
		
		@Override
		protected InputStream doInBackground(String... args) {
			if (jawn.getType() == 'S' && !jAdapter.getLoadedList().get(position)) {
				spark = jawn.getSelfSpark();
				if (spark.getContentType() != 'T' && spark.getContentType() != 'A') {
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
			}
			return null;
		}
		
		protected void onPostExecute(InputStream is) {
			if (edited) {
				mLoader.setJawn(position, spark);
			}
			if (position + 1 < jAdapter.getJawns().length) {
				mLoader.loadMultimedia(position + 1);
			}
		}	
	}
	
	public void setJawn(int pos, Spark spark) {
		jAdapter.setJawn(pos, spark);
		indexgrid.setJawns(jAdapter.getJawns());
		
		jAdapter.notifyDataSetChanged();
	}
}
