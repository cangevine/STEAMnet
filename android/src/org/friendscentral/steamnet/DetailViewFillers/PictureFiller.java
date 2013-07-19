package org.friendscentral.steamnet.DetailViewFillers;

import org.friendscentral.steamnet.R;
import org.friendscentral.steamnet.BaseClasses.Spark;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PictureFiller extends DetailFiller {
	ImageView pictureView;

	public PictureFiller(Spark s, LinearLayout sparkData, Context c) {
		super(s, "Picture", sparkData, c);
		
		pictureView = (ImageView) detailView.findViewById(R.id.picture_data_imageview);
		
		fillData();
	}

	@Override
	void fillData() {
		spark = new Spark();
		spark.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.symbol_video));
		
		// TODO definitely need to scale this image and apply formatting:
		
		pictureView.setImageBitmap(spark.getBitmap());
	}

}
