package com.remindme.filter;

import org.joda.time.DateTime;

public class Filter {
	private String name;
	private int id;
	private String tags;
	private DateTime created_before;
	private DateTime created;
	private DateTime created_after;
	private DateTime due_before;
	private DateTime due;
	private DateTime due_after;
	private Boolean completed;
	private Boolean deleted;
	
	public Filter(String name, int id){
		this.name = name;
		this.id = id;
	}
	
	public Filter(String name, int id, String tags, 
			DateTime created_before, DateTime created, DateTime created_after, 
			DateTime due_before, DateTime due, DateTime due_after, 
			Boolean completed, Boolean deleted)
	{
		this.name = name;
		this.id = id;
		this.tags = tags;
		this.created_before = created_before;
		this.created = created;
		this.created_after = created_after;
		this.due_before = due_before;
		this.due = due;
		this.due_after = due_after;
		this.completed = completed;
		this.deleted = deleted;
	}
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public DateTime getCreatedBefore() {
		return created_before;
	}
	public void setCreatedBefore(DateTime created_before) {
		this.created_before = created_before;
	}
	public DateTime getCreated() {
		return created;
	}
	public void setCreated(DateTime created) {
		this.created = created;
	}
	public DateTime getCreatedAfter() {
		return created_after;
	}
	public void setCreatedAfter(DateTime created_after) {
		this.created_after = created_after;
	}
	public DateTime getDueBefore() {
		return due_before;
	}
	public void setDueBefore(DateTime due_before) {
		this.due_before = due_before;
	}
	public DateTime getDue() {
		return due;
	}
	public void setDue(DateTime due) {
		this.due = due;
	}
	public DateTime getDueAfter() {
		return due_after;
	}
	public void setDueAfter(DateTime due_after) {
		this.due_after = due_after;
	}
	public Boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public Boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
