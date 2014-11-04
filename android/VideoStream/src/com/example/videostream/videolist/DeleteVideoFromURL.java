package com.example.videostream.videolist;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class DeleteVideoFromURL extends AsyncTask<String, String, String> {
	DeleteVideoCallback callback;
	// constant value
	private static final String ACTIVITY_TAG = "Delete";

	// JSON element ids from repsonse of php script:
	private static final String JSON_TAG_SUCCESS = "success";
	private static final String JSON_TAG_MESSAGE = "message";

	//
	private JSONParser jsonParser;

	public DeleteVideoFromURL(DeleteVideoCallback callback) {
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
		callback.onDeleteVideoPreExecute();
	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub
		int success;
		Log.d(ACTIVITY_TAG, "Deleting starting");
		String urlPath = args[0];
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("title", args[1]));

		JSONObject json = jsonParser.makeHttpRequest(urlPath, "POST", params);
		if (json == null) {
			return "No data";
		}
		Log.d(ACTIVITY_TAG, "Deleting attempt : " + json.toString());

		try {
			Log.d(ACTIVITY_TAG, "Deleting attempt : " + json.toString());
			success = json.getInt(JSON_TAG_SUCCESS);
			if (success == 1) {
				Log.d(ACTIVITY_TAG, "Deleting Successful : " + json.toString());
				return json.getString(JSON_TAG_MESSAGE);
			} else {
				Log.d(ACTIVITY_TAG,
						"Deleting Failure : "
								+ json.getString(JSON_TAG_MESSAGE));
				return json.getString(JSON_TAG_MESSAGE);
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
		callback.doDeleteVideoPostExecute(result);
	}

}
