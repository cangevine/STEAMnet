package org.friendscentral.steamnet;

import android.app.Application;

public class STEAMnetApplication extends Application {
	private String userToken;
	private String username;
	private String userId;
	
	@Override
	public void onCreate() {
		userToken = null;
		username = null;
		userId = null;
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
}
