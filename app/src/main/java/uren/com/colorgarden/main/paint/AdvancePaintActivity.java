package uren.com.colorgarden.main.paint;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import uren.com.colorgarden.BaseActivity;
import uren.com.colorgarden.MyApplication;
import uren.com.colorgarden.R;
import uren.com.colorgarden.factory.MyDialogFactory;
import uren.com.colorgarden.listener.OnAddWordsSuccessListener;
import uren.com.colorgarden.listener.OnChangeBorderListener;
import uren.com.colorgarden.model.SaveImageAsyn;
import uren.com.colorgarden.util.ShareImageUtil;
import uren.com.colorgarden.view.DragedTextView;
import uren.com.colorgarden.view.ImageButton_define;
import uren.com.colorgarden.view.MyProgressDialog;

public class AdvancePaintActivity extends BaseActivity {

    public static int Offest = MyApplication.screenWidth / 40;
    @BindView(R.id.addwords)
    ImageButton_define addwords;
    @BindView(R.id.addvoice)
    Button addvoice;
    @BindView(R.id.current_image)
    ImageView currentImage;
    @BindView(R.id.share)
    ImageButton_define share;
    @BindView(R.id.repaint)
    ImageButton_define repaint;
    @BindView(R.id.paintview)
    FrameLayout paintview;
    String imageUri;
    MyDialogFactory myDialogFactory;
    @BindView(R.id.addborder)
    ImageButton_define addborder;
    @BindView(R.id.border)
    ImageView border;
    @BindView(R.id.cancel)
    Button cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        setContentView(R.layout.activity_paint_advance);
        ButterKnife.bind(this);
        myDialogFactory = new MyDialogFactory(AdvancePaintActivity.this);
        imageUri = getIntent().getStringExtra("imagepath");
        currentImage.setImageBitmap(BitmapFactory.decodeFile(imageUri));
        addwords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addwordsDialog();
            }
        });
        addborder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBorderDialog();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDrawable();
            }
        });
        repaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repaintPictureDialog();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWindows() {

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = MyApplication.screenWidth;
        this.getWindow().setAttributes(params);
    }

    private void repaintPictureDialog() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialogFactory.dismissDialog();
                setResult(MyApplication.RepaintResult);
                finish();
            }
        };
        myDialogFactory.showRepaintDialog(onClickListener);
    }

    private void shareDrawable() {
        paintview.setDrawingCacheEnabled(true);
        paintview.destroyDrawingCache();
        paintview.buildDrawingCache();
        MyProgressDialog.show(AdvancePaintActivity.this, null, getString(R.string.savingimage));
        SaveImageAsyn saveImageAsyn = new SaveImageAsyn(AdvancePaintActivity.this);
        saveImageAsyn.execute(paintview.getDrawingCache(), MyApplication.SHAREWORK);
        saveImageAsyn.setOnSaveSuccessListener(new SaveImageAsyn.OnSaveFinishListener() {
            @Override
            public void onSaveFinish(String path) {
                MyProgressDialog.DismissDialog();
                if (path == null) {
                    Toast.makeText(AdvancePaintActivity.this, getString(R.string.saveFailed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdvancePaintActivity.this, getString(R.string.saveSuccess) + path, Toast.LENGTH_SHORT).show();
                    ShareImageUtil.getInstance(AdvancePaintActivity.this).shareImg(path);
                }
            }
        });
    }

    private void addwordsDialog() {
        OnAddWordsSuccessListener addwordssuccess = new OnAddWordsSuccessListener() {
            @Override
            public void addWordsSuccess(DragedTextView dragedTextView) {
                ((ViewGroup) currentImage.getParent()).addView(dragedTextView);
            }
        };
        myDialogFactory.showAddWordsDialog(addwordssuccess);
    }

    private void addBorderDialog() {
        OnChangeBorderListener addborderlistener = new OnChangeBorderListener() {
            @Override
            public void changeBorder(int drawableid, int pt, int pd, int pl, int pr) {
                if (drawableid != 0) {
                    border.setBackgroundResource(drawableid);
                    currentImage.setPadding(pl, pt, pr, pd);
                    currentImage.requestLayout();
                }
                paintview.requestLayout();
            }
        };
        myDialogFactory.showAddBorderDialog(addborderlistener);
    }
}
