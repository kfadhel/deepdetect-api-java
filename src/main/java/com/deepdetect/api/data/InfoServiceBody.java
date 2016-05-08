package com.deepdetect.api.data;

import java.util.List;

public class InfoServiceBody {
	String mllib;
	String description;
	String name;

	public String getMllib() {
		return mllib;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	List<Job> jobs;
}
