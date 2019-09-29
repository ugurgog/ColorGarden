package uren.com.colorgarden;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import uren.com.colorgarden.util.ImageLoaderUtil;
import uren.com.colorgarden.util.L;

public class MyApplication extends Application {

    //public static final String DEFAULTASSETLOCATION = "assets://SecretGarden/";
    public static final String DEFAULTASSETLOCATION = "assets://";

    public static final String MainUrl = "http://api.fingercoloring.com/pic/extAPI";
    public static final String ThemeDetailUrl = MainUrl + "/list"; // post categoryid
    public static final String ThemeThumbUrl = MainUrl + "/categorythumb?category=%d"; //get add categoryid + /category.png
    public static final String ImageThumbUrl = MainUrl + "/imageres?category=%d&image=t_%d"; //get add categoryid and imageid
    public static final String ImageLageUrl = MainUrl + "/imageres?category=%d&image=f_%d";  //get add categoryid and imageid
    public static final String UserLoginUrl = MainUrl + "/login";  //post add header token
    public static final String UserRegisterUrl = MainUrl + "/register";  //post type uid usericon gender location name
    public static final String THEMEID = "theme_id";
    public static final String THEMEPATH = "theme_path";
    public static final String BIGPIC = "bigpic";
    public static final String BIGPICFROMUSER = "bigpic_user";
    public static final String THEMENAME = "theme_name";
    public static final int PaintActivityRequest = 900;
    public static final int RepaintResult = 999;
    public static final String BIGPICFROMUSERPAINTNAME = "bigpic_user_name";


    public static int screenWidth;
    public static CharSequence SHAREWORK = "share_work";

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        screenWidth = getScreenWidth(this);
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(100 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoaderUtil.getInstance().init(config);
    }

    public static String getVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            L.e("VersionE", e.getMessage());
            e.printStackTrace();
            return "0";
        }

    }

    public static void restart(Context context) {

        if (context == null)
            return;
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if (context instanceof Activity)
            ((Activity) context).overridePendingTransition(0, 0);
    }
}
