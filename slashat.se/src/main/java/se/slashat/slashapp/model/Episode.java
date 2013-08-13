package se.slashat.slashapp.model;

import java.io.Serializable;
import java.util.Date;

public class Episode implements Serializable, Comparable<Episode> {

	private static final long serialVersionUID = 1L;
	private final String name;
	private final String streamUrl;
	private final String duration;
	private final String description;
    private final String shownotes;
	private final Date published;
	private final int episodeNumber;
	private String fullEpisodeName;

	public Episode(String name, int episodeNumber, String streamUrl, String duration, Date published, String description, String shownotes) {
		this.name = name;
		this.episodeNumber = episodeNumber;
		this.streamUrl = streamUrl;
		this.duration = duration;
		this.published = published;
		this.description = description;
        this.shownotes = shownotes;
        this.fullEpisodeName = generateFullEpisodeName();
	}

	private String generateFullEpisodeName() {
		return "Avsnitt " + episodeNumber + " - " + name;
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

	public String getDescription() {
		return description;
	}

	public String getFullEpisodeName() {
		if (fullEpisodeName == null || fullEpisodeName.equals("")) {
			fullEpisodeName = generateFullEpisodeName();
		}
		return fullEpisodeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + episodeNumber;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((streamUrl == null) ? 0 : streamUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Episode other = (Episode) obj;
		if (episodeNumber != other.episodeNumber)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (streamUrl == null) {
			if (other.streamUrl != null)
				return false;
		} else if (!streamUrl.equals(other.streamUrl))
			return false;
		return true;
	}

	@Override
	public int compareTo(Episode e2) {
		return e2.published.compareTo(published);
	}

    public String getShownotes() {
        return shownotes;
    }
}
