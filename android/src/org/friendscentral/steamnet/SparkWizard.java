package org.friendscentral.steamnet;

import org.friendscentral.steamnet.SparkWizardFragments.ContentEntry;
import org.friendscentral.steamnet.SparkWizardFragments.ContentTypeChooser;
import org.friendscentral.steamnet.SparkWizardFragments.SparkTypeChooser;

import APIHandlers.RetrieveDataTaskPostSpark;
import BaseClasses.Spark;
import android.app.ActionBar.LayoutParams;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.test.suitebuilder.annotation.Suppress;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
@SuppressWarnings("unused")
public class SparkWizard {
	LinearLayout mainLayout;
	FragmentManager fm;
	
	public SparkWizard(LinearLayout ml, FragmentManager f) {
		mainLayout = ml;
		fm = f;
	}
	
	//Methods for the Wizard Fragments:
			//Very similar, could probably be consolidated
	public void openContentTypeChooser(View v) {
		updateWeights(5, 3, 1);
		
		ContentTypeChooser ctc = new ContentTypeChooser();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.WizardSection, ctc);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public void revertWizard(View v) {
		updateWeights(1, 3, 1); 
		
		SparkTypeChooser stc = new SparkTypeChooser();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.WizardSection, stc);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public void openContentEntry(View v) {
		updateWeights(5, 3, 1);
		
		ContentEntry ce = new ContentEntry();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.WizardSection, ce);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public void submitSpark(View v, Spark s, GridView g, IndexGrid i) {
		RetrieveDataTaskPostSpark task = new RetrieveDataTaskPostSpark(s.getSparkType(), s.getContentType(), s.getContent(), g, i);
		
		revertWizard(v);
	}
	
	public void updateWeights(float sp, float fs, float ib) {
    	mainLayout.findViewById(R.id.WizardSection).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, sp));
    	mainLayout.findViewById(R.id.FilterSettings).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, fs));
    	mainLayout.findViewById(R.id.IdeaBucket).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, ib));
    }
	
	//Set up the auto-weighting for each Sidebar componant:
		//Needs updating, to include descendants...
	/*findViewById(R.id.WizardSection).setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			updateWeights(5, 3, 1);
			//Set things invisible
		}
	});
	
	findViewById(R.id.IdeaBucket).setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			updateWeights(1, 3, 5);
			//Set things invisible
		}
	});
	
	findViewById(R.id.FilterSettings).setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			updateWeights(1, 3, 1);
			//Set things invisible
		}
	});*/

}