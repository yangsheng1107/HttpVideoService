package com.example.videostream.videolist;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class VideoListFromURL extends AsyncTask<String, String, String> {
	VideoListCallback callback;
	// constant value
	private static final String ACTIVITY_TAG = "MovieList";

	// JSON element ids from repsonse of php script:
	private static final String JSON_TAG_LIST = "lists";

	private static final String JSON_TAG_COUNT = "count";
	private static final String JSON_TAG_TITLE = "title";
	private static final String JSON_TAG_THUMB_URL = "thumb_url";
	private static final String JSON_TAG_VIDEO_URL = "video_url";
	private static final String JSON_TAG_DURATION = "duration";
	private static final String JSON_TAG_DESCRIPTION = "description";

	//
	private JSONParser jsonParser;

	public VideoListFromURL(VideoListCallback callback) {
		this.callback = callback;

		jsonParser = new JSONParser();
	}

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		callback.onMovieListPreExecute();
	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub
		Log.d(ACTIVITY_TAG, "Loading starting");
		String urlPath = args[0];
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mode", args[1]));

		JSONObject json = jsonParser.makeHttpRequest(urlPath, "POST", params);
		if (json == null) {
			return "No data";
		}
		Log.d(ACTIVITY_TAG, "Loading attempt : " + json.toString());

		try {

			JSONArray numberList = json.getJSONArray(JSON_TAG_LIST);

			String imageUrl = urlPath
					.substring(0, urlPath.lastIndexOf('/') + 1);

			for (int i = 0; i < numberList.length(); i++) {
				String count = numberList.getJSONObject(i).getString(
						JSON_TAG_COUNT);
				String imagePath = imageUrl
						+ numberList.getJSONObject(i).getString(
								JSON_TAG_THUMB_URL);
				String videoPath = imageUrl
						+ numberList.getJSONObject(i).getString(
								JSON_TAG_VIDEO_URL);
				String title = numberList.getJSONObject(i).getString(
						JSON_TAG_TITLE);
				String duration = numberList.getJSONObject(i).getString(
						JSON_TAG_DURATION);
				String description = numberList.getJSONObject(i).getString(
						JSON_TAG_DESCRIPTION);
				Log.d(ACTIVITY_TAG, "count : " + count);
				Log.d(ACTIVITY_TAG, "title : " + title);
				Log.d(ACTIVITY_TAG, "imagePath : " + imagePath);
				Log.d(ACTIVITY_TAG, "videoPath : " + videoPath);
				Log.d(ACTIVITY_TAG, "duration : " + duration);
				Log.d(ACTIVITY_TAG, "description : " + description);

				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(imagePath);

				HttpResponse httpResponse;
				try {
					httpResponse = httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						HttpEntity httpEntity = httpResponse.getEntity();
						BufferedHttpEntity b_entity = new BufferedHttpEntity(
								httpEntity);
						InputStream input = b_entity.getContent();

						Bitmap bitmap = BitmapFactory.decodeStream(input);
						callback.doMovieListInBackground(new VideoItem(count,
								title, imagePath, videoPath, duration,
								description, bitmap));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		callback.doMovieListPostExecute(result);
	}

}
