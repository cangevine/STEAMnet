package CachingHandlers;

import org.friendscentral.steamnet.BaseClasses.Jawn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class JawnOpenHelper extends SQLiteOpenHelper {
	Jawn[] jawns;
	
	//Database specific things:
	private static final String DATABASE_NAME = "jawns.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_JAWNS = "jawns";
	public static final String COLUMN_ID = "_id";
	
	//Fields that apply to both Sparks and Ideas:
	public static final String JAWN_TYPE = "jawn_type";
	public static final String CREATED_AT = "created_at";
	public static final String JAWN_ID = "jawn_id";
	//public static final String USERNAME = "firstUser"; /* Might be problematic, needs to be updated after loadUsers() */
	
	//Fields only applicable to Sparks:
	public static final String SPARK_TYPE = "spark_type";
	public static final String SPARK_CONTENT_TYPE = "content_type";
	public static final String CONTENT = "content";
	public static final String FILE = "file";
	
	//Fields only applicable to Ideas:
	public static final String DESCRIPTION = "description";
	
	//Database creation SQL statement:
	private static final String DATABASE_CREATE = "create table " + TABLE_JAWNS + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, " 
			+ JAWN_TYPE + " text not null, " 
			+ CREATED_AT + " text, "
			+ JAWN_ID + " integer, "
			+ SPARK_TYPE + " text, "
			+ SPARK_CONTENT_TYPE + " text, "
			+ CONTENT + " text, "
			+ FILE + " text, "
			+ DESCRIPTION + " text"
			+ ");";

	public JawnOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(JawnOpenHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_JAWNS);
		    onCreate(db);
	}

}
