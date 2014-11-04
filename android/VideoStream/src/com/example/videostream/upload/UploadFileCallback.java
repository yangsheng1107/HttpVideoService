package com.example.videostream.upload;

import com.example.videostream.videolist.VideoItem;

public interface UploadFileCallback {
	void onUploadFilePreExecute();

	void onUploadFileProgressUpdate(int value);

	VideoItem onUploadFileGetVideoItem();

	void doUploadFilePostExecute(String result);
}
