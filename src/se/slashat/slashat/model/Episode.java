package se.slashat.slashat.model;

import java.io.Serializable;
import java.util.Date;

public class Episode implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private final String name;
	private final String streamUrl;
	private final String duration;
	private final Date published;
	private final int episodeNumber;
	
	public Episode(String name, int episodeNumber, String streamUrl, String duration, Date published) {
		this.name = name;
		this.episodeNumber = episodeNumber;
		this.streamUrl = streamUrl;
		this.duration = duration;
		this.published = published;
	}
	public String getName() {
		return name;
	}
	public String getStreamUrl() {
		return streamUrl;
	}
	
	public int getEpisodeNumber() {
		return episodeNumber;
	}
	
	public String getDuration() {
		return duration;
	}
	
	public Date getPublished() {
		return published;
	}
}
