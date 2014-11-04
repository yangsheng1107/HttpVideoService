package com.example.videostream.filemanager;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videostream.R;

public class FileArrayAdapter extends ArrayAdapter<Item> {
	private Context context;
	private int id;
	private List<Item> data;

	public FileArrayAdapter(Context context, int textViewResourceId,
			List<Item> data) {
		super(context, textViewResourceId, data);
		this.context = context;
		this.id = textViewResourceId;
		this.data = data;
	}

	public void removeAllItem() {

		data.clear();
	}

	@Override
	public Item getItem(int i) {
		return data.get(i);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(id, parent, false);

			// save components to ViewHolder
			holder = new ViewHolder();
			holder.t1 = (TextView) row.findViewById(R.id.TextView01);
			holder.t2 = (TextView) row.findViewById(R.id.TextView02);
			holder.t3 = (TextView) row.findViewById(R.id.TextViewDate);
			holder.imageCity = (ImageView) row.findViewById(R.id.fd_Icon1);

			row.setTag(holder);
		} else {
			// get components from ViewHolder
			holder = (ViewHolder) row.getTag();
		}

		/* create a new view of my layout and inflate it in the row */
		// convertView = ( RelativeLayout ) inflater.inflate( resource, null );

		final Item o = data.get(position);
		if (o != null) {

			/*
			 * int drawableId; if (o.getImage() == "directory_icon") {
			 * drawableId = R.drawable.directory_icon; } else if (o.getImage()
			 * == "file_icon") { drawableId = R.drawable.file_icon; } else {
			 * drawableId = R.drawable.directory_up; } Drawable image =
			 * context.getResources().getDrawable(drawableId);
			 */
			String uri = "drawable/" + o.getImage();
			int drawableId = context.getResources().getIdentifier(uri, null,
					context.getPackageName());
			Drawable image = context.getResources().getDrawable(drawableId);

			holder.imageCity.setImageDrawable(image);

			if (holder.t1 != null)
				holder.t1.setText(o.getName());
			if (holder.t2 != null)
				holder.t2.setText(o.getData());
			if (holder.t3 != null)
				holder.t3.setText(o.getDate());

		}
		return row;
	}

	static class ViewHolder {
		ImageView imageCity;
		TextView t1;
		TextView t2;
		TextView t3;
	}

}
