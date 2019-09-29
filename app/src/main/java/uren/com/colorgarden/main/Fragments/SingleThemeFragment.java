package uren.com.colorgarden.main.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.billingclient.api.Purchase;
import com.android.vending.billing.IInAppBillingService;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uren.com.colorgarden.BaseFragment;
//import uren.com.colorgarden.BillingModule.BillingManager;
import uren.com.colorgarden.DbHelper.ThemeDbHelper;
import uren.com.colorgarden.MyApplication;
import uren.com.colorgarden.R;
import uren.com.colorgarden.categorylist.GirdRecyclerViewAdapter;
import uren.com.colorgarden.factory.MyDialogFactory;
import uren.com.colorgarden.listener.YesNoDialogBoxCallback;
import uren.com.colorgarden.main.paint.PaintActivity;
import uren.com.colorgarden.model.OnRecycleViewItemClickListener;
import uren.com.colorgarden.model.bean.PictureBean;
import uren.com.colorgarden.model.bean.ThemeBean;
import uren.com.colorgarden.util.SNSUtil;

import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_ANIMALS_1;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_ANIMALS_2;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_ANIMALS_3;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_ANIMALS_4;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_MANDALA_1;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_MANDALA_2;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_MANDALA_3;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_MANDALA_4;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_SEC_GARDEN_2;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_SEC_GARDEN_3;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_SEC_GARDEN_4;
import static uren.com.colorgarden.Constants.StringConstants.APP_PRODUCT_ID;
import static uren.com.colorgarden.Constants.StringConstants.DEF_BASE_64_ENCODED_PUBLIC_KEY;
import static uren.com.colorgarden.Constants.StringConstants.DEF_FALSE;
import static uren.com.colorgarden.Constants.StringConstants.DEF_TRUE;

@SuppressLint("ValidFragment")
public class SingleThemeFragment extends BaseFragment implements BillingProcessor.IBillingHandler {

    private View mView;

