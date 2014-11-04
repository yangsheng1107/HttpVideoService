// http://custom-android-dn.blogspot.tw/2013/01/create-simple-file-explore-in-android.html
package com.example.videostream.filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.videostream.R;

public class FileManager extends Activity implements FileExplorerCallback {

	private File currentDir;
	private FileArrayAdapter adapter = null;
	private ListView list;
	List<Item> dir = new ArrayList<Item>();
	List<Item> fls = new ArrayList<Item>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filemamager_list);
		currentDir = new File(Environment.getExternalStorageDirectory() + "/");

		new FileExplor(FileManager.this).execute(currentDir);
	}

	/**
	 * ListView : ListView 的初始化
	 * 
	 */
	private void initListView(List<Item> dir) {
		adapter = new FileArrayAdapter(FileManager.this,
				R.layout.file_list_row_single, dir);

		list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(adapter);
	}

	/**
	 * ListView : ListView 的事件處理
	 * 
	 */
	private void initListViewEvent() {
		// TODO Auto-generated method stub
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Item o = adapter.getItem(position);
				if (o.getImage().equalsIgnoreCase("directory_icon")
						|| o.getImage().equalsIgnoreCase("directory_up")) {
					currentDir = new File(o.getPath());

					new FileExplor(FileManager.this).execute(currentDir);
				} else if (o.getImage().equalsIgnoreCase("movies_icon")) {
					Intent intent = new Intent();
					intent.putExtra("GetPath", currentDir.toString());
					intent.putExtra("GetFileName", o.getName());
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}

	/**
	 * Callback function of FileExplorer AsyncTask
	 */
	@Override
	public void onFileExPreExecute() {
		// TODO Auto-generated method stub
		if (adapter != null)
			adapter.removeAllItem();
		if (!dir.isEmpty())
			dir.clear();
		if (!fls.isEmpty())
			fls.clear();
	}

	@Override
	public void onFileExInBackgroundUpdateItem(String type, Item item) {
		// TODO Auto-generated method stub
		if (type == "directory_icon") {
			dir.add(item);
		} else {
			fls.add(item);
		}
	}

	@Override
	public void onFileExInBackgroundUpdateList(File f) {
		// TODO Auto-generated method stub
		// sort dir & file list
		Collections.sort(dir);
		Collections.sort(fls);

		// append file list to dir list
		dir.addAll(fls);

		// add "parent directory" item on top
		if (!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0, new Item("..", "Parent Directory", "", f.getParent(),
					"directory_up"));
	}

	@Override
	public void doFileExPostExecute(String result) {
		// TODO Auto-generated method stub
		initListView(dir);
		initListViewEvent();
		if (result != null) {
			Toast.makeText(FileManager.this, result, Toast.LENGTH_LONG).show();
		}
	}
}
