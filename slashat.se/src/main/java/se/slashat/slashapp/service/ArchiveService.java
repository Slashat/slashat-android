package se.slashat.slashapp.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;

import se.slashat.slashapp.MainActivity;
import se.slashat.slashapp.async.EpisodeLoaderAsyncTask.UpdateCallback;
import se.slashat.slashapp.model.Episode;

/**
 * Load episodes from a source (currently Feedburner but later on our own
 * webservice)
 * 
 * @author Nicklas Löf
 * 
 */

public class ArchiveService {
    private final static String TAG = "ArchiveService";
	private static Pattern episodeNumberPattern = Pattern.compile("#[0-9]*");
	private static Pattern episodeNamePattern = Pattern.compile("-.*");
	private static String progressMessage = "";
	private static int episodeCount;
	private static Set<Episode> episodes = new HashSet<Episode>();
	private final static String episodesFileName = "episodes";

	public static int getProcessedNumbers() {
		return episodes.size();
	}

	public static String getProgressMessage() {
		return progressMessage;
	}

	public static int getNumberOfEpisodes() {
		return episodeCount;
	}

	public static Episode[] getEpisodes(final UpdateCallback updateCallback, final boolean fullRefresh) {

		// If we already have parsed the Episodes return that cached list.
		if (!episodes.isEmpty() && !fullRefresh) {
			return compileEpisodes();
		}
		// Load serialized episodes.
		if (!fullRefresh) {
			loadEpisodes();
		} else {
			episodes.clear();
		}

		InputStream inputStream = null;
		// Start parse the feed. When an episode already exists in our list stop
		// the parsing.
		try {
			final FeedburnerParser feedburnerParser = new FeedburnerParser();
			progressMessage = "Hämtar avsnittsinformation";
			updateCallback.onUpdate();
			URL url = new URL("http://feeds.feedburner.com/slashat?format=xml");
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
					public void callback(String title, String url, String duration, String itunesSubtitle, Date published) {
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

						Episode episode = new Episode(episodeTitle, episodeNumber, url, duration, published, itunesSubtitle);
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

		if (!episodes.isEmpty()) {
			saveEpisodes();
		}
		return compileEpisodes();

	}

	private static Episode[] compileEpisodes() {
		if (episodes.isEmpty()) {
			episodes.add(new Episode("Kunde inte ladda några avsnitt", 000, null, "", new Date(), ""));
		}
		ArrayList<Episode> episodeArray = new ArrayList<Episode>(episodes);
		Collections.sort(episodeArray);

		return episodeArray.toArray(new Episode[episodeArray.size()]);
	}

	private static void saveEpisodes() {

		ObjectOutputStream objectOutputStream = null;
		try {
			FileOutputStream fileOutputStream = MainActivity.getContext().openFileOutput(episodesFileName, Context.MODE_PRIVATE);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(episodes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectOutputStream != null) {
					objectOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void loadEpisodes() {
		ObjectInputStream objectInputStream = null;
		try {
			FileInputStream fileInputStream = MainActivity.getContext().openFileInput(episodesFileName);
			objectInputStream = new ObjectInputStream(fileInputStream);
			episodes = (HashSet<Episode>) objectInputStream.readObject();
		} catch (FileNotFoundException e1) {
			Log.i(TAG, "No old episode list found on device. Will reparse the rss feed");
		} catch (IOException e) {
			Log.i(TAG, "No old episode list found on device. Will reparse the rss feed");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (objectInputStream != null) {
				try {
					objectInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
