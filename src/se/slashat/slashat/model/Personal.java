package se.slashat.slashat.model;

public class Personal {
	private final String name;
	private final String email;
	private final String twitter;
	private final String homepage;
	private final String bio;
	private final int img;

	public Personal(String name, int img, String email, String twitter, String homepage, String bio) {
		super();
		this.name = name;
		this.img = img;
		this.email = email;
		this.twitter = twitter;
		this.homepage = homepage;
		this.bio = bio;
	}

	public String getName() {
		return name;
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
