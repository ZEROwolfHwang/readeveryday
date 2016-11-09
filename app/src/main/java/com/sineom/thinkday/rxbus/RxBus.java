package com.sineom.thinkday.rxbus;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {
    private static volatile RxBus mDefaultInstance;

    private RxBus() {
    }

    public static RxBus getmDefaultInstance() {
        if (mDefaultInstance == null)
            synchronized (RxBus.class) {
                if (mDefaultInstance == null)
                    mDefaultInstance = new RxBus();
            }
        return mDefaultInstance;
    }

    private final Subject _bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        _bus.onNext(o);
    }

    public <T> Observable<T> objectObservable(final Class<T> eventType) {
        return _bus.filter(new Func1<Object, Boolean>() {
            @Override
            public final Boolean call(Object t) {
                return eventType.isInstance(t);
            }
        }).cast(eventType);
    }


    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}
