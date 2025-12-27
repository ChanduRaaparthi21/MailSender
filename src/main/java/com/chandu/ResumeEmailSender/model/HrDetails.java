package com.chandu.ResumeEmailSender.model;

public class HrDetails {

	private String companyName;
	private String hrEmail;
	private String hrName;
	private String location;
	private String experience;

	public HrDetails(String companyName, String hrEmail, String hrName, String location, String experience) {
		this.companyName = companyName;
		this.hrEmail = hrEmail;
		this.hrName = hrName;
		this.location = location;
		this.experience = experience;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getHrEmail() {
		return hrEmail;
	}

	public String getHrName() {
		return hrName;
	}

	public String getLocation() {
		return location;
	}

	public String getExperience() {
		return experience;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setHrEmail(String hrEmail) {
		this.hrEmail = hrEmail;
	}

	public void setHrName(String hrName) {
		this.hrName = hrName;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}
}
