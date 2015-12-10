package com.benio.mpost.network;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.benio.mpost.interf.ImageListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 图片加载
 * Created by benio on 2015/10/15.
 */
public class ImageLoader implements ImageListener {

    private RequestManager mManager;

    private final static ImageLoader sImageLoader = new ImageLoader();

    @Override
    public void load(ImageView imageView, String string) {
        load(imageView,string,null);
    }

    public void load(ImageView imageView, String string, @DrawableRes int placeholder) {
        load(imageView, string, placeholder, null);
    }

    public void load(ImageView imageView, @DrawableRes int res) {
        load(imageView, res, null);
    }

    public void load(ImageView imageView, String string, BitmapTransformation transformation) {
        if (transformation != null) {
            mManager.load(string).transform(transformation).into(imageView);
        } else {
            mManager.load(string).into(imageView);
        }
    }

    public void load(ImageView imageView, @DrawableRes int res, BitmapTransformation transformation) {
        if (transformation != null) {
            mManager.load(res).crossFade().fitCenter().transform(transformation).into(imageView);
        } else {
            mManager.load(res).crossFade().fitCenter().into(imageView);
        }
    }

    public void load(ImageView imageView, String string, @DrawableRes int placeholder, BitmapTransformation transformation) {
        if (transformation != null) {
            mManager.load(string).placeholder(placeholder).crossFade().fitCenter().transform(transformation).into(imageView);
        } else {
            mManager.load(string).placeholder(placeholder).crossFade().fitCenter().into(imageView);
        }
    }

    public static ImageLoader getInstance(Fragment fragment) {
        sImageLoader.mManager = Glide.with(fragment);
        return sImageLoader;
    }

    public static ImageLoader getInstance(android.app.Fragment fragment) {
        sImageLoader.mManager = Glide.with(fragment);
        return sImageLoader;
    }

    public static ImageLoader getInstance(Activity activity) {
        sImageLoader.mManager = Glide.with(activity);
        return sImageLoader;
    }

    public static ImageLoader getInstance(FragmentActivity activity) {
        sImageLoader.mManager = Glide.with(activity);
        return sImageLoader;
    }

    public static ImageLoader getInstance(Context context) {
        sImageLoader.mManager = Glide.with(context);
        return sImageLoader;
    }
}
