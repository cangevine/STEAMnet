package org.friendscentral.steamnet.DetailViewFillers;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.content.Context;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.widget.LinearLayout;

public class CodeFiller extends DetailFiller {
	WebView gistHolder;

	public CodeFiller(Spark s,LinearLayout sparkData, Context c) {
		super(s, "Code", sparkData, c);
		
		gistHolder = (WebView) detailView.findViewById(R.id.code_data_webview);
		
		fillData();
	}

	@Override
	void fillData() {
		// To be removed:
		content = "https://gist.github.com/aphillips915/5987363";
		
		if (content.contains("gist.github.com")) {
			String url;
			if (content.contains(".js")) {
				url = "<script src='" + content + "'></script>";
			} else {
				url = "<script src='" + content + ".js'></script>";
			}
			String embed = "<html><head>"+url+"</head></html>";
			
			WebSettings webViewSettings = gistHolder.getSettings();
			webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
			webViewSettings.setJavaScriptEnabled(true);
			webViewSettings.setPluginsEnabled(true);
			webViewSettings.setBuiltInZoomControls(true);
			webViewSettings.setPluginState(PluginState.ON);
			
			gistHolder.loadDataWithBaseURL("https://gist.github.com/", embed, "text/html", "utf-8", null);	
		}
	}

}
