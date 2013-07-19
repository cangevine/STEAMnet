package org.friendscentral.steamnet.SparkSubmitters;

import org.friendscentral.steamnet.Activities.MainActivity;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.view.View;

public abstract class SparkSubmitter {
	MainActivity mainActivity;
	
	public SparkSubmitter(MainActivity m) {
		mainActivity = m;
	}
	
	public abstract Spark getNewSpark(char sparkType);
}
