package uren.com.colorgarden.util;

import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import uren.com.colorgarden.listener.OnDeleteListener;
import uren.com.colorgarden.main.TimestampComparator;
import uren.com.colorgarden.model.bean.LocalImageBean;

public class FileUtils {

    public static List<LocalImageBean> obtainLocalImages() {
        List<LocalImageBean> localImageBeans = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().getPath() + "/ColorGarden";
        File f = new File(path);
        if (f != null && f.listFiles() != null) {
            File file[] = f.listFiles();
            for (int i = 0; i < file.length; i++) {
                if (numberFileName(file[i].getName())) {
                    localImageBeans.add(new LocalImageBean(file[i].getName(), path + "/" + file[i].getName(), DateTimeUtil.formatTimeStamp(file[i].lastModified()), file[i].lastModified(), getDropboxIMGSize(file[i])));
                    L.d("Files", path + "/" + file[i].getName() + ", " + file[i].lastModified());
                }
            }
        }
        TimestampComparator timeComparator = new TimestampComparator();
        Collections.sort(localImageBeans, timeComparator);
        return localImageBeans;
    }

    private static boolean numberFileName(String name) {
        String formatName = name.replace(".png", "").replace(".jpg", "");
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(formatName).matches();
    }

    public static boolean deleteFile(String url) {
        String c = url.replace("file://", "");
        File file = new File(url.replace("file://", ""));

        if (file != null) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }

    public static void deleteAllPaints(OnDeleteListener onDeleteListener) {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/ColorGarden");
        if (dir.isDirectory()) {
            String[] children = dir.list();

            if(children != null && children.length !=0) {
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
                dir.delete();
                onDeleteListener.OnSuccess();
            }
        }
    }

    private static float getDropboxIMGSize(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        float imageHeight = options.outHeight;
        float imageWidth = options.outWidth;
        return imageWidth / imageHeight;
    }
}
