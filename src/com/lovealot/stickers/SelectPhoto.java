package com.lovealot.stickers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

import eu.janmuller.android.simplecropimage.CropImage;

public class SelectPhoto extends Activity {

    public static final String TAG = "MainActivity";

    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    
    public static final int REQUEST_CODE_GALLERY      = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;

    private ImageView mImageView;
    private File      mFileTemp;
    Context context;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	 requestWindowFeature(Window.FEATURE_NO_TITLE);
    	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.activity_first);
//        File directory = new File(Environment.getExternalStorageDirectory() + "/LoveaLot");
//		if(!directory.exists() && !directory.isDirectory()) 
//	    {
//	        // create empty directory
//	        if (directory.mkdirs())
//	        {
//	            Log.i("CreateDir","App dir created");
//	        }
//	        else
//	        {
//	            Log.w("CreateDir","Unable to create app dir!");
//	        }
//	    }
//	    else
//	    {
//	        Log.i("CreateDir","App dir already exists");
//	    }
        findViewById(R.id.btnOpenGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });
        
        YoYo.with(Techniques.FadeInUp)
        .duration(2100).withListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				 YoYo.with(Techniques.Tada)
			        .duration(2100)
			        .playOn(findViewById(R.id.logo_image));
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		})
        .playOn(findViewById(R.id.give_image));
        
       
        
        findViewById(R.id.logo_image).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 YoYo.with(Techniques.Tada)
			        .duration(1400)
			        .playOn(findViewById(R.id.logo_image));
				
			}
		});
        
        context = this;

        findViewById(R.id.btnOpenCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePicture();
            }
        });

        mImageView = (ImageView) findViewById(R.id.image);
        
    	String state = Environment.getExternalStorageState();
    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    		mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
    	}
    	else {
    		mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
    	}

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
    
    public void RotateBitmap(String source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		
		File imgFile = new  File(source);
		Bitmap myBitmap;
		
		if(imgFile.exists()){
			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    //Drawable d = new BitmapDrawable(getResources(), myBitmap); 
			Bitmap result = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(),
					myBitmap.getHeight(), matrix, true);
			
			File f = new File(context.getCacheDir(), source);
			try {
				f.createNewFile();
				//Convert bitmap to byte array 
				Bitmap bitmap = result;
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
				byte[] bitmapdata = bos.toByteArray();
				 
				//write the bytes in file 
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
    
    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
        	
        	String state = Environment.getExternalStorageState();
        	if (Environment.MEDIA_MOUNTED.equals(state)) {
        		mImageCaptureUri = Uri.fromFile(mFileTemp);
        		// orient
        		
        	}
        	else {
	        	/*
	        	 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
	        	 */
	        	mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
        	}	
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

            Log.d(TAG, "cannot take picture", e);
        }
    }

    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    private void startCropImage(String rotation) {

    	
        Intent intent = new Intent(this, CropImage.class);
        
        
        //RotateBitmap(mFileTemp.getPath(),(float) Float.parseFloat(rotation));
        
        
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra("DEGREE", rotation);

        intent.putExtra(CropImage.ASPECT_X, 4);
        intent.putExtra(CropImage.ASPECT_Y, 4);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {

            return;
        }

        switch (requestCode) {

            case REQUEST_CODE_GALLERY:

                try {

                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    
                    

                    int rotation = getCameraPhotoOrientation(mFileTemp.getAbsolutePath());
                  
                    startCropImage(String.valueOf(rotation));

                } catch (Exception e) {

                    Log.e(TAG, "Error while creating temp file", e);
                }

                break;
            case REQUEST_CODE_TAKE_PICTURE:
            	int rotation = getCameraPhotoOrientation(mFileTemp.getAbsolutePath());
               
                startCropImage(String.valueOf(rotation));
                break;
            case REQUEST_CODE_CROP_IMAGE:

                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                Uri uriPath = Uri.parse(new File(path).toString());
                i.putExtra("photo", uriPath);
    
                startActivity(i);

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

}

