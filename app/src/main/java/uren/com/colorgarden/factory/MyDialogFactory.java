package uren.com.colorgarden.factory;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import uren.com.colorgarden.MyApplication;
import uren.com.colorgarden.R;
import uren.com.colorgarden.listener.OnAddWordsSuccessListener;
import uren.com.colorgarden.listener.OnChangeBorderListener;
import uren.com.colorgarden.listener.OnDeleteListener;
import uren.com.colorgarden.listener.YesNoDialogBoxCallback;
import uren.com.colorgarden.util.CommentUtil;
import uren.com.colorgarden.util.DensityUtil;
import uren.com.colorgarden.util.FileUtils;
import uren.com.colorgarden.view.ColorPickerSeekBar;
import uren.com.colorgarden.view.MyDialogStyle;

public class MyDialogFactory extends MyDialogStyle {

    //just for add border
    int drawableid;

    public MyDialogFactory(Context context) {
        super(context);
    }

    public void FinishSaveImageDialog(View.OnClickListener savelistener, View.OnClickListener quitlistener) {
        showTwoButtonDialog(context.getString(R.string.quitorsave), context.getString(R.string.save), context.getString(R.string.quit), savelistener, quitlistener, true);
    }

    public void showAboutDialog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.version) + ":" + MyApplication.getVersion(context) + "\n\n");

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            buffer.append(pInfo.versionName + "\n");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        buffer.append(context.getString(R.string.aboutImage) + "\n\n");
        buffer.append(context.getString(R.string.email) + ":" + context.getString(R.string.authoremail) + "\n");

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        showOneButtonDialog(context.getString(R.string.app_name), buffer, context.getString(R.string.ok), listener, true);
    }


    public void showDeletePaintsDialog(final OnDeleteListener onDeleteListener) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.confirmDeleteAllPaints) + "\n");
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                FileUtils.deleteAllPaints(onDeleteListener);
            }
        };
        showTwoButtonDialog(buffer, context.getString(R.string.delete), context.getString(R.string.cancel), listener1, listener2, true);
    }

    public void showDeleteOnePaintDialog(final OnDeleteListener onDeleteListener) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.confirmOneDelete) + "\n");
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                onDeleteListener.OnSuccess();
            }
        };
        showTwoButtonDialog(buffer, context.getString(R.string.delete), context.getString(R.string.cancel), listener1, listener2, true);
    }


    public void yesNoDialogBox(final YesNoDialogBoxCallback yesNoDialogBoxCallback, String text) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(text + "\n");
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                yesNoDialogBoxCallback.yesClick();
            }
        };
        showTwoButtonDialog(buffer, context.getString(R.string.upperYes), context.getString(R.string.upperNo), listener1, listener2, true);
    }

    public void showCommentDialog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.commentDialogContent) + "\n");
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                CommentUtil.commentApp(context);
            }
        };
        showTwoButtonDialog(buffer, context.getString(R.string.gocomment), context.getString(R.string.nexttime), listener1, listener2, true);
    }

    public void showPaintFirstOpenDialog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.paintHint));
        showOnceTimesContentDialog(context.getString(R.string.welcomeusethis), buffer, SharedPreferencesFactory.PaintHint);
    }

    public void showPaintFirstOpenSaveDialog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.paintHint2));
        showOnceTimesContentDialog(context.getString(R.string.welcomeusethis), buffer, SharedPreferencesFactory.PaintHint2);
    }

    public void showAddWordsDialog(final OnAddWordsSuccessListener onAddWordsSuccessListener) {
        View layout = LayoutInflater.from(context).inflate(R.layout.view_addwords, null);
        final EditText editText = (EditText) layout.findViewById(R.id.addeditwords);
        RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.small:
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DragTextViewFactory.getInstance().SmallTextSize);
                        break;
                    case R.id.middle:
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DragTextViewFactory.getInstance().MiddleTextSize);
                        break;
                    case R.id.large:
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DragTextViewFactory.getInstance().BigTextSize);
                        break;
                    case R.id.huge:
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DragTextViewFactory.getInstance().HugeSize);
                        break;
                }
            }
        });
        ColorPickerSeekBar colorPicker = (ColorPickerSeekBar) layout.findViewById(R.id.seekcolorpicker);
        colorPicker.setOnColorSeekbarChangeListener(new ColorPickerSeekBar.OnColorSeekBarChangeListener() {
            @Override
            public void onColorChanged(SeekBar seekBar, int color, boolean b) {
                editText.setTextColor(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().trim().isEmpty()) {
                    dismissDialog();
                    onAddWordsSuccessListener.addWordsSuccess(DragTextViewFactory.getInstance().createUserWordTextView(context, editText.getText().toString(), editText.getCurrentTextColor(), (int) editText.getTextSize()));
                } else {
                    Toast.makeText(context, context.getString(R.string.nowords), Toast.LENGTH_SHORT).show();
                }
            }
        };
        showBlankDialog(context.getString(R.string.addtext), layout, listener);
    }

    public void showRepaintDialog(View.OnClickListener confirm) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.confirmRepaint) + "\n");
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        showTwoButtonDialog(buffer, context.getString(R.string.repaint), context.getString(R.string.cancel), confirm, listener2, true);
    }

    public void showAddBorderDialog(final OnChangeBorderListener onChangeBorderListener) {
        View layout = LayoutInflater.from(context).inflate(R.layout.view_addborder, null);
        final ImageView border1 = (ImageView) layout.findViewById(R.id.xiangkuang1);
        final ImageView border2 = (ImageView) layout.findViewById(R.id.xiangkuang2);
        drawableid = 1;
        final View.OnClickListener changeBorderOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == border1.getId()) {
                    border1.setBackgroundResource(R.drawable.maincolor_border_bg);
                    drawableid = 1;
                    border2.setBackgroundResource(0);
                } else {
                    border2.setBackgroundResource(R.drawable.maincolor_border_bg);
                    drawableid = 2;
                    border1.setBackgroundResource(0);
                }
            }
        };
        border1.setOnClickListener(changeBorderOnclickListener);
        border2.setOnClickListener(changeBorderOnclickListener);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                if (drawableid == 1) {
                    onChangeBorderListener.changeBorder(R.drawable.xiangkuang, DensityUtil.dip2px(context, 36), DensityUtil.dip2px(context, 36), DensityUtil.dip2px(context, 21), DensityUtil.dip2px(context, 21));
                } else {
                    onChangeBorderListener.changeBorder(R.drawable.xiangkuang2, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
                }
            }
        };
        showBlankDialog(context.getString(R.string.addborder), layout, listener);
    }

    public void showBuxianButtonClickDialog() {
        showOnceTimesContentDialog(context.getString(R.string.buxianfunction), context.getString(R.string.buxianfunctionhint), SharedPreferencesFactory.BuXianButtonClickDialogEnable);
    }

    public void showBuxianFirstPointSetDialog() {
        showOnceTimesContentDialog(context.getString(R.string.buxianfunction), context.getString(R.string.buxianfirstpointsethint), SharedPreferencesFactory.BuXianFirstPointDialogEnable);
    }

    public void showBuxianNextPointSetDialog() {
        showOnceTimesContentDialog(context.getString(R.string.buxianfunction), context.getString(R.string.buxiannextpointsethint), SharedPreferencesFactory.BuXianNextPointDialogEnable);
    }

    public void showPickColorHintDialog() {
        showOnceTimesContentDialog(context.getString(R.string.pickcolor), context.getString(R.string.pickcolorhint), SharedPreferencesFactory.PickColorDialogEnable);
    }

    private void showOnceTimesContentDialog(String title, CharSequence contentstr, final String whichDialog) {
        if (SharedPreferencesFactory.getBoolean(context, whichDialog, true)) {
            View layout = LayoutInflater.from(context).inflate(R.layout.view_dialog_with_checkbox, null);
            TextView content = (TextView) layout.findViewById(R.id.content);
            final CheckBox checkBox = (CheckBox) layout.findViewById(R.id.checkbox_dontshow);
            content.setText(contentstr);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        SharedPreferencesFactory.saveBoolean(context, whichDialog, false);
                    }
                    dismissDialog();
                }
            };
            showBlankDialog(title, layout, listener);
        }
    }

    public void showPaintHintDialog() {
        showPaintFirstOpenDialog();
        if (!dialog.isShowing()) {
            showPaintFirstOpenSaveDialog();
        }
    }

    public void showGradualHintDialog() {
        showOnceTimesContentDialog(context.getString(R.string.gradualModel), context.getString(R.string.gradualModelHint), SharedPreferencesFactory.GradualModel);
    }
}
