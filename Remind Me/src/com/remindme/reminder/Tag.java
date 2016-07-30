package com.remindme.reminder;

public class Tag {
	private String tag;
	private int tag_id;

	public Tag(int tag_id, String tag) {
		this.tag_id = tag_id;
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getTagId() {
		return tag_id;
	}

	public void setTagId(int tag_id) {
		this.tag_id = tag_id;
	}
	
	public String toString(){
		return getTag() + " | " + getTagId();
	}
}
