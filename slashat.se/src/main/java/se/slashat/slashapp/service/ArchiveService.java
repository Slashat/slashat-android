package se.slashat.slashapp.service;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.slashat.slashapp.async.EpisodeLoaderAsyncTask.UpdateCallback;
import se.slashat.slashapp.model.Episode;
import se.slashat.slashapp.util.Network;

/**
 * Load episodes from a source (currently Feedburner but later on our own
 * webservice)
 *
 * @author Nicklas Löf
 */

public class ArchiveService extends SerializableService<Set<Episode>> {
    private final static String TAG = "ArchiveService";
    private static Pattern episodeNumberPattern = Pattern.compile("#[0-9]*");
    private static Pattern episodeNamePattern = Pattern.compile("-.*");
    private static String progressMessage = "";
    private int episodeCount;
    private Set<Episode> episodes = new HashSet<Episode>();
    private final static String episodesFileName = "episodes";
    private static ArchiveService instance;

    public int getProcessedNumbers() {
        return episodes.size();
    }

    public String getProgressMessage() {
        return progressMessage;
    }

    public int getNumberOfEpisodes() {
        return episodeCount;
    }

    public static ArchiveService getInstance() {
        if (instance == null) {
            instance = new ArchiveService();
        }
        return instance;
    }

    public Episode[] getEpisodes(final UpdateCallback updateCallback, final boolean fullRefresh) {

        // If we already have parsed the Episodes return that cached list.
        if (!episodes.isEmpty() && !fullRefresh) {
            return compileEpisodes();
        }
        boolean networkAvailable = Network.isNetworkAvailable();
        // Load serialized episodes.
        if (!fullRefresh && !networkAvailable) {
            loadEpisodes();
        } else {
            episodes.clear();
        }

        if (networkAvailable) {
            InputStream inputStream = null;
            // Start parse the feed. When an episode already exists in our list stop
            // the parsing.
            try {
                final FeedburnerParser feedburnerParser = new FeedburnerParser();
                progressMessage = "Hämtar avsnittsinformation";
                updateCallback.onUpdate();
                URL url = new URL("http://slashat.se/avsnitt.rss");
                inputStream = url.openStream();

                try {
                    progressMessage = "Extraherar avsnitt";
                    updateCallback.onUpdate();
                    feedburnerParser.parseFeed(inputStream, new FeedburnerParser.Itemcallback() {
                        /*
                         * For every item that the Feedburner parser founds we are
                         * creating our Episode model object and tells the Asynctask
                         * that we have an update so it can display the progress.
                         */
                        @Override
                        public void callback(String title, String url, String duration, String itunesSubtitle, String itunesSummary, Date published) {
                            // This processing is not safe to changes in the
                            // titles in the RSS-feed and might
                            // break in case of an "conflicting" episode
                            // title.
                            int episodeNumber = 000;
                            String episodeTitle = "";
                            Matcher matcher = episodeNumberPattern.matcher(title);
                            if (matcher.find()) {
                                episodeNumber = Integer.valueOf(matcher.group().replaceFirst("#", ""));
                            }

                            matcher = episodeNamePattern.matcher(title);

                            if (matcher.find()) {
                                episodeTitle = matcher.group().replaceFirst("- ", "");
                            }

                            Episode episode = new Episode(episodeTitle, episodeNumber, url, duration, published, itunesSubtitle, itunesSummary);
                            if (episodes.contains(episode) && !fullRefresh) {
                                feedburnerParser.interrupt();
                            } else {
                                episodes.add(episode);
                            }
                            updateCallback.onUpdate();
                        }

                        @Override
                        public void setCount(int attributeCount) {
                            episodeCount = attributeCount;

                        }
                    });
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!episodes.isEmpty()) {
            saveEpisodes();
        }
        return compileEpisodes();
    }

    private Episode[] compileEpisodes() {
        if (episodes.isEmpty()) {
            episodes.add(new Episode("Kunde inte ladda några avsnitt", 000, null, "", new Date(), "", ""));
        }
        ArrayList<Episode> episodeArray = new ArrayList<Episode>(episodes);
        Collections.sort(episodeArray);

        return episodeArray.toArray(new Episode[episodeArray.size()]);
    }

    private void saveEpisodes() {
        save(episodes);
    }

    private void loadEpisodes() {
        episodes = load();
        if (episodes == null){
            episodes = Collections.EMPTY_SET;
        }
    }

    @Override
    public String getFilename() {
        return episodesFileName;
    }
}
