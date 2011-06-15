package com.cockpitconfig.objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.sf.json.JSONObject;

public class Sources {

	private int id;
	private String url;
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", this.id);
		json.put("url", this.url);
		json.put("description", this.description);
		return json;
	}
}