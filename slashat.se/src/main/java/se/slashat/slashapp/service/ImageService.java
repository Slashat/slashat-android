package se.slashat.slashapp.service;

/**
 * Created by nicklas on 10/6/13.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

/*import com.jakewharton.disklrucache.DiskLruCache;
import com.jakewharton.disklrucache.DiskLruCache.Editor;
import com.jakewharton.disklrucache.DiskLruCache.Snapshot;*/

import se.slashat.slashapp.adapter.AbstractArrayAdapter;
import se.slashat.slashapp.async.LoadImageAsyncTask;

public class ImageService {

    private static LruCache<String, Bitmap> memoryCache;
    //private static DiskLruCache diskLruCache;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private static Object sync = new Object();

    private ImageService() {
    }

    public static void init(Context context) {
        if (memoryCache == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            memoryCache = new LruCache<String, Bitmap>(cacheSize);
        }

        /*if (diskLruCache == null) {
            File cacheDir = getDiskCacheDir(context, "tumbs");
            try {
                diskLruCache = DiskLruCache.open(cacheDir, APP_VERSION,
                        VALUE_COUNT, DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public static void populateImage(AbstractArrayAdapter.ImageAsyncHolder holder, String imageUrl, int position) {
        Bitmap bitmap = getBitmapFromMemCache(imageUrl);

        if (bitmap == null) {
            //bitmap = getBitmapFromDiskCache(imageUrl);
            if (bitmap != null) {
                addBitmapToMemoryCache(imageUrl, bitmap);
            }
            if (bitmap == null) {
                //if (holder.loadImageAsyncTask != null) {
                //    holder.loadImageAsyncTask.cancel();
                //}
                // holder.newsImage.setImageResource(R.drawable.ic_launcher);
                //holder.image.setImageBitmap(null);
                //holder.image.invalidate();
                holder.imageThumb.setImageBitmap(null);
                holder.imageThumb.invalidate();

                ImageView image = null;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    image = holder.image;
                }

                LoadImageAsyncTask loadImageAsyncTask = new LoadImageAsyncTask(
                        position, image, holder, holder.imageThumb, holder.progressBar,
                        new LoadImageAsyncTask.ImageCacheCallback() {
                            @Override
                            public void addToCache(String url, Bitmap bitmap) {
                                addBitmapToMemoryCache(url, bitmap);
                                //addBitmapToDiskCache(url, bitmap);
                            }
                        });
                holder.loadImageAsyncTask = loadImageAsyncTask;
                loadImageAsyncTask.execute(imageUrl);
            }
        }

        if (bitmap != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                //holder.image.setImageBitmap(bitmap);
            }
            holder.imageThumb.setImageBitmap(bitmap);
            holder.hasImage = true;
        }
    }


    private static Bitmap getBitmapFromMemCache(String url) {
        String key = md5(url);
        Log.d(ImageService.class.getName(), "getting image: " + key
                + " from memory cache");
        Bitmap bitmap = memoryCache.get(key);
        if (bitmap != null) {
            Log.d(ImageService.class.getName(), "image: " + key
                    + " read from memory cache");
        }
        return bitmap;
    }

    /*private static Bitmap getBitmapFromDiskCache(String url) {
        String key = md5(url);
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            Snapshot snapshot = diskLruCache.get(key);

            if (snapshot == null) {
                return null;
            }

            inputStream = snapshot.getInputStream(0);

            if (inputStream != null) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(
                        inputStream, 8192);
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d(NewsitemAdapter.class.getName(), "image: " + key
                + " read from disk cache");
        addBitmapToMemoryCache(key, bitmap);
        return bitmap;
    }*/

    public static void addBitmapToMemoryCache(String url, Bitmap bitmap) {
        if (url != null && bitmap != null) {
            String key = md5(url);
            Log.d(ImageService.class.getName(), "image: " + key
                    + " added to memory cache");
            memoryCache.put(key, bitmap);
        }
    }

   /* public static void addBitmapToDiskCache(String url, Bitmap bitmap) {
        if (url != null && bitmap != null) {
            synchronized (sync) {
                String key = md5(url);
                Editor editor = null;
                try {
                    editor = diskLruCache.edit(key);
                    if (editor == null) {
                        return;
                    }

                    if (writeBitmapToFile(bitmap, editor)) {
                        diskLruCache.flush();
                        editor.commit();
                        Log.d(NewsitemAdapter.class.getName(), "image: " + key
                                + " added to disk cache");
                    } else {
                        editor.abort();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        editor.abort();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }*/

    /*private static boolean writeBitmapToFile(Bitmap bitmap,
                                             DiskLruCache.Editor editor) throws IOException,
            FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(editor.newOutputStream(0), 8192);
            return bitmap.compress(CompressFormat.JPEG, 70, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }*/

    private static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    private static String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}