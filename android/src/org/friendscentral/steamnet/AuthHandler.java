package org.friendscentral.steamnet;

import org.friendscentral.steamnet.Activities.AuthActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class AuthHandler {
	ProgressDialog dialog;
	AuthActivity ma;
	LinearLayout mainForm;
	WebView authForm;
	
	public AuthHandler(AuthActivity a) {
		ma = a;
		
		authForm = (WebView) ma.findViewById(R.id.Auth_webview);
		
		CustomWebViewClient c = new CustomWebViewClient();
		authForm.setWebViewClient(c);
		authForm.addJavascriptInterface(new JavaScriptInterface(), "HTMLOUT"); 
		
		WebSettings webViewSettings = authForm.getSettings();
		webViewSettings.setJavaScriptEnabled(true);
		
		authForm.loadUrl("http://steamnet.herokuapp.com/auth");
	}
	
	public void checkIfLoggedIn(String url) {
		Log.v("AuthHandler", url);
		if (url.contains("callback")) {
			//authForm.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');"); 
			authForm.loadUrl("javascript:window.HTMLOUT.setUsername(document.getElementById('user-name').innerHTML);");
			authForm.loadUrl("javascript:window.HTMLOUT.setToken(document.getElementById('token').innerHTML);");
			authForm.loadUrl("javascript:window.HTMLOUT.setUserId(document.getElementById('user-id').innerHTML);");
		}
	}
	
	private class JavaScriptInterface {
		String username;
		String token;
		String userId;
		STEAMnetApplication sna;
		
		public JavaScriptInterface() {
			sna = (STEAMnetApplication) ma.getApplication();
		}
		
	    @SuppressWarnings("unused")  
	    public void showHTML(String html) {  
	    	Context context = (Context) ma;
	        new AlertDialog.Builder(context)  
	            .setTitle("HTML")  
	            .setMessage(html)  
	            .setPositiveButton(android.R.string.ok, null)  
	        .setCancelable(false)  
	        .create()  
	        .show();  
	    }
	    
	    @SuppressWarnings("unused")
		public void setUsername(String html) {
	    	username = html;
	    	sna.setUsername(username);
	    	Log.v("Auth - new username:", username);
	    	checkIfFinished();
	    }
	    
	    @SuppressWarnings("unused")
		public void setToken(String html) {
	    	token = html;
	    	sna.setToken(token);
	    	Log.v("Auth - new token", token);
	    	checkIfFinished();
	    }
	    
	    @SuppressWarnings("unused")
		public void setUserId(String html) {
	    	userId = html;
	    	sna.setUserId(userId);
	    	Log.v("Auth - new userId", userId);
	    	checkIfFinished();
	    }
	    
	    public void checkIfFinished() {
	    	if (sna.getUsername() != null && sna.getToken() != null && sna.getUserId() != null) {
	    		Log.v("AuthHandler", "reverting");
	    		sna.setReadOnlyMode(false);
	    		ma.revertAuth();
	    	}
	    }
	} 
	
	private class CustomWebViewClient extends WebViewClient {
		@Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
		
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			dialog = new ProgressDialog(ma);
			dialog.setMessage("Communicating with authorization portal...");
			dialog.show();
		}
		
		public void onPageFinished(WebView view, String url) {
			dialog.dismiss();
			if (url.contains("callback?"))
				checkIfLoggedIn(url);
		}
	}
}