    @BindView(R.id.detail_gird)
    RecyclerView recyclerView;
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.imgvLock)
    ImageView imgvLock;

    List<PictureBean.Picture> pictureBeans;
    GirdRecyclerViewAdapter gridViewAdapter;
    private ThemeBean.Theme theme;
    private boolean isSavedFile = false;
    private String imageName;
    private MyDialogFactory myDialogFactory;
    private ThemeDbHelper themeDbHelper;
    //private BillingManager billingManager;
    private BillingProcessor billingProcessor;

    private static final String TAG = "SingleThemeFragment";

    private static final int MARGING_GRID = 2;
    private String root = Environment.getExternalStorageDirectory().getPath() + "/ColorGarden/";
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 355;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE1 = 356;

    private static final int ACT_RESULT_APP_SHARE = 234;

    public SingleThemeFragment(ThemeBean.Theme theme) {
        this.theme = theme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.activity_gridview, container, false);
            ButterKnife.bind(this, mView);
            initViews();
            addListeners();
        }
        return mView;
    }

    private void initViews() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(addItemDecoration());
        recyclerView.setLayoutManager(layoutManager);
        toolbar_title.setText(theme.getName());
        myDialogFactory = new MyDialogFactory(getContext());
        themeDbHelper = new ThemeDbHelper(getContext());
        billingProcessor = new BillingProcessor(getContext(), DEF_BASE_64_ENCODED_PUBLIC_KEY, this);
        billingProcessor.initialize();
        setLockImage();
    }

    private void setLockImage() {
        if (theme.getIsLocked().equals(DEF_TRUE)) {
            Glide.with(getContext())
                    .load(R.drawable.icon_locked)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(imgvLock);
        } else if (theme.getIsLocked().equals(DEF_FALSE)) {
            Glide.with(getContext())
                    .load(R.drawable.icon_unlocked)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(imgvLock);
        }
    }

    private void addListeners() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        imgvLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (theme.getIsLocked().equals(DEF_TRUE)) {
                    checkLocked();
                }
            }
        });
    }

    private void checkStoragePermission() {
        try {
            pictureBeans = getSecretGardenBean(new ArrayList<>(Arrays.asList(getActivity().getAssets().list(theme.getPath()))));
            for (PictureBean.Picture picture : pictureBeans) {
                String url = MyApplication.DEFAULTASSETLOCATION + theme.getPath() + "/" + picture.getUri();
                if (hasSavedFile(url)) {
                    isSavedFile = true;
                    break;
                }
            }

            if (isSavedFile) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED)
                    loadLocaldata();
                else
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_WRITE_EXTERNAL_STORAGE);
            } else
                loadLocaldata();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadLocaldata();
            }
        } else if (requestCode == PERMISSION_WRITE_EXTERNAL_STORAGE1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotoPaintActivity(imageName);
            }
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

    private void loadLocaldata() {
        if (pictureBeans == null) {
            Toast.makeText(getContext(), getString(R.string.loadfailed), Toast.LENGTH_SHORT).show();
        } else {
            showGrid();
        }
    }

    private List<PictureBean.Picture> getSecretGardenBean(ArrayList<String> secretGarden) {
        List<PictureBean.Picture> pictureBeans = new ArrayList<>();
        for (String s : secretGarden) {
            pictureBeans.add(new PictureBean.Picture(s));
        }
        return pictureBeans;
    }

    private RecyclerView.ItemDecoration addItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = MARGING_GRID;
                outRect.right = MARGING_GRID;
                outRect.bottom = MARGING_GRID;
                if (parent.getChildLayoutPosition(view) >= 0 && parent.getChildLayoutPosition(view) <= 3) {
                    outRect.top = MARGING_GRID;
                }
            }
        };
    }

    private void gotoPaintActivity(String s) {
        Intent intent = new Intent(getContext(), PaintActivity.class);
        intent.putExtra(MyApplication.BIGPIC, MyApplication.DEFAULTASSETLOCATION + theme.getPath() + "/" + s);
        startActivity(intent);
    }

    private void showGrid() {
        gridViewAdapter = new GirdRecyclerViewAdapter(getContext(), pictureBeans, theme.getPath());
        gridViewAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void recycleViewItemClickListener(View view, int i) {

                if (theme.getIsLocked().equals(DEF_TRUE)) {
                    checkLocked();
                } else {
                    imageName = pictureBeans.get(i).getUri();

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED) {
                        gotoPaintActivity(imageName);
                    } else
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE1);
                }
            }
        });
        recyclerView.setAdapter(gridViewAdapter);
    }

    private void checkLocked() {
        switch (theme.getCategoryId()) {
            case DEF_CAT_SEC_GARDEN_2:
            case DEF_CAT_SEC_GARDEN_3:
                openThemeWithAppShare();
                break;
            case DEF_CAT_SEC_GARDEN_4:
            case DEF_CAT_ANIMALS_1:
            case DEF_CAT_ANIMALS_2:
            case DEF_CAT_ANIMALS_3:
            case DEF_CAT_ANIMALS_4:
            case DEF_CAT_MANDALA_1:
            case DEF_CAT_MANDALA_2:
            case DEF_CAT_MANDALA_3:
            case DEF_CAT_MANDALA_4:
                buyTheme();
                break;
            default:
                break;
        }
    }

    private void openThemeWithAppShare() {
        myDialogFactory.yesNoDialogBox(new YesNoDialogBoxCallback() {
            @Override
            public void yesClick() {
                startActivityForResult(SNSUtil.getShareAppIntent(getContext()), ACT_RESULT_APP_SHARE);
            }

            @Override
            public void noClick() {

            }
        }, getString(R.string.open_sec_garden_text));
    }

    private void buyTheme() {
        myDialogFactory.yesNoDialogBox(new YesNoDialogBoxCallback() {
            @Override
            public void yesClick() {

                try {
                    boolean isOneTimePurchaseSupported = billingProcessor.isOneTimePurchaseSupported();
                    if (isOneTimePurchaseSupported) {
                        billingProcessor.purchase(getActivity(), theme.getProductId());
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void noClick() {

            }
        }, getString(R.string.buy_theme_text));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ACT_RESULT_APP_SHARE) {
                unlockTheme();
            }
        }

        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void unlockTheme(){
        int result = themeDbHelper.updateThemeItem(DEF_FALSE, theme.getCategoryId());

        if(result == 1){
            theme.setIsLocked(DEF_FALSE);
            Toast.makeText(getContext(), getContext().getString(R.string.theme_lock_opened), Toast.LENGTH_SHORT).show();
            setLockImage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkStoragePermission();
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        /*Log.i(TAG, "onProductPurchased");
        Log.i(TAG, "  ->productId:" + productId);
        Log.i(TAG, "  ->details:" + details);*/
        unlockTheme();

    }

    @Override
    public void onPurchaseHistoryRestored() {
        //Log.i(TAG, "onPurchaseHistoryRestored");
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        /*Log.i(TAG, "onPurchaseHistoryRestored");
        Log.i(TAG, "  ->errorCode:" + errorCode);
        Log.i(TAG, "  ->error:" + error);*/
    }

    @Override
    public void onBillingInitialized() {
        //Log.i(TAG, "onPurchaseHistoryRestored");
    }

    @Override
    public void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
        super.onDestroy();
    }
}
