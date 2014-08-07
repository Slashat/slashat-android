package se.slashat.slashapp.model;

import java.io.Serializable;

public class Crew implements Serializable{
	
	public enum Type {
        SHOW,
		HOST,
		ASSISTANT,
		DEV
	}
	
	private static final long serialVersionUID = 1L;
	private final String name;
	private final String title;
	private final Type type;
	private final String email;
	private final String twitter;
	private final String homepage;
	private final String bio;
	private final int img;

	public Crew(String name, String title, Type type, int img, String email, String twitter, String homepage, String bio) {
		super();
		this.name = name;
		this.title = title;
		this.type = type;
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
	
	public Type getType() {
		return type;
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
