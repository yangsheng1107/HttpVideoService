// http://custom-android-dn.blogspot.tw/2013/01/create-simple-file-explore-in-android.html
package com.example.videostream.videolist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.videostream.R;
import com.example.videostream.VideoStreamDefine;
import com.example.videostream.VideoViewActivity;
import com.example.videostream.upload.UploadFormActivity;

public class VideoManager extends Activity implements VideoListCallback {

	// constant value
	private static String LIST_URL;

	private VideoArrayAdapter adapter = null;
	private ListView list;
	private ProgressDialog pDialog = null;
	private VideoStreamDefine VideoDefine;
	List<VideoItem> Items = new ArrayList<VideoItem>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_list);

		list = (ListView) findViewById(R.id.listView1);

		VideoDefine = new VideoStreamDefine();
		LIST_URL = VideoDefine.getserviceUrl() + "tube/videolistquery.php";

		new VideoListFromURL(this).execute(LIST_URL, "0");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

		// clear preview data
		adapter.removeAllItem();
		Items.clear();
		new VideoListFromURL(this).execute(LIST_URL, "0");
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
		case R.id.video_list_menu_sort:
			displayMenuDialog();
			break;
		case R.id.video_list_menu_upload:
			Intent intent = new Intent(VideoManager.this,
					UploadFormActivity.class);
			startActivity(intent);
			break;
		case R.id.video_list_menu_quit:
			finish();
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
		inflater.inflate(R.menu.video_list_menu, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	private void displayMenuDialog() {
		final String[] options = { "Default", "Sort by title(des)",
				"Sort by duration", "Sort by duration(des)" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(
				R.string.video_manager_selection_dialog_title));
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				// Do something with the selection
				String mode;
				switch (position) {
				case 1:
					mode = "1";
					break;
				case 2:
					mode = "2";
					break;
				case 3:
					mode = "3";
					break;
				default:
					mode = "0";
					break;
				}

				// clear preview data
				adapter.removeAllItem();
				Items.clear();
				new VideoListFromURL(VideoManager.this).execute(LIST_URL, mode);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Callback function of VideoListFromURL AsyncTask
	 */
	@Override
	public void onMovieListPreExecute() {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(VideoManager.this);
		pDialog.setMessage(getResources().getString(
				R.string.video_manager_download_progress_dialog_context));
		pDialog.setIndeterminate(false);// 取消進度條
		pDialog.setCancelable(true);// 開啟取消
		pDialog.show();
	}

	@Override
	public void doMovieListInBackground(VideoItem item) {
		// TODO Auto-generated method stub
		Items.add(item);
	}

	@Override
	public void doMovieListPostExecute(String result) {
		// TODO Auto-generated method stub
		pDialog.dismiss();
		if (result != null) {
			Toast.makeText(VideoManager.this, result, Toast.LENGTH_LONG).show();
		}

		adapter = new VideoArrayAdapter(VideoManager.this,
				R.layout.video_list_row_single, Items);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(VideoManager.this,
						VideoViewActivity.class);
				intent.putExtra("videoUrl", Items.get(position).getVideoUrl());
				startActivity(intent);
			}
		});
	}
}
