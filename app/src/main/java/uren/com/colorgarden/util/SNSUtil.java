package uren.com.colorgarden.util;

import android.content.Context;
import android.content.Intent;

import uren.com.colorgarden.R;
import uren.com.colorgarden.listener.OnUnLockImageSuccessListener;


public class SNSUtil {

    public static void shareApp(Context context) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = context.getString(R.string.sharecontent);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.pleaseselect)));
    }

    public static void shareApp(Context context, OnUnLockImageSuccessListener onUnLockImageSuccessListener) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = context.getString(R.string.sharecontent);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.pleaseselect)));
        onUnLockImageSuccessListener.UnlockImageSuccess();
    }

    public static Intent getShareAppIntent(Context context) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = context.getString(R.string.sharecontent);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        return sharingIntent;
    }
}
