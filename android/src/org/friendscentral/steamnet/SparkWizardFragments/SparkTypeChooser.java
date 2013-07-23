package org.friendscentral.steamnet.SparkWizardFragments;

import org.friendscentral.steamnet.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SparkTypeChooser extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.spark_type_chooser, container, false);
		return v;
	}
}
