package com.sineom.thinkday.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/11/11 14:18
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2016/11/11
 * @updataDes ${描述更新内容}
 */

public class RxHolder<T> {
    public static <T> Observable.Transformer<T, T> io_main() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
