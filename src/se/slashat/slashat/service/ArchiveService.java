package se.slashat.slashat.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

import se.slashat.slashat.async.EpisodeLoaderAsyncTask.UpdateCallback;
import se.slashat.slashat.model.Episode;

/**
 * Load episodes from a source (currently Feedburner but later on our own
 * webservice)
 * 
 * @author Nicklas Löf
 * 
 */

public class ArchiveService {
	private static Pattern episodeNumberPattern = Pattern.compile("#[0-9]*");
	private static Pattern episodeNamePattern = Pattern.compile("-.*");
	private static String progressMessage = "";
	private static int episodeCount;
	private static ArrayList<Episode> episodes = new ArrayList<Episode>();

	public static int getProcessedNumbers() {
		return episodes.size();
	}

	public static String getProgressMessage() {
		return progressMessage;
	}

	public static int getNumberOfEpisodes() {
		return episodeCount;
	}

	public static Episode[] getEpisodes(final UpdateCallback updateCallback) {

		// If we already have parsed the Episodes return that cached list.
		if (!episodes.isEmpty()) {
			return episodes.toArray(new Episode[episodes.size()]);
		}

		episodes.add(new Episode("STOP PLAYING", 999, null, "", new Date()));
		// Temporary to allow stop playing until we have better player controls
		// implemented

		try {
			FeedburnerParser feedburnerParser = new FeedburnerParser();
			progressMessage = "Hämtar avsnittsinformation";
			updateCallback.onUpdate();
			URL url = new URL("http://feeds.feedburner.com/slashat?format=xml");
			InputStream inputStream = url.openStream();

			try {
				progressMessage = "Extraherar avsnitt";
				updateCallback.onUpdate();
				feedburnerParser.parseFeed(inputStream,
						new FeedburnerParser.Itemcallback() {
							/*
							 * For every item that the Feedburner parser founds
							 * we are creating our Episode model object and
							 * tells the Asynctask that we have an update so it
							 * can display the progress.
							 */
							@Override
							public void callback(String title, String url,
									String duration, Date published) {
								// This processing is not safe to changes in the
								// titles in the RSS-feed and might
								// break in case of an "conflicting" episode
								// title.
								int episodeNumber = 000;
								String episodeTitle = "";
								Matcher matcher = episodeNumberPattern
										.matcher(title);
								if (matcher.find()) {
									episodeNumber = Integer.valueOf(matcher
											.group().replaceFirst("#", ""));
								}

								matcher = episodeNamePattern.matcher(title);

								if (matcher.find()) {
									episodeTitle = matcher.group()
											.replaceFirst("- ", "");
								}

								Episode episode = new Episode(episodeTitle,
										episodeNumber, url, duration, published);
								episodes.add(episode);
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
		}

		if (episodes.isEmpty()) {
			episodes.add(new Episode("Kunde inte ladda några avsnitt", 000,
					null, "", new Date()));
		}

		return episodes.toArray(new Episode[episodes.size()]);

	}
}
