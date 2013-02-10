package se.slashat.slashat.model;

import java.io.Serializable;

public class Episode implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String streamUrl;
	private int episodeNumber;
	
	public Episode(String name, int episodeNumber, String streamUrl) {
		this.name = name;
		this.episodeNumber = episodeNumber;
		this.streamUrl = streamUrl;
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
}
