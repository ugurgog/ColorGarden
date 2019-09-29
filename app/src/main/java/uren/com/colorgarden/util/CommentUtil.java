package uren.com.colorgarden.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import uren.com.colorgarden.R;
import uren.com.colorgarden.factory.SharedPreferencesFactory;
import uren.com.colorgarden.listener.OnUnLockImageSuccessListener;

public class CommentUtil {
    public static void commentApp(Context context) {
        try {
            String mAddress = "market://details?id=" + context.getPackageName();
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            context.startActivity(marketIntent);
            SharedPreferencesFactory.saveBoolean(context, SharedPreferencesFactory.CommentEnableKey, false);
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.commentFailed), Toast.LENGTH_SHORT).show();
        }
    }

    public static void commentApp(Context context, OnUnLockImageSuccessListener onUnLockImageSuccessListener) {
        try {
            String mAddress = "market://details?id=" + context.getPackageName();
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            context.startActivity(marketIntent);
            SharedPreferencesFactory.saveBoolean(context, SharedPreferencesFactory.CommentEnableKey, false);
            onUnLockImageSuccessListener.UnlockImageSuccess();
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.commentFailed), Toast.LENGTH_SHORT).show();
        }
    }
}
