package com.example.videostream;

public interface DownloadFileCallback {
	void onDownloadFilePreExecute();

	void onDownloadFileProgressUpdate(int value);

	void doDownloadFilePostExecute(String result);
}
