package com.lovealot.stickers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ResultSave extends Activity {
	ImageView image;
	Uri uri;
	Button btn_result_share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_svae);
		image = (ImageView) findViewById(R.id.image_share);
		btn_result_share = (Button) findViewById(R.id.btn_result_share);
		uri = getIntent().getParcelableExtra("reuslt");
		image.setImageURI(uri);
		btn_result_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent share = new Intent(Intent.ACTION_SEND);

				// Type of file to share
				share.setType("image/jpeg");
				share.putExtra(Intent.EXTRA_STREAM, uri);

				// Start the share dialog
				startActivity(Intent.createChooser(share, "Share Image"));

			}
		});

	}

}
