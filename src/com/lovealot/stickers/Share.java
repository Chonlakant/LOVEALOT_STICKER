package com.lovealot.stickers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class Share extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				String text = intent.getStringExtra(Intent.EXTRA_TEXT);

			} else if (type.startsWith("image/")) {
				Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
				// imageView1.setImageURI(uri);
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				i.putExtra("photo", uri);
				startActivity(i);
				finish();
			}
		}

	}

}