package se.slashat.slashapp.async;

/**
 * Created by nicklas on 10/6/13.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import se.slashat.slashapp.adapter.AbstractArrayAdapter;

//TODO: Move away all the caching internal code and merge to separate class!

public class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private int position;
    private AbstractArrayAdapter.ImageAsyncHolder holder;

    public interface ImageCacheCallback {
        void addToCache(String url, Bitmap bitmap);
    }

    private WeakReference<ImageView> imageView;
    private boolean cancel;
    private ProgressBar progressBar;
    private ImageCacheCallback imageCacheCallback;
    private WeakReference<ImageView> imageViewThumbnail;

    public LoadImageAsyncTask(int position, ImageView imageView, AbstractArrayAdapter.ImageAsyncHolder holder, ImageView imageViewThumbnail, ProgressBar progressBar,
                              ImageCacheCallback imageCacheCallback) {
        this.position = position;
        this.holder = holder;
        this.imageViewThumbnail = new WeakReference<ImageView>(imageViewThumbnail);
        this.progressBar = progressBar;
        this.imageCacheCallback = imageCacheCallback;
        this.imageView = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = null;
        try {
            loadDataFromStream(baos, params);
            bais = cacheToByteArrayStream(baos); // To avoid reading the same
            // data twice from the
            // network.
            final BitmapFactory.Options options = calculateSampleSize(bais);
            bais.reset();
            bitmap = BitmapFactory.decodeStream(bais, null, options);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            try {
                bais.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageCacheCallback.addToCache(params[0], bitmap);
        return bitmap;
    }

    private BitmapFactory.Options calculateSampleSize(
            ByteArrayInputStream byteArrayInputStream) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(byteArrayInputStream, null, options);

        options.inSampleSize = calculateInSampleSize(options, imageViewThumbnail.get()
                .getWidth(), imageViewThumbnail.get().getHeight());
        options.inJustDecodeBounds = false;

        return options;
    }

    private ByteArrayInputStream cacheToByteArrayStream(
            ByteArrayOutputStream baos) {
        ByteArrayInputStream byteArrayInputStream;
        byteArrayInputStream = new ByteArrayInputStream(baos.toByteArray());
        byteArrayInputStream.markSupported();
        return byteArrayInputStream;
    }

    private void loadDataFromStream(ByteArrayOutputStream baos,
                                    String... params) throws MalformedURLException {
        InputStream is = null;
        URL url = new URL(params[0]);
        try {
            is = url.openConnection().getInputStream();
            byte[] buffer = new byte[2048];
            int read = 0;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (holder.position == position) {
            if (imageViewThumbnail.get() != null){
                progressBar.setVisibility(View.GONE);
                imageViewThumbnail.get().setImageBitmap(result);
                imageViewThumbnail.get().setScaleType(ScaleType.FIT_XY);
                holder.loadImageAsyncTask = null;
            }
        }
    }

    public void cancel() {
        cancel = true;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}