package com.sineom.thinkday;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.sineom.thinkday.adapter.LeftDrawerAdapter;
import com.sineom.thinkday.adapter.LeftDrawerItemDecoration;
import com.sineom.thinkday.present.GLobalData;
import com.sineom.thinkday.view.ArticleFragment;
import com.sineom.thinkday.view.BookFragment;
import com.sineom.thinkday.view.SocietySide;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-29
 * Time: 13:42
 * DESIC
 */
public class BaseActivity extends AppCompatActivity {

    @BindView(R.id.rv_left_drawer)
    public RecyclerView mRvLeftDrawer;
    private FragmentManager mManager;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    public DrawerLayout mDrawerLayout;
    Fragment mFragment;

    private int getLayoutResId() {
        return R.layout.activity_content;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        initFragment(new ArticleFragment(), GLobalData.ARTICLE);
        initToolbar();
        initLeftDrawer();
    }

    private void initLeftDrawer() {
        final List<String> strings = Arrays.asList(getResources().getStringArray(R.array.leftItem));
        mRvLeftDrawer.setLayoutManager(new LinearLayoutManager(this));
        mRvLeftDrawer.addItemDecoration(new LeftDrawerItemDecoration(48));
        mRvLeftDrawer.setAdapter(new LeftDrawerAdapter(this, strings, new LeftDrawerAdapter.CLick() {
            @Override
            public void onItemClick(Object position) {
                toolbar.setTitle(strings.get((int) position));
                openOrClose();
                switch ((int) position) {
                    case 0:
                        mFragment = mManager.findFragmentByTag(GLobalData.ARTICLE);
                        if (mFragment == null)
                            mFragment = new ArticleFragment();
                        initFragment(mFragment, GLobalData.ARTICLE);
                        break;
                    case 1:
                        mFragment = mManager.findFragmentByTag(GLobalData.SOCIETYSICE);
                        if (mFragment == null)
                            mFragment = new SocietySide();
                        initFragment(mFragment, GLobalData.SOCIETYSICE);
                        break;
                    case 2:
                        mFragment = mManager.findFragmentByTag(GLobalData.BOOK);
                        if (mFragment == null)
                            mFragment = new BookFragment();
                        initFragment(mFragment, GLobalData.BOOK);
                        break;
                    default:
                        break;
                }
            }
        }));
    }


    private void initToolbar() {
        toolbar.setTitle("每日推荐");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        toggle.syncState();
        mDrawerLayout.addDrawerListener(toggle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrClose();
            }
        });
    }

    private void openOrClose() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        else
            mDrawerLayout.openDrawer(Gravity.LEFT);
    }


    private void initFragment(Fragment fragment, String tag) {
        mManager = getFragmentManager();
        mManager.beginTransaction()
                .replace(R.id.fragment_content, fragment, tag)
                .commit();
    }

}
