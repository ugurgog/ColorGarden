package uren.com.colorgarden.main.Adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uren.com.colorgarden.MyApplication;
import uren.com.colorgarden.R;
import uren.com.colorgarden.model.AsynImageLoader;
import uren.com.colorgarden.model.GridViewActivityModel;
import uren.com.colorgarden.model.OnRecycleViewItemClickListener;
import uren.com.colorgarden.model.bean.PictureBean;
import uren.com.colorgarden.model.bean.ThemeBean;

import static uren.com.colorgarden.Constants.StringConstants.DEF_TRUE;

public class ThemeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    List<ThemeBean.Theme> themelist;
    OnRecycleViewItemClickListener onRecycleViewItemClickListener;
    private final int TYPE_ITEM = 1;

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        this.onRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    public ThemeListAdapter(Context context, List<ThemeBean.Theme> themelist) {
        this.context = context;
        this.themelist = themelist;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.view_list_item, parent, false);
        return new VHItem(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ThemeBean.Theme theme = themelist.get(position);
        ((VHItem) holder).setData(theme, position);
    }

    @Override
    public int getItemCount() {
        return themelist.size();
    }

    public List<ThemeBean.Theme> getList() {
        return themelist;
    }

    class VHItem extends RecyclerView.ViewHolder {

        public ImageView image;
        public ImageView lock;
        public TextView name;
        public View parent;
        ThemeBean.Theme theme;
        int position;

        public VHItem(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            lock = itemView.findViewById(R.id.lock);
            name = (TextView) itemView.findViewById(R.id.name);
            parent = itemView;

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRecycleViewItemClickListener != null)
                        onRecycleViewItemClickListener.recycleViewItemClickListener(parent, position);
                }
            });
        }

        public void setData(ThemeBean.Theme theme, int position) {
            this.theme = theme;
            this.position = position;
            name.setText(themelist.get(position).getName());


            try {
                List<PictureBean.Picture> pictureBeans = getSecretGardenBean(new ArrayList<>(Arrays.asList(context.getAssets().list(theme.getPath()))));
                String url = MyApplication.DEFAULTASSETLOCATION + theme.getPath() + "/"+ pictureBeans.get(0).getUri();
                AsynImageLoader.showImageAsynWithAllCacheOpen(image, url);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(theme.getIsLocked().equals(DEF_TRUE)){
                Glide.with(context)
                    .load(R.drawable.icon_locked)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(lock);
                lock.setColorFilter(context.getResources().getColor(R.color.Red), PorterDuff.Mode.SRC_IN);
            }else {
                Glide.with(context)
                        .load(R.drawable.icon_unlocked)
                        .apply(RequestOptions.fitCenterTransform())
                        .into(lock);
                lock.setColorFilter(context.getResources().getColor(R.color.Green), PorterDuff.Mode.SRC_IN);
            }


        }

        private List<PictureBean.Picture> getSecretGardenBean(ArrayList<String> secretGarden) {
            List<PictureBean.Picture> pictureBeans = new ArrayList<>();
            for (String s : secretGarden) {
                pictureBeans.add(new PictureBean.Picture(s));
            }
            return pictureBeans;
        }
    }
}
