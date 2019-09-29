package uren.com.colorgarden.main.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import uren.com.colorgarden.BaseFragment;
import uren.com.colorgarden.DbHelper.DefaultThemeList;
import uren.com.colorgarden.DbHelper.ThemeDbHelper;
import uren.com.colorgarden.R;
import uren.com.colorgarden.factory.MyDialogFactory;
import uren.com.colorgarden.main.Adapters.ThemeListAdapter;
import uren.com.colorgarden.model.OnRecycleViewItemClickListener;
import uren.com.colorgarden.model.bean.ThemeBean;
import uren.com.colorgarden.util.CommentUtil;
import uren.com.colorgarden.util.ListAnimationUtil;
import uren.com.colorgarden.util.SNSUtil;

public class ThemeFragment extends BaseFragment {

    private View mView;

    @BindView(R.id.theme_list)
    RecyclerView listView;
    @BindView(R.id.floating)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.llSettings)
    RelativeLayout llSettings;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navViewLayout)
    NavigationView navViewLayout;

    List<ThemeBean.Theme> themelist;
    private ThemeListAdapter adapter;
    private AlphaInAnimationAdapter alphaAdapter;
    LinearLayoutManager layoutManager;
    private boolean mDrawerState;
    private MyDialogFactory myDialogFactory;
    private ThemeDbHelper themeDbHelper;

    public ThemeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_theme_list, container, false);
            ButterKnife.bind(this, mView);
            initViews();
            addListeners();
        }
        return mView;
    }

    private void initViews() {
        myDialogFactory = new MyDialogFactory(getContext());
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        themeDbHelper = new ThemeDbHelper(getContext());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) floatingActionButton.getLayoutParams();
            p.setMargins(0, 0, 0, 0); // get rid of margins since shadow area is now the margin
            floatingActionButton.setLayoutParams(p);
        }
    }

    private void addListeners() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listTop();
            }
        });

        drawerLayout.addDrawerListener(new ActionBarDrawerToggle(getActivity(),
                drawerLayout,
                null,
                0,
                0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDrawerState = false;
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerState = true;
            }
        });

        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSettings.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));

                if (mDrawerState) {
                    drawerLayout.closeDrawer(Gravity.START);
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        });

        navViewLayout.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.shareApp:
                        drawerLayout.closeDrawer(Gravity.START);
                        SNSUtil.shareApp(getContext());
                        break;
                    case R.id.rateUs:
                        drawerLayout.closeDrawer(Gravity.START);
                        CommentUtil.commentApp(getContext());
                        break;
                    case R.id.aboutApp:
                        drawerLayout.closeDrawer(Gravity.START);
                        myDialogFactory.showAboutDialog();
                        break;
                    default:
                        break;
                }

                return false;
            }
        });
    }

    private void listTop() {
        listView.smoothScrollToPosition(0);
    }

    private void fillThemeList() {
        themelist = new ArrayList<ThemeBean.Theme>();
        if (themeDbHelper.isAnyItem()) {
            themelist.addAll(themeDbHelper.getAllThemeItems());
        } else {
            themeDbHelper.addAllDataToDB(DefaultThemeList.getThemeList(getContext()));
            themelist.addAll(DefaultThemeList.getThemeList(getContext()));
        }
        addListListener();
    }

    private void addListListener() {
        adapter = new ThemeListAdapter(getActivity(), themelist);
        adapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void recycleViewItemClickListener(View view, int i) {
                gotoSingleThemeFragment(i);
            }
        });
        alphaAdapter = ListAnimationUtil.addAlphaAnim(adapter);
        listView.setAdapter(alphaAdapter);
    }

    private void gotoSingleThemeFragment(final int i) {
        if (mFragmentNavigation != null)
            mFragmentNavigation.pushFragment(new SingleThemeFragment(themelist.get(i)));
    }

    @Override
    public void onResume() {
        super.onResume();
        fillThemeList();
    }
}
