package se.slashat.slashat.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;

import edu.emory.mathcs.backport.java.util.Collections;

import se.slashat.slashat.model.LiveEvent;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

public class CalendarService {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ENGLISH);

	public static Collection<LiveEvent> getLiveEvents() {
		dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
		try {
			URL icalUrl = new URL("https://www.google.com/calendar/ical/3om4bg9o7rdij1vuo7of48n910%40group.calendar.google.com/public/basic.ics");
			InputStream stream = icalUrl.openStream();
			CalendarBuilder calendarBuilder = new CalendarBuilder();
			Calendar calendar = calendarBuilder.build(stream);

			List<LiveEvent> events = new ArrayList<LiveEvent>();

			//TODO replace with propertyname getters instead of loops.
			for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
				Component component = (Component) i.next();
				if (component.getName().equals("VEVENT")) {
					Date start = null;
					Date end = null;
					String summary = "";
					for (Iterator j = component.getProperties().iterator(); j.hasNext();) {
						try {
							Property property = (Property) j.next();
							if (property.getName().equals("DTSTART")) {
								
								start = dateFormat.parse(property.getValue().replace("Z", ""));
								System.out.println(property.getValue().replace("Z", ""));
							}
							if (property.getName().equals("DTEND")) {
								end = dateFormat.parse(property.getValue().replace("Z", ""));
								System.out.println(property.getValue().replace("Z", ""));
							}
							if (property.getName().equals("SUMMARY")) {
								summary = property.getValue();
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					events.add(new LiveEvent(start, end, summary));
				}
			}
			System.out.println("Before:");
			for (LiveEvent liveEvent : events) {
				System.out.println(liveEvent.getStart());
			}
			
			
			Collections.sort(events);
			Collections.reverse(events);
			System.out.println("After:");
			for (LiveEvent liveEvent : events) {
				System.out.println(liveEvent.getStart());
			}
			return Collections.unmodifiableList(events);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

}
