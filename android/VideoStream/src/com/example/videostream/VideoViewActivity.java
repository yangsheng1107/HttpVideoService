package com.example.videostream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class VideoViewActivity extends Activity implements DownloadFileCallback{
	private String urlPath;
	private ProgressDialog pDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_view);
		
		Intent i = getIntent();
		urlPath = i.getStringExtra("videoUrl");
		
		VideoView vidView = (VideoView)findViewById(R.id.myVideo);
		MediaController vidControl = new MediaController(this);
		vidControl.setAnchorView(vidView);
		vidView.setMediaController(vidControl);

		Uri vidUri = Uri.parse(urlPath);
		vidView.setVideoURI(vidUri);
		vidView.start();		
	}
	
	/**
	 * Menu
	 */	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.video_view_menu_download:
			String filePathName = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/test.mp4";

			new DownloadFileFromURL(VideoViewActivity.this).execute(
					urlPath, filePathName);			
			break;
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.clear();
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.video_view_menu, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onDownloadFilePreExecute() {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(VideoViewActivity.this);
		pDialog.setMessage(getResources().getString(
				R.string.video_view_activity_download_progress_dialog_context));
		pDialog.setIndeterminate(false);// 取消進度條
		pDialog.setCancelable(true);// 開啟取消
		pDialog.setMax(100);
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog.show();
	}

	@Override
	public void onDownloadFileProgressUpdate(int value) {
		// TODO Auto-generated method stub
		pDialog.setProgress(value);
	}

	@Override
	public void doDownloadFilePostExecute(String result) {
		// TODO Auto-generated method stub
		pDialog.dismiss();

		if (result != null) {
			Toast.makeText(VideoViewActivity.this, result, Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Callback function of VideoListFromURL AsyncTask
	 */		
}
