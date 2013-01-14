package se.slashat.slashat.service;

import se.slashat.slashat.model.Episode;

public class ArchiveService {

	
	public static Episode[] getEpisodes(){
		
		Episode[] episodes = new Episode[10];
		
		episodes[0] = new Episode("STOP PLAY",000,null); // Temporary until buttons are implemented.
		episodes[1] = new Episode("Vi CES och hörs!",199,"http://www.slashat.se/feed/podcast/slashat-avsnitt-199.mp3");
		episodes[2] = new Episode("Feber!",198,"http://www.slashat.se/feed/podcast/slashat-avsnitt-198.mp3");
		episodes[3] = new Episode("Årsshow 2012!",197,"http://www.slashat.se/feed/podcast/slashat-avsnitt-197.mp3");
		episodes[4] = new Episode("So long, and thanks for all the fish!",196,"http://www.slashat.se/feed/podcast/slashat-avsnitt-196.mp3");
		episodes[5] = new Episode("Trendat!",195,"http://www.slashat.se/feed/podcast/slashat-avsnitt-195.mp3");
		episodes[6] = new Episode("T-tjötar!",194,"http://www.slashat.se/feed/podcast/slashat-avsnitt-194.mp3");
		episodes[7] = new Episode("Jeppe Gangnam Style!",193,"http://www.slashat.se/feed/podcast/slashat-avsnitt-193.mp3");
		episodes[8] = new Episode("#decembergate!",192,"http://www.slashat.se/feed/podcast/slashat-avsnitt-192.mp3");
		episodes[9] = new Episode("McAffarlane!",191,"http://www.slashat.se/feed/podcast/slashat-avsnitt-191.mp3");		
		
		return episodes;
		
	}
}
