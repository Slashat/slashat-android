package se.slashat.slashapp.fragments.live;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.List;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.LiveFullscreenActivity;
import se.slashat.slashapp.R;
import se.slashat.slashapp.androidservice.EpisodePlayer;
import se.slashat.slashapp.model.LiveEvent;
import se.slashat.slashapp.service.BambuserService;
import se.slashat.slashapp.service.GoogleCalendarService;
import se.slashat.slashapp.util.Strings;

/**
 * Created by nicklas on 7/2/13.
 */

// @TODO Decide which layout to use. (same as iOS app can be enabled by removing comments here and in the fragment layout)
// @TODO Handle rotation


public class CountdownFragment extends Fragment {
    private TextView days;
    private TextView hours;
    private TextView minutes;
    private TextView seconds;
    private LiveEvent liveEvent;
    private CountDownTimer countDownTimer;
    private Button liveViewButton;
    private TextView countdownText;
    private TextView eventTitle;
    private TextView eventDescription;
    private View eventTitleHeader;
    private Button liveListenButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.countdown_fragment, null);

        /*days = (TextView) view.findViewById(R.id.days);
        hours = (TextView) view.findViewById(R.id.hours);
        minutes = (TextView) view.findViewById(R.id.minutes);
        seconds = (TextView) view.findViewById(R.id.seconds);*/

        return view;

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        liveViewButton = (Button) view.findViewById(R.id.live_view_button);
        liveListenButton = (Button) view.findViewById(R.id.live_listen_button);
        countdownText = (TextView) view.findViewById(R.id.countdowntext);
        eventTitle = (TextView) view.findViewById(R.id.liveeventtitle);
        eventDescription = (TextView) view.findViewById(R.id.liveeventdescription);
        eventTitleHeader = view.findViewById(R.id.eventtitleheader);


        liveViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LiveFullscreenActivity.class);
                startActivity(intent);
            }
        });

        liveListenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EpisodePlayer.getEpisodePlayer().playLiveStream();
            }
        });

        BambuserService.getLiveStream(new Callback<String>() {
            @Override
            public void call(String result) {
                if (Strings.isNullOrEmpty(result)) { // If no live stream is found at Bambuser start the countdown.
                    setOffAir();
                } else {
                    setOnAir();
                }
            }
        });
    }

    private void setOffAir() {
        GoogleCalendarService googleCalendarService = new GoogleCalendarService();
        googleCalendarService.getCalendarEntries("3om4bg9o7rdij1vuo7of48n910", new Callback<List<LiveEvent>>() {
            @Override
            public void call(final List<LiveEvent> result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!result.isEmpty()) {
                            LiveEvent liveEvent = result.get(0);

                            final DateTime start = liveEvent.getStart();


                /* Enable this for the iOS-app-style counter.
                PeriodType periodType = PeriodType.dayTime().withMillisRemoved();
                Period period = new Period(now, start, periodType);

                days.setText(String.valueOf(period.getDays()));
                hours.setText(String.valueOf(period.getHours()));
                minutes.setText(String.valueOf(period.getMinutes()));
                seconds.setText(String.valueOf(period.getSeconds()));*/

                            eventTitle.setText(liveEvent.getSummary());
                            eventDescription.setText(liveEvent.getDescription());

                            final PeriodFormatter formatter = new PeriodFormatterBuilder().appendDays().appendSeparator(":").printZeroAlways().minimumPrintedDigits(2).appendHours().appendSeparator(":")
                                    .appendMinutes().appendSeparator(":").appendSeconds().toFormatter();


                            final DateTime now = new DateTime();
                            Period period = new Period(now, start);
                            countdownText.setText(formatter.print(period.normalizedStandard()));

                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }

                            countDownTimer = new CountDownTimer(start.getMillis() - now.getMillis(), 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                    final DateTime now = new DateTime();
                                    Period period = new Period(now, start);

                                    countdownText.setText(formatter.print(period.normalizedStandard()));
                                }

                                @Override
                                public void onFinish() {
                                    setOnAir();
                                }
                            };
                            countDownTimer.start();
                        }
                    }
                });

            }
        });
    }

    private void setOnAir() {
        liveViewButton.setVisibility(View.VISIBLE);
        liveListenButton.setVisibility(View.VISIBLE);
        eventTitleHeader.setVisibility(View.GONE);
        eventDescription.setVisibility(View.GONE);
        countdownText.setText(getString(R.string.now_live));
        countdownText.setTextColor(Color.RED);
    }
}
