package se.slashat.slashapp.util;

import android.content.Context;

/**
 * Created by nicklas on 6/30/14.
 */
public class Conversion {

    private static float density;

    public static void init(Context context){
        density = context.getResources().getDisplayMetrics().density;
    }

    public static int getPx(int dimensionDp) {
        return (int) (dimensionDp * density + 0.5f);
    }
}
