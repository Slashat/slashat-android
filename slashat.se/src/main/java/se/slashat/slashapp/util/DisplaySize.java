package se.slashat.slashapp.util;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by nicklas on 6/28/13.
 */
public abstract class DisplaySize {

    public abstract int getWidth();

    public abstract int getHeight();

    public static DisplaySize getInstance(WindowManager w) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return new HoneycombDisplaySize(w);
        } else {
            return new PreHoneycombDisplaySize(w);
        }
    }

    public static class PreHoneycombDisplaySize extends DisplaySize {

        private Display d;

        public PreHoneycombDisplaySize(WindowManager w) {
            d = w.getDefaultDisplay();
        }

        @SuppressWarnings("deprecation")
        @Override
        public int getWidth() {
            return d.getWidth();
        }

        @SuppressWarnings("deprecation")
        @Override
        public int getHeight() {
            return d.getHeight();
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static class HoneycombDisplaySize extends DisplaySize {

        private Point size;

        public HoneycombDisplaySize(WindowManager w) {
            size = new Point();
            w.getDefaultDisplay().getSize(size);
        }

        @Override
        public int getWidth() {
            return size.x;
        }

        @Override
        public int getHeight() {
            return size.y;
        }

    }
}

