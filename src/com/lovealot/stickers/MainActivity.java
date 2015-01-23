package com.lovealot.stickers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import lab.prada.collage.component.ComponentFactory;
import lab.prada.collage.component.PhotoView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;

import eu.janmuller.android.simplecropimage.CropImage;

public class MainActivity extends Activity {

	public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
	public static final int REQUEST_CODE_GALLERY = 0x1;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
	public static final int REQUEST_GALLERY = 0x04;

	int countSticker = 0;

	Uri uri;
	PhotoView iv;
	File mFileTemp;

	ViewGroup photoPanel;

	Bitmap rotatedBitmap;

	ImageView imageViewAtZero;
	AQuery aq;
	Uri mImageUri;
	Context context;

	View currentStickerTouch;

	ArrayList<TagImage> Listtag = new ArrayList<TagImage>();

	Button btnCamera;
	Button btnShare;
	Button btnClear;
	Button btnSave;
	Button btnGallery;
	ImageView btnAddPic;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		

		context = this;
		aq = new AQuery(this);
		imageViewAtZero = (ImageView) findViewById(R.id.imageView);
		photoPanel = (ViewGroup) aq.find(R.id.frame_images).getView();

		initImageView();

		btnCamera = (Button) findViewById(R.id.btnCamera);
		btnGallery = (Button) findViewById(R.id.btngallery);
		
		btnAddPic = (ImageView) findViewById(R.id.imageView1);
		btnShare = (Button) findViewById(R.id.btnOpenGallery);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnClear = (Button) findViewById(R.id.btnclear);

		btnGallery.setOnClickListener(

		new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				showAlert(view);

			}

		});

		btnCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				showAlert(view);
			}
		});

		btnAddPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pickSticker();

			}
		});
		
		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, GalleryPhoto.class);
				startActivity(i);

			}
		});

		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// photoPanel.findViewWithTag(sticker_1);
				// photoPanel.findViewWithTag(sticker_2);
				// photoPanel.removeView(iv);
				if (countSticker > 0) {
					photoPanel.removeViewAt(1);
					countSticker--;
				}

			}

		});

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Save();
				//Toast.makeText(context, ""+dir, Toast.LENGTH_SHORT).show();
