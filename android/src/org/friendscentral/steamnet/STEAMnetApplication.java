package org.friendscentral.steamnet;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

@SuppressWarnings("rawtypes")
public class STEAMnetApplication extends Application {
	private String userToken;
	private String username;
	private String userId;
	boolean readOnlyMode;
	AsyncTask currentTask;
	AsyncTask mCurrentTask;
	AsyncTask uCurrentTask;
	double seedNum = 2.0;
	String savedTag;
	String[] savedTags;
	
	@Override
	public void onCreate() {
		userToken = readFromFile("token");
		username = readFromFile("username");
		userId = readFromFile("userId");
		if (userToken != null && userId != null && username != null)
			readOnlyMode = false;
		else
			readOnlyMode = true;
		super.onCreate();
	}
	
	public String readFromFile(String param) {
		try {
			InputStream is = openFileInput(param);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(b)) != -1) {
				bos.write(b, 0, bytesRead);
			}
			byte[] bytes = bos.toByteArray();
			String byteString = new String(bytes);
			if (!byteString.equals("")) {
				return byteString;
			}
		} catch (FileNotFoundException e) {
			Log.v("STEAMnetApplication error:", "No persistant variable for "+param+". Initializing with ''");
			try {
				FileOutputStream fos = openFileOutput(param, Context.MODE_PRIVATE);
				fos.write("".getBytes());
				fos.close();
			} catch (FileNotFoundException es) {
				e.printStackTrace();
			} catch (IOException es) {
				e.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setToken(String s) {
		userToken = s;
		try {
			FileOutputStream fos = openFileOutput("token", Context.MODE_PRIVATE);
			fos.write(userToken.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getToken() {
		return userToken;
	}
	
	public void setUsername(String s) {
		username = s;
		try {
			FileOutputStream fos = openFileOutput("username", Context.MODE_PRIVATE);
			fos.write(username.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUserId(String s) {
		userId = s;
		try {
			FileOutputStream fos = openFileOutput("userId", Context.MODE_PRIVATE);
			fos.write(userId.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setReadOnlyMode(boolean b) {
		readOnlyMode = b;
	}
	
	public boolean getReadOnlyMode() {
		return readOnlyMode;
	}
	
	public void logOut() {
		readOnlyMode = true;
		setUserId("");
		setUsername("");
		setToken("");
	}
	
	public void setSeedNum(double d) {
		seedNum = d;
	}
	
	public double getSeedNum() {
		return seedNum;
	}
	
	public void setCurrentTask(AsyncTask a) {
		currentTask = a;
	}
	public AsyncTask getCurrentTask() {
		return currentTask;
	}
	
	public void setCurrentMultimediaTask(AsyncTask a) {
		mCurrentTask = a;
	}
	public AsyncTask getCurrentMultimediaTask() {
		return mCurrentTask;
	}
	
	public void setCurrentUserTask(AsyncTask a) {
		uCurrentTask = a;
	}
	public AsyncTask getCurrentUserTask() {
		return uCurrentTask;
	}
	
	public String getSavedTag() {
		return savedTag;
	}
	public void setSavedTag(String s) {
		savedTag = s;
	}
	
	public String[] getSavedTags() {
		return savedTags;
	}
	public void setSavedTags(String[] s) {
		savedTags = s;
	}
}
