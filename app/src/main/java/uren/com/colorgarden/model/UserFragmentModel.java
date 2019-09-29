package uren.com.colorgarden.model;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import uren.com.colorgarden.listener.OnLoadUserPaintListener;
import uren.com.colorgarden.model.bean.LocalImageBean;
import uren.com.colorgarden.util.FileUtils;
import uren.com.colorgarden.util.L;


public class UserFragmentModel {
    private static UserFragmentModel ourInstance;
    Context context;
    AsyncTask asyncTask;

    public static UserFragmentModel getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new UserFragmentModel(context);
        }
        return ourInstance;
    }

    private UserFragmentModel(Context context) {
        this.context = context;
    }

    public void obtainLocalPaintList(OnLoadUserPaintListener onLoadUserPaintListener) {
        asyncTask = new LoadLocalPaintsAsyn();
        asyncTask.execute(onLoadUserPaintListener);
    }

    private class LoadLocalPaintsAsyn extends AsyncTask {
        OnLoadUserPaintListener onLoadUserPaintListener;

        @Override
        protected Object doInBackground(Object[] objects) {
            L.e("load local data");
            onLoadUserPaintListener = (OnLoadUserPaintListener) objects[0];
            return FileUtils.obtainLocalImages();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            L.e(o.toString());
            if (onLoadUserPaintListener != null) {
                onLoadUserPaintListener.loadUserPaintFinished((List<LocalImageBean>) o);
            }
        }
    }
}
