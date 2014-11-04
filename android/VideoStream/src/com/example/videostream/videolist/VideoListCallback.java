package com.example.videostream.videolist;

public interface VideoListCallback {
	void onMovieListPreExecute();

	void doMovieListInBackground(VideoItem item);

	void doMovieListPostExecute(String result);
}
