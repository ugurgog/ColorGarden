package uren.com.colorgarden.main.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uren.com.colorgarden.R;
import uren.com.colorgarden.factory.MyDialogFactory;
import uren.com.colorgarden.listener.OnBeanReturn;
import uren.com.colorgarden.listener.OnDeleteListener;
import uren.com.colorgarden.model.AsynImageLoader;
import uren.com.colorgarden.model.bean.LocalImageBean;
import uren.com.colorgarden.util.ShapeUtil;

public class LocalPaintAdapter extends RecyclerView.Adapter<LocalPaintAdapter.ViewHolder> {
    List<LocalImageBean> localImageListBean;
    Context context;
    private OnBeanReturn onBeanReturn;
    private OnDeleteListener onDeleteListener;

    public LocalPaintAdapter(Context context, List<LocalImageBean> localImageListBean, OnBeanReturn onBeanReturn,
                             OnDeleteListener onDeleteListener) {
        if (localImageListBean == null) {
            localImageListBean = new ArrayList<>();
        }
        this.localImageListBean = localImageListBean;
        this.context = context;
        this.onBeanReturn = onBeanReturn;
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.view_localimage_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        LocalImageBean localImageBean = localImageListBean.get(position);
        holder.setData(localImageBean, position);
    }

    public void setItems(List<LocalImageBean> localImageListBean){
        this.localImageListBean = localImageListBean;
    }

    public void removeItemProcess(int position){
        localImageListBean.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return localImageListBean.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ImageView trash;
        TextView lastModifyTime;
        LocalImageBean localImageBean;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            lastModifyTime = (TextView) itemView.findViewById(R.id.lastModify);
            trash = itemView.findViewById(R.id.trash);

            trash.setBackground(ShapeUtil.getShape(context.getResources().getColor(R.color.transparentBlack),
                    0, GradientDrawable.OVAL, 50, 0));

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBeanReturn.onReturn("file://" + localImageBean.getImageUrl(), localImageBean.getImageName());
                }
            });

            trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MyDialogFactory myDialogFactory = new MyDialogFactory(context);
                    myDialogFactory.showDeleteOnePaintDialog(new OnDeleteListener() {
                        @Override
                        public void OnSuccess() {

                            boolean deleted = false;
                            File file = null;
                            File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/ColorGarden");
                            if (dir.isDirectory()) {
                                file = new File(dir, localImageBean.getImageName());
                                deleted = file.delete();
                            }

                            if(deleted){
                                updateGallery(file);
                                removeItemProcess(position);
                                onDeleteListener.OnSuccess();
                            }
                        }

                        @Override
                        public void OnFailed() {

                        }
                    });
                }
            });
        }

        public void updateGallery(File file) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            context.sendBroadcast(intent);
        }

        public void setData(LocalImageBean localImageBean, int position){
            this.localImageBean = localImageBean;
            this.position = position;
            AsynImageLoader.showImageAsynWithoutCache(image, "file://" + localImageBean.getImageUrl());
            lastModifyTime.setText(context.getString(R.string.lastModifty) + " " + localImageListBean.get(position).getLastModDate());
        }
    }
}
