package uren.com.colorgarden.main.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uren.com.colorgarden.BaseFragment;
import uren.com.colorgarden.MyApplication;
import uren.com.colorgarden.R;
import uren.com.colorgarden.factory.MyDialogFactory;
import uren.com.colorgarden.listener.OnBeanReturn;
import uren.com.colorgarden.listener.OnDeleteListener;
import uren.com.colorgarden.listener.OnLoadUserPaintListener;
import uren.com.colorgarden.main.Adapters.LocalPaintAdapter;
import uren.com.colorgarden.main.paint.PaintActivity;
import uren.com.colorgarden.model.UserFragmentModel;
import uren.com.colorgarden.model.bean.LocalImageBean;

public class MyPaintsFragment extends BaseFragment {

    private View mView;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.emptylay_paintlist)
    LinearLayout emptylayPaintlist;
    @BindView(R.id.imgDown)
    ImageView imgDown;
    @BindView(R.id.floating)
    FloatingActionButton floating;

    List<LocalImageBean> localImageBeans;
    MyDialogFactory myDialogFactory;
    //RecyclerView.Adapter adapter;
    private LocalPaintAdapter localPaintAdapter;

    private String imageUrl;
    private String imageName;

    private static final int MARGING_GRID = 2;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 195;

    public MyPaintsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, mView);
        initViews();
        addEvents();
        return mView;
    }

    private void initViews() {
        localImageBeans = new ArrayList<>();
        floating.setVisibility(View.GONE);
        emptylayPaintlist.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) floating.getLayoutParams();
            p.setMargins(0, 0, 0, 0); // get rid of margins since shadow area is now the margin
            floating.setLayoutParams(p);
        }
        myDialogFactory = new MyDialogFactory(getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerview.addItemDecoration(addItemDecoration());
        recyclerview.setLayoutManager(layoutManager);
        loadLocalPaints();
    }

    private void addEvents() {
        imgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), imgDown);
                popupMenu.inflate(R.menu.menu_my_paints);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteAllPaints:
                                MyDialogFactory myDialogFactory = new MyDialogFactory(getContext());
                                myDialogFactory.showDeletePaintsDialog(new OnDeleteListener() {
                                    @Override
                                    public void OnSuccess() {
                                        Toast.makeText(getContext(), getContext().getString(R.string.deleteAllPaintsSuccess), Toast.LENGTH_SHORT).show();
                                        if (localImageBeans != null)
                                            localImageBeans.clear();
                                        if (localPaintAdapter != null)
                                            localPaintAdapter.notifyDataSetChanged();
                                        setUIVisibility();
                                    }

                                    @Override
                                    public void OnFailed() {

                                    }
                                });
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listTop();
            }
        });
    }

    private void listTop() {
        recyclerview.smoothScrollToPosition(0);
    }

    private void loadLocalPaints() {

        localPaintAdapter = new LocalPaintAdapter(getActivity(), localImageBeans, new OnBeanReturn() {
            @Override
            public void onReturn(String url, String name) {
                imageName = name;
                imageUrl = url;
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    gotoPaintActivity(imageUrl, imageName);
                } else
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            }
        }, new OnDeleteListener() {
            @Override
            public void OnSuccess() {
                setUIVisibility();
            }

            @Override
            public void OnFailed() {

            }
        });

        recyclerview.setAdapter(localPaintAdapter);

        OnLoadUserPaintListener onLoadUserPaintListener = new OnLoadUserPaintListener() {
            @Override
            public void loadUserPaintFinished(List<LocalImageBean> list) {
                if (list != null) {
                    localImageBeans = list;
                    setUIVisibility();
                    localPaintAdapter.setItems(localImageBeans);
                    if (localPaintAdapter != null)
                        localPaintAdapter.notifyDataSetChanged();
                }
            }
        };
        UserFragmentModel.getInstance(getActivity()).obtainLocalPaintList(onLoadUserPaintListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotoPaintActivity(imageUrl, imageName);
            }
        }
    }

    public void gotoPaintActivity(String uri, String filename) {
        Intent intent = new Intent(getContext(), PaintActivity.class);
        intent.putExtra(MyApplication.BIGPICFROMUSER, uri);
        int formatName = Integer.valueOf(filename.replace(".png", ""));
        intent.putExtra(MyApplication.BIGPICFROMUSERPAINTNAME, formatName);
        startActivity(intent);
    }

    private void setUIVisibility() {
        if (localImageBeans.size() > 0) {
            floating.setVisibility(View.VISIBLE);
            emptylayPaintlist.setVisibility(View.GONE);
        } else {
            emptylayPaintlist.setVisibility(View.VISIBLE);
            floating.setVisibility(View.GONE);
        }
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

    @Override
    public void onResume() {
        super.onResume();

        if (localPaintAdapter != null) {
            loadLocalPaints();
            localPaintAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
