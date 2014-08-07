package se.slashat.slashapp.viewmodel;

import java.util.Date;

import se.slashat.slashapp.model.Episode;

public class EpisodeViewModel extends ViewModelBase<Episode> {

    private final int episodeNumber;
    private final String name;
    private final String description;
    private final String duration;
    private final Date published;
    private final Episode episode;

    public EpisodeViewModel(Episode episodeDatamodel) {
        super();
        episodeNumber = episodeDatamodel.getEpisodeNumber();
        name = episodeDatamodel.getName();
        description = episodeDatamodel.getDescription();
        duration = episodeDatamodel.getDuration();
        published = episodeDatamodel.getPublished();
        this.episode = episodeDatamodel;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }

    public Date getPublished() {
        return published;
    }

    public Episode getEpisode() {
        return episode;
    }

    public String getFormatedEpisodeNumber() {
        return String.format("#%03d", episodeNumber);
    }
}
