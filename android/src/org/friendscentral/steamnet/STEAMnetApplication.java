package org.friendscentral.steamnet;

import android.app.Application;

public class STEAMnetApplication extends Application {
	private String userToken;
	private String username;
	private String userId;
	boolean readOnlyMode;
	
	@Override
	public void onCreate() {
		// TODO eventually make this persistant
		
		userToken = null;
		username = null;
		userId = null;
		readOnlyMode = true;
		super.onCreate();
	}
	
	public void setToken(String s) {
		userToken = s;
	}
	
	public String getToken() {
		return userToken;
	}
	
	public void setUsername(String s) {
		username = s;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUserId(String s) {
		userId = s;
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
}