//				Intent i = new Intent(getApplicationContext(),ResultSave.class);
//				i.putExtra("result", "");
//				startActivity(i);
			}
		});

		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {

			mFileTemp = new File(Environment.getExternalStorageDirectory(),

			TEMP_PHOTO_FILE_NAME);

		} else {

			mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);

		}

	}
	
	public void Save(){
		OutputStream output;
	
		View view = findViewById(R.id.frame_images);
		view.setDrawingCacheEnabled(true);
		Bitmap  bm = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		Date d = new Date();
        String filename  = (String)DateFormat.format("kkmmss-MMddyyyy"
                , d.getTime());
		File filepath = Environment.getExternalStorageDirectory();
		 
        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath()+ "/LoveaLot/");
        dir.mkdirs();

        // Create a name for the saved image
        File file = new File(dir,  filename +".png");

        // Show a toast message on successful save
        Toast.makeText(MainActivity.this, "Next",Toast.LENGTH_LONG).show();
        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bm.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
            updateImage(file);
            Uri uri = Uri.fromFile(file);
            //Toast.makeText(context, uri.toString(), Toast.LENGTH_SHORT).show();
           Intent i = new Intent(getApplicationContext(),ResultSave.class);
           i.putExtra("reuslt", uri);
           startActivity(i);
        }

        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}


	public void updateImage(File f) {
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		intent.setData(Uri.fromFile(f));
		sendBroadcast(intent);
	}

	private void initImageView() {

		//String paths = getIntent().getStringExtra("photo");
		//mImageUri = Uri.parse(paths);
		mImageUri = getIntent().getParcelableExtra("photo");
		imageViewAtZero.setImageURI(mImageUri);
	}

	public void clearImages() {
		((ViewGroup) aq.find(R.id.frame_images).getView()).removeAllViews();
	}
	
	
	

	public int getCameraPhotoOrientation(String imagePath) {

		int rotate = 0;

		try {

			File imageFile = new File(imagePath);

			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());

			int orientation = exif.getAttributeInt(

			ExifInterface.TAG_ORIENTATION,

			ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {

			case ExifInterface.ORIENTATION_ROTATE_270:

				rotate = 270;

				break;

			case ExifInterface.ORIENTATION_ROTATE_180:

				rotate = 180;

				break;

			case ExifInterface.ORIENTATION_ROTATE_90:

				rotate = 90;

				break;

			}

			Log.i("RotateImage", "Exif orientation: " + orientation);

			Log.i("RotateImage", "Rotate value: " + rotate);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return rotate;

	}

	public void pickSticker() {

		Intent intent = new Intent(this, MainStickersTap.class);

		startActivityForResult(intent, REQUEST_GALLERY);

	}

	private void takePicture() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {

			String state = Environment.getExternalStorageState();

			if (Environment.MEDIA_MOUNTED.equals(state)) {

				mImageCaptureUri = Uri.fromFile(mFileTemp);

				// orient

			} else {


				mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;

			}

			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,

			mImageCaptureUri);

			intent.putExtra("return-data", true);

			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);

		} catch (ActivityNotFoundException e) {

			// Log.d(TAG, "cannot take picture", e);

		}

	}

	private void openGallery() {

		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

		photoPickerIntent.setType("image/*");

		startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);

	}

	public void RotateBitmap(String source, float angle) {

		Matrix matrix = new Matrix();

		matrix.postRotate(angle);

		File imgFile = new File(source);

		Bitmap myBitmap;


		if (imgFile.exists()) {

			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

			// Drawable d = new BitmapDrawable(getResources(), myBitmap);

			Bitmap result = Bitmap.createBitmap(myBitmap, 0, 0,

			myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);

			File f = new File(context.getCacheDir(), source);

			try {

				f.createNewFile();

				// Convert bitmap to byte array

				rotatedBitmap = result;

				ByteArrayOutputStream bos = new ByteArrayOutputStream();

				rotatedBitmap.compress(CompressFormat.PNG,
						0 /* ignored for PNG */, bos);

				byte[] bitmapdata = bos.toByteArray();

				// write the bytes in file

				FileOutputStream fos = new FileOutputStream(f);

				fos.write(bitmapdata);

				fos.close();

			} catch (IOException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

		}

	}

	Uri mImageCaptureUri;

	private void startCropImage(String rotation) {

		Intent intent = new Intent(this, CropImage.class);

		// RotateBitmap(mFileTemp.getPath(),(float) Float.parseFloat(rotation));

		intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());

		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra("DEGREE", rotation);

		intent.putExtra(CropImage.ASPECT_X, 4);

		intent.putExtra(CropImage.ASPECT_Y, 4);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);

	}

	public static boolean test(String str, int num) {
		boolean res = true;
		return res;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {

			return;

		}

		switch (requestCode) {

		case REQUEST_CODE_GALLERY:

			try {

				InputStream inputStream = getContentResolver().openInputStream(

				data.getData());

				FileOutputStream fileOutputStream = new FileOutputStream(

				mFileTemp);

				copyStream(inputStream, fileOutputStream);

				fileOutputStream.close();

				inputStream.close();

				int rotation = getCameraPhotoOrientation(mFileTemp

				.getAbsolutePath());

				

				startCropImage(String.valueOf(rotation));

			} catch (Exception e) {

			}

			break;

		case REQUEST_CODE_TAKE_PICTURE:

			int rotation = getCameraPhotoOrientation(mFileTemp

			.getAbsolutePath());

			

			startCropImage(String.valueOf(rotation));

			break;

		case REQUEST_CODE_CROP_IMAGE:

			String path = data.getStringExtra(CropImage.IMAGE_PATH);
			Uri my = Uri.parse(path);
			imageViewAtZero.setImageURI(my);

			break;

		case REQUEST_GALLERY:

			try {

				String paths = data.getStringExtra("result");

				AssetManager mngr = getAssets();

				InputStream input;

				input = mngr.open(paths);
				
				Bitmap bm = BitmapFactory.decodeStream(input);
				
				if (paths.length() > 0) {

					// clearImages();

					int gapX = photoPanel.getWidth()

					/ Constants.SUPPORTED_FRAME_WIDTH;

					int gapY = photoPanel.getHeight()

					/ Constants.SUPPORTED_FRAME_HEIGHT;

					// int x, y;

					// ImageView iv;

					iv = ComponentFactory.create(
							ComponentFactory.COMPONENT_IMAGE, this, null);
					iv.refreshDrawableState();
					iv.setImageBitmap(bm);


					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							gapX, gapY);
					params.setMargins(0, 0, 0, 0);
					iv.setLayoutParams(params);
					iv.setTag("sticker_1");
					iv.setTag("sticker_2");
					photoPanel.addView(iv);
					countSticker++;

				}

				// image.setImageBitmap(bm);

			} catch (IOException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

			photoPanel.invalidate();

			break;

		}

		super.onActivityResult(requestCode, resultCode, data);

	}
	public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {

		Bitmap bm = null;
		
		try{
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
	    return bm;
	}
	
	public void showAlert(final View view) {
		new AlertDialog.Builder(this)
				.setTitle("ยกเลิกรูปที่แต่ง")
				.setMessage("คุณต้องการยกเลิกรูปที่แต่งอยู่หรือไม่?")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (view == btnCamera) {
									if (countSticker > 0)
										photoPanel.removeViews(1, countSticker);
									countSticker = 0;
									imageViewAtZero.setImageDrawable(null);
									takePicture();
								} else if (view == btnGallery) {
									if (countSticker > 0)
										photoPanel.removeViews(1, countSticker);
									countSticker = 0;
									imageViewAtZero.setImageDrawable(null);
									openGallery();
								} else {

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

	public static void copyStream(InputStream input, OutputStream output)

	throws IOException {

		byte[] buffer = new byte[1024];

		int bytesRead;

		while ((bytesRead = input.read(buffer)) != -1) {

			output.write(buffer, 0, bytesRead);

		}

	}
	public int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);	
			} else {
				inSampleSize = Math.round((float)width / (float)reqWidth);	
			}	
		}
		return inSampleSize;	
	}

}