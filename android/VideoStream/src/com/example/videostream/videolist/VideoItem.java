package com.example.videostream.videolist;

import android.graphics.Bitmap;

public class VideoItem implements Comparable<VideoItem> {
	private String count;
	private String title;
	private String thumb_url;
	private String video_url;
	private String duration;
	private String description;
	private Bitmap bitmap;

	public VideoItem() {

	}

	public VideoItem(String count, String title, String thumb_url,
			String video_url, String duration, String description, Bitmap bitmap) {
		this.count = count;
		this.title = title;
		this.thumb_url = thumb_url;
		this.video_url = video_url;
		this.duration = duration;
		this.description = description;
		this.bitmap = bitmap;
	}

	/**
	 * count
	 */
	public void setCount(String count) {
		this.count = count;
	}

	public String getCount() {
		return count;
	}

	/**
	 * Title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * thumb_url
	 */
	public void setThumbUrl(String thumb_url) {
		this.thumb_url = thumb_url;
	}

	public String getThumbUrl() {
		return thumb_url;
	}

	/**
	 * video_url
	 */
	public void setVideoUrl(String video_url) {
		this.video_url = video_url;
	}

	public String getVideoUrl() {
		return video_url;
	}

	/**
	 * Duration
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDuration() {
		return duration;
	}

	/**
	 * Description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * Bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	@Override
	public int compareTo(VideoItem o) {
		if (this.count != null)
			return this.count.toLowerCase().compareTo(
					o.getCount().toLowerCase());
		else
			throw new IllegalArgumentException();
	}
}
