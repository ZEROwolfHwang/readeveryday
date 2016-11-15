package com.sineom.thinkday.utils;

import android.util.Log;

import rx.Subscriber;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/11/11 14:26
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2016/11/11
 * @updataDes ${描述更新内容}
 */

public abstract class RxSubscriber extends Subscriber {
    public final String TAG = "RxSubscriber";

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, e.getMessage());
    }

    @Override
    public void onNext(Object o) {
        _onNext(o);
    }

    protected abstract void _onNext(Object o);
}
