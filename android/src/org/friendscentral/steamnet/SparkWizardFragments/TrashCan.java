package org.friendscentral.steamnet.SparkWizardFragments;

import org.friendscentral.steamnet.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TrashCan extends Fragment {
	View trashCanView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		trashCanView = inflater.inflate(R.layout.trash_can_fragment, container, false);
		return trashCanView;
	}
	
	public View getTrashCanView() {
		return trashCanView;
	}
}
