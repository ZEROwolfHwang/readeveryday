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

import java.util.ArrayList;

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
    private Fragment mFragment;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    public DrawerLayout mDrawerLayout;

    private int getLayoutResId() {
        return R.layout.activity_content;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        initFragment();
        initToolbar();
        initLeftDrawer();
    }

    private void initLeftDrawer() {
        final ArrayList<String> datas = new ArrayList<>();
        datas.add("每日推荐");
        datas.add("社会一面");
        datas.add("读点好书");
        mRvLeftDrawer.setLayoutManager(new LinearLayoutManager(this));
        mRvLeftDrawer.addItemDecoration(new LeftDrawerItemDecoration(36));
        mRvLeftDrawer.setAdapter(new LeftDrawerAdapter(this, datas, new LeftDrawerAdapter.CLick() {
            @Override
            public void onItemClick(int position) {
                toolbar.setTitle(datas.get(position));
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
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                else
                    mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void initFragment() {
        mManager = getFragmentManager();
        mFragment = mManager.findFragmentById(R.id.fragment_content);
        if (mFragment == null) {
            mFragment = new ArticleFragment();
            mManager.beginTransaction()
                    .add(R.id.fragment_content, mFragment, GLobalData.ARTICLE)
                    .commit();
        }
    }
}
