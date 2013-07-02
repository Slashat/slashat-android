package se.slashat.slashapp.fragments.live;

import android.content.Intent;
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
import se.slashat.slashapp.model.LiveEvent;
import se.slashat.slashapp.service.GoogleCalendarService;

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


        GoogleCalendarService googleCalendarService = new GoogleCalendarService();
        googleCalendarService.getCalendarEntries("3om4bg9o7rdij1vuo7of48n910", new Callback<List<LiveEvent>>() {
            @Override
            public void call(List<LiveEvent> result) {
                for (LiveEvent liveEvent : result) {
                    System.out.println(liveEvent.getStart());
                    System.out.println(liveEvent.getEnd());
                    System.out.println(liveEvent.getSummary());
                }

                LiveEvent liveEvent = result.get(0);

                final DateTime start = liveEvent.getStart();
                final DateTime now = new DateTime();
                /*
                PeriodType periodType = PeriodType.dayTime().withMillisRemoved();
                Period period = new Period(now, start, periodType);

                days.setText(String.valueOf(period.getDays()));
                hours.setText(String.valueOf(period.getHours()));
                minutes.setText(String.valueOf(period.getMinutes()));
                seconds.setText(String.valueOf(period.getSeconds()));*/


                final TextView textView = (TextView) getView().findViewById(R.id.countdowntext);

                final PeriodFormatter formatter = new PeriodFormatterBuilder().appendDays().appendSeparator(":").printZeroAlways().minimumPrintedDigits(2).appendHours().appendSeparator(":")
                        .appendMinutes().appendSeparator(":").appendSeconds().toFormatter();

                countDownTimer = new CountDownTimer(start.getMillis() - now.getMillis(), 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                        final DateTime now = new DateTime();
                        Period period = new Period(now, start);

                        textView.setText(formatter.print(period.normalizedStandard()));
                    }

                    @Override
                    public void onFinish() {

                    }
                };
                countDownTimer.start();


                Button button = (Button) view.findViewById(R.id.livebutton);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), LiveFullscreenActivity.class);

                        startActivity(intent);
                    }
                });

            }
        });
    }
}
