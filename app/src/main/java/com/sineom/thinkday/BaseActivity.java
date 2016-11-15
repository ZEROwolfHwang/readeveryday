package com.sineom.thinkday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sineom.thinkday.present.GLobalData;
import com.sineom.thinkday.utils.NetWorkUtils;
import com.sineom.thinkday.view.ArticleFragment;
import com.sineom.thinkday.view.RandomArticleFragment;
import com.sineom.thinkday.view.BookFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-29
 * Time: 13:42
 * DESIC
 */
public class BaseActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    public NavigationView mNavigationView;
    public FragmentManager mManager;
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
        mManager = getSupportFragmentManager();
        if (NetWorkUtils.isNetworkConnected(this)) {
            initFragment(new ArticleFragment(), GLobalData.ARTICLE);
        } else {
            Toast.makeText(this, "请连接网络后再使用", Toast.LENGTH_SHORT).show();
        }
        initToolbar();
        initLeftDrawer();
    }

    private void initLeftDrawer() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (!NetWorkUtils.isNetworkConnected(BaseActivity.this)) {
                    Toast.makeText(BaseActivity.this, "请连接网络后再使用", Toast.LENGTH_SHORT).show();
                    return true;
                }
                switch (item.getItemId()) {
                    case R.id.navigation_article:
                        toolbar.setTitle("每日一文");
                        mFragment = mManager.findFragmentByTag(GLobalData.ARTICLE);
                        if (mFragment == null)
                            mFragment = new ArticleFragment();
                        initFragment(mFragment, GLobalData.ARTICLE);
                        break;
                    case R.id.navigation_randomArticle:
                        toolbar.setTitle("精彩推荐");
                        mFragment = mManager.findFragmentByTag(GLobalData.RANDOMARTICLE);
                        if (mFragment == null)
                            mFragment = new RandomArticleFragment();
                        initFragment(mFragment, GLobalData.RANDOMARTICLE);
                        break;
                    case R.id.navigation_society:
                        toolbar.setTitle("好书推荐");
                        mFragment = mManager.findFragmentByTag(GLobalData.SOCIETYSICE);
                        if (mFragment == null)
                            mFragment = new BookFragment();
                        initFragment(mFragment, GLobalData.SOCIETYSICE);
                        break;
                }
                openOrClose();
                return true;
            }
        });
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


    public void initFragment(Fragment fragment, String tag) {
        mManager.beginTransaction()
                .replace(R.id.fragment_content, fragment, tag)
                .commit();
    }

    public void fragmentHideAndShow(Fragment hideFrag, Fragment showFrag) {
        if (!showFrag.isAdded()) {
            mManager.beginTransaction()
                    .hide(hideFrag)
                    .add(R.id.fragment_content, showFrag)
                    .show(showFrag)
                    .commit();
        } else {
            mManager.beginTransaction()
                    .hide(hideFrag)
                    .show(showFrag)
                    .commit();
        }
    }

}
