package com.example.videostream.filemanager;

import java.io.File;

public interface FileExplorerCallback {
	void onFileExPreExecute();

	void onFileExInBackgroundUpdateItem(String type, Item item);

	void onFileExInBackgroundUpdateList(File f);

	void doFileExPostExecute(String result);
}
