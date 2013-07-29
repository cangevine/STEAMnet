package org.friendscentral.steamnet.EventHandlers;

import org.friendscentral.steamnet.IdeaBucket;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.Activities.MainActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class IdeaBucketEventHandler {
	Context context;
	IdeaBucket ideaBucket;
	LinearLayout mainLayout;
	FragmentManager fm;
	
	public IdeaBucketEventHandler(Context c, IdeaBucket b, LinearLayout m, FragmentManager f) {
		context = c;
		ideaBucket = b;
		mainLayout = m;
		fm = f;
		
		for (int i = 0; i < ideaBucket.getImageViews().length; i++) {
        	IdeaBucketSparkListener newSparkListener = new IdeaBucketSparkListener();
        	newSparkListener.setPos(i);
        	
        	View v = ideaBucket.getImageViews()[i];
        	v.setOnLongClickListener(null);
        	v.setOnTouchListener(null);
        	
        	v.setOnLongClickListener(newSparkListener);
        }
	}
    
    private class IdeaBucketSparkListener implements OnLongClickListener  {
    	int pos = -1;
    	
    	@Override
    	public boolean onLongClick(View view) {
    		
    		ClipData data = ClipData.newPlainText("", "");
	        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	        view.startDrag(data, shadowBuilder, view, 0);
	        
	        //TrashCan tc = new TrashCan();
	        //updateFragment(tc, R.id.WizardSection);
	        //View trashCanView = tc.getTrashCanView();
	        
    		TrashCanListener tcl = new TrashCanListener();
	        tcl.setPos(pos);
	        
	        /*
	         * Eventually set to only the trashcan:
	         */
	        //mainLayout.findViewById(R.id.Sidebar).setOnDragListener(tcl);
	        ((MainActivity) context).findViewById(R.id.MainLayout).setOnDragListener(tcl);
	        //trashCanView.setOnDragListener(tcl);
    		
    		return false;
    	}
    	
    	/*
    	 * Needs to be called after this Listener is initiated by before it is attached
    	 */
    	public void setPos(int p) {
    		pos = p;
    	}

    }
    
    public void killDragListener() {
    	((MainActivity) context).findViewById(R.id.MainLayout).setOnDragListener(null);
    }
    
    private class TrashCanListener implements OnDragListener {
    	int pos;
    	
    	public TrashCanListener() {
    		Drawable overlay =  ((MainActivity) context).getResources().getDrawable(R.drawable.trash_can_overlay);
    		overlay.setAlpha(50);
    		 ((FrameLayout) ((MainActivity) context).findViewById(R.id.IndexGridFrame)).setForeground(overlay);
    	}
    	
		@Override
		public boolean onDrag(View view, DragEvent dragEvent) {
			//TextView tv = (TextView) findViewById(R.id.trashCanTextView);
			
			switch (dragEvent.getAction()) {
		      	case DragEvent.ACTION_DRAG_STARTED:
		      		break;
		      	case DragEvent.ACTION_DRAG_ENTERED:
		      		//tv.setTextColor(Color.WHITE);
		      		break;
		      	case DragEvent.ACTION_DRAG_EXITED:
		      		//tv.setTextColor(Color.BLACK);
		      		break;
		      	case DragEvent.ACTION_DROP:
	      			ideaBucket.removeSpark(pos);
	      			((FrameLayout) ((MainActivity) context).findViewById(R.id.IndexGridFrame)).setForeground(null);
	      			//SparkTypeChooser stc = new SparkTypeChooser();
	      			//updateFragment(stc, R.id.WizardSection);
	      			
	      			killDragListener();
		    		
		    		break;
		      	case DragEvent.ACTION_DRAG_ENDED:
		      		//nothing
		      	default:
		      		break;
		      }
			return false;
		}
		
		public void setPos(int n) {
			pos = n;
		}
 
    }
    
    public void updateFragment(Fragment f, int viewId) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(viewId, f);
		ft.addToBackStack(null);
		ft.commit();
	}
    
}