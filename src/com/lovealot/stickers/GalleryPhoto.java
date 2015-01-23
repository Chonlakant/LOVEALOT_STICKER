package com.lovealot.stickers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryPhoto extends Activity {
	List<String> imageList;
	ImageAdapter1 adapter;
	GridView grid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Select Picture to Share");
		setContentView(R.layout.gridviewdemo);
		grid = (GridView) findViewById(R.id.gridView1);
		imageList = getSD();
		getSD();
		adapter = new ImageAdapter1(this, imageList);
		grid.setAdapter(adapter);

		grid.setOnItemLongClickListener(new OnItemLongClickListener() { 
			 
		    @Override 
		    public boolean onItemLongClick(AdapterView<?> arg0, View v,
		            int position, long arg3) {
		    	final Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
				 vibrator.vibrate(100);
				 showAlert(position);
				 
		        return true; 
		    } 
		}); 
		
		grid.setOnItemClickListener(new OnItemClickListener() {
			
			

			@SuppressLint("ShowToast")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

	
				Bitmap bitmap;
				OutputStream output;
				Intent share;

				// Decoding the image
				bitmap = BitmapFactory.decodeFile(imageList.get(position));

				// Find the SD Card path
				File filepath = Environment.getExternalStorageDirectory();

				// Create a new folder AndroidBegin in SD Card
				File dir = new File(filepath.getAbsolutePath()+ "/LoveaLot/");
				dir.mkdirs();

				// Create a name for the saved image
				File file = new File(dir, "sample_wallpaper.png");

				try {

					// Start Share Intent
					share = new Intent(getApplicationContext(),ResultSave.class);

					// Type of file to share
					share.setType("image/jpeg");

					// Saves the file into SD Card
					output = new FileOutputStream(file);

					bitmap.compress(Bitmap.CompressFormat.PNG, 50, output);
					output.flush();
					output.close();

					// Locate the image to Share
					Uri uri = Uri.fromFile(file);

					// Captures the share image
					share.putExtra("reuslt", uri);

					// Start the share dialog
					startActivity(share);
				}

				// Catch exceptions
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	private List<String> getSD() {
		List<String> it = new ArrayList<String>();
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/LoveaLot";

		File f = new File(path);
		f.exists();
		Log.d("Check", "" + f.exists());
		File[] files = f.listFiles();
		Log.d("Check", "" + files.length);

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			Log.d("Count", file.toString());
			// Log.d("Count",file.getPath());
			it.add(file.getPath());
		}
		return it;
	}

	public class ImageAdapter1 extends BaseAdapter {
		private Context context;
		private List<String> lis;

		public ImageAdapter1(Context c, List<String> li) {
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

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.row_grid, null);
			}
			String strPath = lis.get(position).toString();
			// Image Resource
			ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

			Bitmap bm = BitmapFactory.decodeFile(strPath);
			imageView.setImageBitmap(bm);
			
			
			
			


			return convertView;

		}
	}
	
	public void showAlert(final int pos) {
		final String selectedFilePath = imageList.get(pos).toString();
		new AlertDialog.Builder(this)
				.setTitle("Delete picture")
				.setMessage("Do you want to remove this picture. ? \n" + selectedFilePath)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								
								File file = new File(selectedFilePath);
								boolean deleted = file.delete();
								if(deleted) {
									Toast.makeText(getApplicationContext(), "Delete complete !", Toast.LENGTH_LONG).show();
									adapter.notifyDataSetChanged();
									grid.invalidateViews();
								}
									
								else {
									Toast.makeText(getApplicationContext(), "Opz ! Can't delete this picture. " + selectedFilePath , Toast.LENGTH_LONG).show();
								}
									
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();

	}

}
