package com.sineom.thinkday.view;

import android.app.Fragment;

import com.sineom.thinkday.BaseActivity;

public class MainActivity extends BaseActivity {

//    @BindView(R.id.text)
//    TextView mText;

    @Override
    public Fragment createFragment() {

        return new MainFragment();
    }

}
