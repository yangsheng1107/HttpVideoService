package com.example.videostream.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.videostream.VideoStreamDefine;
import com.example.videostream.videolist.VideoItem;

public class UploadFileFromURL extends AsyncTask<String, String, String> {
	UploadFileCallback callback;
	// constant value
	private static final String ACTIVITY_TAG = "UPLOAD";
	private static String INSERT_URL;
	private static final int TIMEOUT = 5;
	// JSON element ids from repsonse of php script:
	private static final String JSON_TAG_SUCCESS = "success";
	private static final String JSON_TAG_MESSAGE = "message";

	//
	private JSONParser jsonParser;
	private VideoStreamDefine VideoDefine;

	public UploadFileFromURL(UploadFileCallback callback) {
		this.callback = callback;

		VideoDefine = new VideoStreamDefine();
		jsonParser = new JSONParser();

		INSERT_URL = VideoDefine.getserviceUrl() + "tube/videoinsertquery.php";
	}

	private int updateVideoInBackground(String url, String filePath,
			String fileName) {
		int success;
		File videoFile = new File(filePath + fileName);

		if (!videoFile.isFile()) {
			String strMessage = "Source File not exist :" + filePath + fileName;
			Log.e("uploadFile", strMessage);
			return -1;
		}

		JSONObject json = jsonParser.makeHttpRequest(url, videoFile, fileName);
		if (json == null) {
			return -2;
			// return "No data or connection failure";
		}
		try {
			Log.d(ACTIVITY_TAG, "Upload attempt : " + json.toString());
			success = json.getInt(JSON_TAG_SUCCESS);
			if (success == 1) {
				Log.d(ACTIVITY_TAG, "Upload Successful : " + json.toString());

				return 0;
				// return json.getString(JSON_TAG_MESSAGE);
			} else {
				Log.d(ACTIVITY_TAG,
						"Upload Failure : " + json.getString(JSON_TAG_MESSAGE));
				// return json.getString(JSON_TAG_MESSAGE);
				return -3;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -4;
	}

	private int updateThumbInBackground(String url, String filePath,
			String thumbName, Bitmap thumb) {
		int success;
		File thumbFile = new File(filePath + thumbName);
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(thumbFile);
			thumb.compress(Bitmap.CompressFormat.PNG, 90, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		JSONObject json = jsonParser.makeHttpRequest(url, thumbFile, thumbName);
		if (json == null) {
			return -2;
			// return "No data or connection failure";
		}
		try {
			Log.d(ACTIVITY_TAG, "Upload attempt : " + json.toString());
			success = json.getInt(JSON_TAG_SUCCESS);
			if (success == 1) {
				Log.d(ACTIVITY_TAG, "Upload Successful : " + json.toString());

				thumbFile.delete();
				return 0;
				// return json.getString(JSON_TAG_MESSAGE);
			} else {
				Log.d(ACTIVITY_TAG,
						"Upload Failure : " + json.getString(JSON_TAG_MESSAGE));
				// return json.getString(JSON_TAG_MESSAGE);
				return -3;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -4;
	}

	private String getErrorMessage(int errcode) {
		switch (errcode) {
		case 0:
			return "success";
		case -1:
			return "Source File not exist";
		case -2:
			return "No data or connection failure";
		case -3:
			return "JSON parsion failure";
		case -4:
			return "undefine error";
		default:
		}
		return null;
	}

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		callback.onUploadFilePreExecute();
	}

	/**
	 * Starting background thread
	 * */
	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub
		int success;
		String videoName = args[3];
		String thumbName = videoName.substring(0,
				videoName.lastIndexOf('.') + 1) + "jpg";

		/**
		 * Video
		 */
		success = updateVideoInBackground(args[0], args[2], videoName);
		if (success != 0) {
			return getErrorMessage(success);
		}

		/**
		 * Thumb
		 */
		VideoItem item = callback.onUploadFileGetVideoItem();
		success = updateThumbInBackground(args[1], args[2], thumbName,
				item.getBitmap());
		if (success != 0) {
			return getErrorMessage(success);
		}

		/**
		 * Update database
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("title", item.getTitle()));
		params.add(new BasicNameValuePair("duration", item.getDuration()));
		params.add(new BasicNameValuePair("description", item.getDescription()));
		params.add(new BasicNameValuePair("video_rul", "video/" + videoName));
		params.add(new BasicNameValuePair("thumb_rul", "thumb/" + thumbName));

		// request method is POST
		DefaultHttpClient httpClient = new DefaultHttpClient();

		// Set http time out
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT * 1000); // http.connection.timeout
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT * 1000); // http.socket.timeout

		// http post method, set params by setEntity
		HttpPost httpPost = new HttpPost(INSERT_URL);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse httpResponse = httpClient.execute(httpPost);

			// Http connect success.
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

			} else {
				return null;
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "success";
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		callback.doUploadFilePostExecute(result);
	}

}
