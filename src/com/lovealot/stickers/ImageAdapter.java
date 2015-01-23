package com.lovealot.stickers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private List<String> lis;

	public ImageAdapter(Context c, List<String> li) {
		// TODO Auto-generated method stub
		context = c;
		lis = li;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return lis.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.imageitem, null);
		}

		String strPath = lis.get(position).toString();


		// Image Resource
		ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
		//Bitmap bm = BitmapFactory.decodeFile(strPath);
		AssetManager mngr = context.getAssets();
		InputStream input;
		try {
			input = mngr.open(strPath);
			//Bitmap bm = BitmapFactory.decodeStream(input);
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
			
			imageView.setImageBitmap(bitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;

	}
}