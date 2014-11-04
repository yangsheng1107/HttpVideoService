package com.example.videostream.videolist;

public interface DeleteVideoCallback {
	void onDeleteVideoPreExecute();

	void doDeleteVideoPostExecute(String result);
}
