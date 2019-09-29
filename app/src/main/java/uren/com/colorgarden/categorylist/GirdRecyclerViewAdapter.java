package uren.com.colorgarden.categorylist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

import uren.com.colorgarden.MyApplication;
import uren.com.colorgarden.R;
import uren.com.colorgarden.model.GridViewActivityModel;
import uren.com.colorgarden.model.OnRecycleViewItemClickListener;
import uren.com.colorgarden.model.bean.PictureBean;
import uren.com.colorgarden.util.L;

public class GirdRecyclerViewAdapter extends RecyclerView.Adapter<GirdRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<PictureBean.Picture> pictureBeans;
    private String path;

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        this.onRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    private OnRecycleViewItemClickListener onRecycleViewItemClickListener;

    public GirdRecyclerViewAdapter(Context context, List<PictureBean.Picture> pictureBeans, String path) {
        mContext = context;
        this.path = path;
        this.pictureBeans = pictureBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.view_gridview_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String url;
        //holder.image.setLayoutParams(new FrameLayout.LayoutParams(MyApplication.getScreenWidth(mContext) / 2, MyApplication.getScreenWidth(mContext) / 2));
        url = MyApplication.DEFAULTASSETLOCATION + path + "/"+ pictureBeans.get(position).getUri();
        GridViewActivityModel.getInstance().showGridLocalImageAsyn(holder.image, url);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecycleViewItemClickListener != null)
                    onRecycleViewItemClickListener.recycleViewItemClickListener(holder.image, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pictureBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.gridImage);
        }
    }

}
