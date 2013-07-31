package org.friendscentral.steamnet.Activities;

import org.friendscentral.steamnet.AuthHandler;
import org.friendscentral.steamnet.R;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AuthActivity extends Activity {
	final static int GET_AUTH_ACTIVITY_REQUEST_CODE = 4;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_form);
        
        new AuthHandler(AuthActivity.this);
	}
	
	public void revertAuth() {
		Log.v("AuthActivity", "Reverting to main");
		//finish(GET_AUTH_ACTIVITY_REQUEST_CODE);
		finish();
	}
	
	// Starting and stopping Google Analytics code:
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
}
