package com.example.videostream.filemanager;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;

import android.os.AsyncTask;

public class FileExplor extends AsyncTask<File, String, String> {
	FileExplorerCallback callback;

	public FileExplor(FileExplorerCallback callback) {
		this.callback = callback;
	}

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		callback.onFileExPreExecute();
	}

	@Override
	protected String doInBackground(File... args) {
		// TODO Auto-generated method stub
		File f = args[0];
		File[] dirs = f.listFiles();

		try {
			for (File ff : dirs) {
				// get last modify date
				Date lastModDate = new Date(ff.lastModified());
				DateFormat formater = DateFormat.getDateTimeInstance();
				String date_modify = formater.format(lastModDate);

				if (ff.isDirectory()) {
					// get number of files in directory
					File[] fbuf = ff.listFiles();
					int buf = 0;
					if (fbuf != null) {
						buf = fbuf.length;
					} else
						buf = 0;
					String num_item = String.valueOf(buf);
					if (buf == 0)
						num_item = num_item + " item";
					else
						num_item = num_item + " items";

					callback.onFileExInBackgroundUpdateItem(
							"directory_icon",
							new Item(ff.getName(), num_item, date_modify, ff
									.getAbsolutePath(), "directory_icon"));
				} else {
					String ext = getFileExtension(ff).toLowerCase();
					String resType;
					switch (ext) {
					case "jpg":
					case "png":
					case "bmp":
						resType = "pics_icon";
						break;
					case "mp4":
					case "3gp":
						resType = "movies_icon";

						// add into list
						callback.onFileExInBackgroundUpdateItem(resType,
								new Item(ff.getName(), ff.length() + " Byte",
										date_modify, ff.getAbsolutePath(),
										resType));
						break;
					default:
						resType = "file_icon";
					}
				}
			}
		} catch (Exception e) {

		}

		callback.onFileExInBackgroundUpdateList(f);
		return null;
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		callback.doFileExPostExecute(result);
	}

	private static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}
}
