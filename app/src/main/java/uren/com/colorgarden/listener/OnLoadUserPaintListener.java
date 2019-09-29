package uren.com.colorgarden.listener;

import java.util.List;

import uren.com.colorgarden.model.bean.LocalImageBean;

public interface OnLoadUserPaintListener {
    void loadUserPaintFinished(List<LocalImageBean> list);
}
