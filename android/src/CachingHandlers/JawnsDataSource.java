package CachingHandlers;

import java.util.ArrayList;
import java.util.List;

import org.friendscentral.steamnet.BaseClasses.Idea;
import org.friendscentral.steamnet.BaseClasses.Jawn;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class JawnsDataSource {
	//Database fields
	private SQLiteDatabase database;
	private JawnOpenHelper dbHelper;
	private String[] allColumns = { JawnOpenHelper.COLUMN_ID,
		JawnOpenHelper.JAWN_TYPE, JawnOpenHelper.CREATED_AT, JawnOpenHelper.SPARK_ID, JawnOpenHelper.SPARK_TYPE, 
		JawnOpenHelper.SPARK_CONTENT_TYPE, JawnOpenHelper.CONTENT, JawnOpenHelper.FILE, JawnOpenHelper.IDEA_ID, JawnOpenHelper.DESCRIPTION };
	
	public JawnsDataSource(Context context) {
		dbHelper = new JawnOpenHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void convertJawnArrayToDb(Jawn[] jawns) {
		for (Jawn j : jawns) {
			addJawnToDb(j);
		}
	}
	
	public void deleteAllJawnsInDb() {
		String delete = "DELETE FROM "+JawnOpenHelper.TABLE_JAWNS;
	    database.rawQuery(delete, null);
	}
	
	public void addJawnToDb(Jawn jawn) {
		ContentValues values = new ContentValues();
	    values.put(JawnOpenHelper.JAWN_TYPE, String.valueOf(jawn.getType()));
	    if (jawn.getType() == 'S') {
	    	Spark spark = jawn.getSelfSpark();
	    	values.put(JawnOpenHelper.CREATED_AT, spark.getCreatedAt());
	    	values.put(JawnOpenHelper.SPARK_ID, String.valueOf(spark.getId()));
	    	values.put(JawnOpenHelper.SPARK_TYPE, String.valueOf(spark.getSparkType()));
	    	values.put(JawnOpenHelper.SPARK_CONTENT_TYPE, String.valueOf(spark.getContentType()));
	    	values.put(JawnOpenHelper.CONTENT, spark.getContent());
	    	if (spark.getContentType() != 'T') 
	    		values.put(JawnOpenHelper.FILE, spark.getCloudLink());
	    	else
	    		values.put(JawnOpenHelper.FILE, "");
	    	
	    	values.put(JawnOpenHelper.IDEA_ID, "");
	    	values.put(JawnOpenHelper.DESCRIPTION, "");
	    } else if (jawn.getType() == 'I') {
	    	Idea idea = jawn.getSelfIdea();
	    	values.put(JawnOpenHelper.CREATED_AT, "");
	    	values.put(JawnOpenHelper.SPARK_ID, "");
	    	values.put(JawnOpenHelper.SPARK_TYPE, "");
	    	values.put(JawnOpenHelper.SPARK_CONTENT_TYPE, "");
	    	values.put(JawnOpenHelper.CONTENT, "");
	    	values.put(JawnOpenHelper.FILE, "");
	    	
	    	values.put(JawnOpenHelper.IDEA_ID, String.valueOf(idea.getId()));
	    	values.put(JawnOpenHelper.DESCRIPTION, idea.getDescription());
	    }
	    database.insert(JawnOpenHelper.TABLE_JAWNS, null, values);
	}
	
	public Jawn[] getAllJawns() {
	    List<Jawn> jawns = new ArrayList<Jawn>();

	    Cursor cursor = database.query(JawnOpenHelper.TABLE_JAWNS,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Jawn jawn = cursorToJawn(cursor);
	      jawns.add(jawn);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    
	    Jawn[] jawnsArray = new Jawn[jawns.size()];
	    for (int i = 0; i < jawns.size(); i++) {
	    	jawnsArray[i] = jawns.get(i);
	    }
	    return jawnsArray;
	}
	
	public Jawn cursorToJawn(Cursor cursor) {
		char jawnType = cursor.getString(1).charAt(0);
		if (jawnType == 'S') {
			Spark newSpark = new Spark(Integer.parseInt(cursor.getString(3)), cursor.getString(4).charAt(0), cursor.getString(5).charAt(0), cursor.getString(6), cursor.getString(2));
		    if (newSpark.getContentType() != 'T') {
		    	if (!cursor.getString(7).equals("")) {
    	    		newSpark.setCloudLink(cursor.getString(7));
		    	}
		    }
		    return newSpark;
		} else if (jawnType == 'I') {
			Idea newIdea = new Idea(Integer.parseInt(cursor.getString(8)), cursor.getString(9), cursor.getString(2));
			return newIdea;
		}
		return null;
	}
}
