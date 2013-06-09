package se.slashat.slashat.model;

import java.io.Serializable;

public class Personal implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private final String name;
	private final String title;
	private final String email;
	private final String twitter;
	private final String homepage;
	private final String bio;
	private final int img;

	public Personal(String name, String title, int img, String email, String twitter, String homepage, String bio) {
		super();
		this.name = name;
		this.title = title;
		this.img = img;
		this.email = email;
		this.twitter = twitter;
		this.homepage = homepage;
		this.bio = bio;
	}

	public String getName() {
		return name;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getImg() {
		return img;
	}

	public String getEmail() {
		return email;
	}

	public String getTwitter() {
		return twitter;
	}
	
	public String getHomepage() {
		return homepage;
	}

	public String getBio() {
		return bio;
	}

}
