package uren.com.colorgarden.main;


import java.util.Comparator;

import uren.com.colorgarden.model.bean.LocalImageBean;

public class TimestampComparator implements Comparator<LocalImageBean> {
    @Override
    public int compare(LocalImageBean localImageBean, LocalImageBean t1) {
        long diff = t1.getLastModTimeStamp() - localImageBean.getLastModTimeStamp();
        if (diff > 0)
            return 1;
        if (diff < 0)
            return -1;
        else
            return 0;
    }
}
