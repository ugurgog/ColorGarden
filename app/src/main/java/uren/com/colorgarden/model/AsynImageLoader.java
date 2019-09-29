package uren.com.colorgarden.model;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import uren.com.colorgarden.util.ImageLoaderUtil;

public class AsynImageLoader {

    private static Drawable getBlankDrawable() {
        Drawable drawable;
        int color = Color.argb(255, 255, 255, 255);
        drawable = new ColorDrawable(color);
        return drawable;
    }

    public static void showImageAsynWithoutCache(ImageView imageView, String url) {
        if (url.contains("file://") || url.contains("drawable://")) {
            ImageLoaderUtil.getInstance().displayImage(url, imageView,
                    ImageLoaderUtil.getNoCacheOptions(getBlankDrawable()),
                    new ImageLoaderUtil.AnimateFirstDisplayListener());
            return;
        }
        ImageLoaderUtil.getInstance().displayImage(url, imageView,
                ImageLoaderUtil.getNoCacheOptions(),
                new ImageLoaderUtil.AnimateFirstDisplayListener());
    }

    public static void showImageAsynWithAllCacheOpen(ImageView imageView, String url) {
        if (url.contains("file://") || url.contains("drawable://")) {
            ImageLoaderUtil.getInstance().displayImage(url, imageView,
                    ImageLoaderUtil.getOpenAllCacheOptions(getBlankDrawable()),
                    new ImageLoaderUtil.AnimateFirstDisplayListener());
            return;
        }
        ImageLoaderUtil.getInstance().displayImage(url, imageView,
                ImageLoaderUtil.getOpenAllCacheOptions(),
                new ImageLoaderUtil.AnimateFirstDisplayListener());
    }

    public static void showLagreImageAsynWithAllCacheOpen(ImageView imageView, String url, ImageLoadingListener listener) {
        ImageLoaderUtil.getInstance().displayImage(url, imageView,
                ImageLoaderUtil.DetailImageOptions(),
                listener);
    }

    public static void showLagreImageAsynWithNoCacheOpen(ImageView imageView, String url, ImageLoadingListener listener) {
        ImageLoaderUtil.getInstance().displayImage(url, imageView,
                ImageLoaderUtil.DetailImageOptionsNoCache(),
                listener);
    }
}
