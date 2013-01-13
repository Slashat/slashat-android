package se.slashat.slashat.fragment;

import se.slashat.slashat.R;
import se.slashat.slashat.adapter.EpisodeAdapter;
import se.slashat.slashat.service.ArchiveService;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ArchiveFragment extends ListFragment {
    private EpisodeAdapter adapter;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	/**ArchiveService.getEpisodes();
    	String url = "http://www.slashat.se/feed/podcast/slashat-avsnitt-199.mp3"; // your URL here
    	MediaPlayer mediaPlayer = new MediaPlayer();
    	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    	try {
			mediaPlayer.setDataSource(url);
	    	mediaPlayer.prepare(); // might take long! (for buffering, etc)
	    	mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}**/
        return inflater.inflate(R.layout.fragment_archive, container, false);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	if (adapter == null) {
			adapter = new EpisodeAdapter(getActivity(),
					R.layout.archive_item_row, ArchiveService.getEpisodes());
		}
		setListAdapter(adapter);
    }
}