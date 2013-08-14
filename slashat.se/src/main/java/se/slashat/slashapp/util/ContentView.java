package se.slashat.slashapp.util;

import android.os.Build;

import se.slashat.slashapp.R;

/**
 * Created by nicklas on 8/14/13.
 */
public class ContentView {

    public static int getContentViewCompat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.id.content
                : R.id.action_bar_activity_content;
    }
}
