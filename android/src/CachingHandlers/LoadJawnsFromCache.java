package CachingHandlers;

import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.JawnAdapter;
import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Jawn;

import APIHandlers.MultimediaLoader;
import APIHandlers.RefreshXJawns;
import APIHandlers.UserLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LoadJawnsFromCache {
	JawnsDataSource datasource;
	IndexGrid indexgrid;
	Context context;
	MainActivity mainActivity;
	
	public LoadJawnsFromCache(JawnsDataSource d, IndexGrid ig, Context c) {
		datasource = d;
		indexgrid = ig;
		context = c;
		mainActivity = (MainActivity) context;
		
		new CacheLoader().execute();
	}
	
	public class CacheLoader extends AsyncTask<Void, Void, String> {
		Jawn[] jawnsFromCache;

		@Override
		protected String doInBackground(Void... arg0) {
			Jawn[] cachedJawns = datasource.getAllJawns();
			
			//Cut cached Jawns down to just 50, if needed
			int jawnAmount = Math.min(cachedJawns.length, 50);
			if (jawnAmount == 50) {
				Jawn[] newJawns = new Jawn[50];
				for (int i = 0; i < 50; i++) {
					newJawns[i] = cachedJawns[i];
				}
				cachedJawns = newJawns;
			}
			
			jawnsFromCache = cachedJawns;
			return "";
		}
		
		protected void onPostExecute(String s) {
			Log.v("CacheLoader", "in post execute");
			
			indexgrid.setAdapter(new JawnAdapter(context, jawnsFromCache, 200));
			indexgrid.setJawns(indexgrid.getAdapter().getJawns());
			if (mainActivity != null) {
				mainActivity.setSparkEventHandlers();
				mainActivity.setScrollListener();
			}
			new MultimediaLoader(indexgrid, indexgrid.getAdapter());
			new UserLoader(indexgrid, indexgrid.getAdapter());
			
			// Lazy hardcoded "true" for Sparks and Ideas
			new RefreshXJawns(50, indexgrid.getGridView(), indexgrid, context, true, true);
		}
		
	}
}
