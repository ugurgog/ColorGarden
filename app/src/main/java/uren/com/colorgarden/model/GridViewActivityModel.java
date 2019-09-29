package uren.com.colorgarden.model;

import android.os.Environment;
import android.widget.ImageView;

import java.io.File;



public class GridViewActivityModel {
    private String root = Environment.getExternalStorageDirectory().getPath() + "/ColorGarden/";
    private static GridViewActivityModel gridViewActivityModel;

    private GridViewActivityModel() {
    }

    public static GridViewActivityModel getInstance() {
        if (gridViewActivityModel == null) {
            gridViewActivityModel = new GridViewActivityModel();
        }
        return gridViewActivityModel;
    }

    public void showGridLocalImageAsyn(ImageView imageView, String url) {
        if (hasSavedFile(url)) {
            AsynImageLoader.showImageAsynWithoutCache(imageView, "file:/" + root + url.hashCode() + ".png");
        } else {
            AsynImageLoader.showImageAsynWithAllCacheOpen(imageView, url);
        }
    }

    private boolean hasSavedFile(String s) {
        int hashCode = s.hashCode();
        String path = root + hashCode + ".png";
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
