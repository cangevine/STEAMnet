package org.friendscentral.steamnet.EventHandlers;

import org.friendscentral.steamnet.IdeaBucket;
import org.friendscentral.steamnet.IndexGrid;
import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.Activities.DetailActivity;

import BaseClasses.Spark;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

public class SparkEventHandler {
	static final String EXTRA_MESSAGE = null;
	Context context;
	LinearLayout mainLayout;
	IdeaBucket ideaBucket;
	GridView gridview;
	IndexGrid indexGrid;
	
	public SparkEventHandler(Context c, LinearLayout m, IdeaBucket b, GridView g, IndexGrid i) {
		context = c;
		mainLayout = m;
		ideaBucket = b;
		gridview = g;
		indexGrid = i;
		
		initializeIndexGridLayout();
	}
	
	public void openDetailView(Spark s) {
		/*Bundle b = new Bundle();
		b.putString("Content", s.getContent());
		b.putInt("Id", s.getId());
		b.putString("SparkType", s.getSparkTypeString());
		b.putString("ContentType", s.getContentTypeString());
		b.putString("CreatedAt", s.getCreatedAt());*/
    	Intent intent = new Intent(context, DetailActivity.class);
    	//intent.putExtra(EXTRA_MESSAGE, b);
    	intent.putExtra("spark", s);
    	context.startActivity(intent);
    }
    
    public void initializeIndexGridLayout() {
    	gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                openDetailView(indexGrid.getSparks()[position]);
            }
        });
    	
    	gridview.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> adapterView, View view,
					int pos, long id) {
				ClipData data = ClipData.newPlainText("", "");
		        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		        view.startDrag(data, shadowBuilder, view, 0);
		        
		        SparkDragListener sdl = new SparkDragListener();
		        sdl.setPos(pos);
		        sdl.setIndexGridContext(indexGrid);
		        
		        mainLayout.findViewById(R.id.Sidebar).setOnDragListener(sdl);
		        
				return true;
			}
    	});

    }
    
    private class SparkDragListener implements OnDragListener {
    	int pos;
    	IndexGrid indexGridContext;
    	
		@Override
		public boolean onDrag(View view, DragEvent dragEvent) {
			LinearLayout ib = (LinearLayout) mainLayout.findViewById(R.id.IdeaBucket);
			
			switch (dragEvent.getAction()) {
		      	case DragEvent.ACTION_DRAG_STARTED:
		      		//do nothing
		      		break;
		      	case DragEvent.ACTION_DRAG_ENTERED:
		      		ib.setBackgroundResource(R.drawable.idea_bucket_drop_background);
		      		break;
		      	case DragEvent.ACTION_DRAG_EXITED:
		      		ib.setBackgroundResource(0);
		      		break;
		      	case DragEvent.ACTION_DROP:
		      		ib.setBackgroundResource(0);
		      		
		      		/*
		      		 * Need to clear the other listeners so it can't be re-added to the bucket
		      		 * (currently fixed by making the dropzone of the bucket the entire sidebar)
		      		 */
		      		
		      		ideaBucket.addSpark(indexGridContext.getSparks()[pos]);
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
		
		public void setIndexGridContext(IndexGrid i) {
			indexGridContext = i;
		}
 
    }
}
