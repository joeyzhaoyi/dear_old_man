package com.github.joey.dearoldman;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = "MainActivity";
	
	private ImageButton mPhoneItem = null;
	private ImageButton mRadioItem = null;
	private ImageButton mVideoChatItem = null;
	private ImageButton mCustomItem = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Phone
		mPhoneItem = (ImageButton) findViewById(R.id.phoneButton);
		mPhoneItem.setImageBitmap(genTextBitmap("Phone"));
		mPhoneItem.setOnClickListener(this);
		
		// Radio
		mRadioItem = (ImageButton) findViewById(R.id.radioButton);
		mRadioItem.setImageBitmap(genTextBitmap("Radio"));
		mRadioItem.setOnClickListener(this);
		
	  // Video Chat
		mVideoChatItem = (ImageButton) findViewById(R.id.videoButton);
		mVideoChatItem.setImageBitmap(genTextBitmap("Video"));
		mVideoChatItem.setOnClickListener(this);
		
		// custom
		mCustomItem = (ImageButton) findViewById(R.id.customButton);
		mCustomItem.setImageBitmap(genTextBitmap("Custom"));
		mCustomItem.setOnClickListener(this);
	}

	private Bitmap genTextBitmap(String text) {
		// they are all ZERO!
		Log.d(TAG, "item01: width=" + mPhoneItem.getWidth() + ", heigh=" + mPhoneItem.getHeight());
		
		float scale = getResources().getDisplayMetrics().density;

		// TODO: adjust size
		Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bitmap);
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.rgb(0xFF, 0, 0));
		// TODO: scale needed
		paint.setTextSize(80f*scale);
		
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		
		// int x = (bitmap.getWidth() - bounds.width()) / 6;
		// int y = (bitmap.getHeight() - bounds.height()) / 5;
		int x = 0;
		int y = bounds.height();
		
		canvas.drawText(text, x, y, paint);
		
		return bitmap;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mPhoneItem) {
			onPhoneItemClicked();
		} else if (v == mRadioItem) {
			onRadioItemClicked();
		} else if (v == mVideoChatItem) {
			onVideoChatItemClicked();
		} else if (v == mCustomItem) {
			onCustomItemClicked();
		} else {
			// do nothing
		}
	}

	private void onCustomItemClicked() {
		// TODO Auto-generated method stub
		
	}

	private void onVideoChatItemClicked() {
		Log.d(TAG, "onVideoChatItemClicked");

		final String packageName = "com.tencent.mobileqq";

		launchApplicationEx(packageName);
	}

	private void onRadioItemClicked() {
		// TODO Auto-generated method stub
		
	}

	private void onPhoneItemClicked() {
		// TODO Auto-generated method stub
		
	}

	private PackageInfo getPackageInfo(String packageName) {
		PackageInfo packageInfo = null;
		
		try {
			packageInfo = getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return packageInfo;
	}
	
	private boolean launchApplication(String packageName) {
		PackageInfo packageInfo = getPackageInfo(packageName);
		if (packageInfo == null) {
			Toast.makeText(
					getApplicationContext(),
					"pacage [" + packageName + "] doesn't exist",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		Log.d(TAG, "Original package: " + packageName);
		Log.d(TAG, "Getted package: " + packageInfo.packageName);

		// resolve package
		ResolveInfo resolveInfo;
		{
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveIntent.setPackage(packageInfo.packageName);
			
			List<ResolveInfo> activities =
					getPackageManager().queryIntentActivities(resolveIntent, 0);
			resolveInfo = activities.iterator().next();
		}
		
		if (resolveInfo == null) {
			Toast.makeText(
					getApplicationContext(),
					"resolved info is null:(",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		// launch it
		{
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName component =
					new ComponentName(
							resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
			intent.setComponent(component);
			startActivity(intent);
		}
		
		return true;
	}
	
	private boolean launchApplicationEx(String packageName) {
		Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
		if (intent == null) {
			return false;
		} else {
			startActivity(intent);
			return true;
		}
	}

}
