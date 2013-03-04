package se.slashat.slashat.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * Parse a Feedburner URL and return each entry as a callback. XMLparsing code
 * based on Android developer examples source
 * 
 * @author Nicklas Löf
 * 
 */

public class FeedburnerParser {
	private Itemcallback itemcallback;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

	/**
	 * 
	 * Callers to the Feedburner service needs to pass an implementation of this
	 * interface. For every parsed item found in the xml file it will execute
	 * the callback method with the parsed information.
	 * 
	 */
	public interface Itemcallback {
		public void callback(String title, String url, String duration,
				Date published);

		public void setCount(int attributeCount);
	}

	public void parseFeed(InputStream inputStream, Itemcallback itemcallback)
			throws XmlPullParserException, IOException, ParseException {

		this.itemcallback = itemcallback;
		itemcallback.setCount(205); // Figure out how to get the correct count
									// from the XML-file
		XmlPullParser parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(inputStream, null);
		parser.nextTag();

		readFeed(parser);

	}

	private void readFeed(XmlPullParser parser) throws XmlPullParserException,
			IOException, ParseException {

		parser.require(XmlPullParser.START_TAG, "", "rss");

		parser.nextTag();

		while (parser.next() != XmlPullParser.END_DOCUMENT) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if (name.equals("item")) {
				readEntry(parser);
			}

		}
	}

	private void readEntry(XmlPullParser parser) throws XmlPullParserException,
			IOException, ParseException {
		parser.require(XmlPullParser.START_TAG, "", "item");
		String title = null;
		String url = null;
		Date date = null;
		String duration = null;

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("title")) {
				title = readTitle(parser);
			} else if (name.equals("media:content")) {
				url = getMediaReadUrl(parser);
			} else if (name.equals("pubDate")) {
				date = getDate(parser);
			} else if (name.equals("itunes:duration")) {
				duration = getDuration(parser);
			} else {
				skip(parser);
			}
		}

		// For every episode item found in the feedburner xml file call the
		// callers callback with title and url so it
		// can creates the internal models for each episode.
		itemcallback.callback(title, url, duration, date);

	}

	private String getDuration(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, "", "itunes:duration");
		String duration = readText(parser);
		parser.require(XmlPullParser.END_TAG, "", "itunes:duration");
		return duration;
	}

	private Date getDate(XmlPullParser parser) throws XmlPullParserException,
			IOException, ParseException {
		parser.require(XmlPullParser.START_TAG, "", "pubDate");
		Date duration = readDate(parser);
		parser.require(XmlPullParser.END_TAG, "", "pubDate");
		return duration;
	}

	private String getMediaReadUrl(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		String url = null;
		parser.require(XmlPullParser.START_TAG, "", "media:content");
		String tag = parser.getName();
		if (tag.equals("media:content")) {
			url = parser.getAttributeValue(null, "url");
			parser.nextTag();
		}

		parser.require(XmlPullParser.END_TAG, "", "media:content");

		return url;
	}

	private String readTitle(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, "", "title");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, "", "title");
		return title;
	}

	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private Date readDate(XmlPullParser parser) throws XmlPullParserException,
			IOException, ParseException {
		Date result = null;
		if (parser.next() == XmlPullParser.TEXT) {
			result = dateFormat.parse(parser.getText());
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}
