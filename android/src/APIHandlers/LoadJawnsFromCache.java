package APIHandlers;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.BaseClasses.Jawn;

import CachingHandlers.JawnsDataSource;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LoadJawnsFromCache {
	JawnsDataSource datasource;
	IndexGrid indexgrid;
	Context context;
	
	public LoadJawnsFromCache(JawnsDataSource d, IndexGrid ig, Context c) {
		datasource = d;
		indexgrid = ig;
		context = c;
		
		new CacheLoader().execute();
	}
	
	public class CacheLoader extends AsyncTask<Void, Void, String> {
		Jawn[] jawnsFromCache;

		@Override
		protected String doInBackground(Void... arg0) {
			Jawn[] cachedJawns = datasource.getAllJawns();
			
			Log.v("CacheLoader", "executed");
			
			//Cut cached Jawns down to just 50, if needed
			/*int jawnAmount = Math.min(cachedJawns.length, 50);
			if (jawnAmount == 50) {
				Jawn[] newJawns = new Jawn[50];
				for (int i = 0; i < 50; i++) {
					newJawns[i] = cachedJawns[i];
				}
				cachedJawns = newJawns;
			}*/
			
			jawnsFromCache = cachedJawns;
			return "";
		}
		
		protected void onPostExecute(String s) {
			Log.v("CacheLoader", "in post execute");
			indexgrid.setJawns(jawnsFromCache);
			new MultimediaLoader(indexgrid, indexgrid.getAdapter());
			new UserLoader(indexgrid, indexgrid.getAdapter());
		}
		
	}
}
