package com.example.videostream.upload;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.videostream.R;
import com.example.videostream.VideoStreamDefine;
import com.example.videostream.filemanager.FileManager;
import com.example.videostream.videolist.VideoItem;

public class UploadFormActivity extends Activity implements UploadFileCallback {
	private static String VIDEO_UPLOAD_URL;
	private static String THUMB_UPLOAD_URL;

	private static final int REQUEST_PATH = 1;

	private ImageView videoThumb;
	private Button uploadButton;
	private EditText editPath;
	private EditText editName;
	private EditText editDuration;
	private EditText editDescription;
	private ProgressDialog pDialog = null;
	private VideoStreamDefine VideoDefine;
	/********** File Path *************/
	String uploadFilePath;
	String uploadFileName;

	private Bitmap thumb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_upload_form);

		initUploadFormActivityView();
		initUploadFormActivityEvent();

		VideoDefine = new VideoStreamDefine();
		VIDEO_UPLOAD_URL = VideoDefine.getserviceUrl() + "tube/videoupload.php";
		THUMB_UPLOAD_URL = VideoDefine.getserviceUrl() + "tube/thumbupload.php";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// See which child activity is calling us back.
		if (requestCode == REQUEST_PATH) {
			if (resultCode == RESULT_OK) {
				uploadFilePath = data.getStringExtra("GetPath") + "/";
				uploadFileName = data.getStringExtra("GetFileName");
				editPath.setText(uploadFilePath + uploadFileName);

				thumb = ThumbnailUtils.createVideoThumbnail(uploadFilePath
						+ uploadFileName,
						MediaStore.Images.Thumbnails.MINI_KIND);

				// get video duration
				int msec = MediaPlayer
						.create(UploadFormActivity.this,
								Uri.fromFile(new File(uploadFilePath
										+ uploadFileName))).getDuration();
				videoThumb.setImageBitmap(thumb);
				editPath.setText(uploadFilePath + uploadFileName);
				editDuration.setText(durationConvert(msec));

			}
		}
	}

	private static String durationConvert(int msec) {
		// TODO Auto-generated method stub
		if (msec < 1000)
			return "00:00:01";

		int sec = msec / 1000;
		int min = 0;
		int hr = 0;
		if (sec >= 3600) {
			hr = sec / 3600;
			sec %= 3600;
		}

		if (sec >= 60) {
			min = sec / 60;
			min %= 60;
		}

		sec = sec % 60;

		return String.format("%02d:%02d:%02d", hr, min, sec);
	}

	private void initUploadFormActivityView() {
		// TODO Auto-generated method stub
		uploadButton = (Button) findViewById(R.id.uploadButton);
		editPath = (EditText) findViewById(R.id.editUpFormPath);
		editName = (EditText) findViewById(R.id.editUpFormName);
		editDuration = (EditText) findViewById(R.id.editUpFormDuration);
		editDescription = (EditText) findViewById(R.id.editUpFormDescription);
		videoThumb = (ImageView) findViewById(R.id.imageView1);
	}

	private void initUploadFormActivityEvent() {
		// TODO Auto-generated method stub
		uploadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new UploadFileFromURL(UploadFormActivity.this).execute(
						VIDEO_UPLOAD_URL, THUMB_UPLOAD_URL, uploadFilePath,
						uploadFileName);
			}
		});

		editPath.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UploadFormActivity.this,
						FileManager.class);
				startActivityForResult(intent, REQUEST_PATH);
			}
		});
	}

	@Override
	public void onUploadFilePreExecute() {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(UploadFormActivity.this);
		pDialog.setMessage(getResources().getString(
				R.string.main_activity_uploading_progress_dialog_context));
		pDialog.setIndeterminate(false);// 取消進度條
		pDialog.setCancelable(true);// 開啟取消
		pDialog.show();
	}

	@Override
	public void onUploadFileProgressUpdate(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public VideoItem onUploadFileGetVideoItem() {
		// TODO Auto-generated method stub
		VideoItem item = new VideoItem();
		item.setTitle(editName.getText().toString());
		item.setDuration(editDuration.getText().toString());
		item.setDescription(editDescription.getText().toString());
		item.setBitmap(thumb);
		return item;
	}

	@Override
	public void doUploadFilePostExecute(String result) {
		// TODO Auto-generated method stub
		pDialog.dismiss();
		if (result != "success") {
			Toast.makeText(UploadFormActivity.this, result, Toast.LENGTH_LONG)
					.show();
		} else {
			finish();
		}
	}

}