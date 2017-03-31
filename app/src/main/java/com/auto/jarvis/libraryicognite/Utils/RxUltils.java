package com.auto.jarvis.libraryicognite.Utils;

import android.os.Handler;
import android.view.View;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by HaVH on 3/26/17.
 */

public class RxUltils {

    public static <T> Observable.Transformer<T, T> withLoading(final Handler handler, final View loadingView) {
        return tObservable -> tObservable.observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> handler.post(() -> loadingView.setVisibility(View.VISIBLE)))
                .doOnTerminate(() -> handler.post(() -> loadingView.setVisibility(View.INVISIBLE)));
    }

    public static Observable<Boolean> checkConnectToServer() {
        return Observable.create(subscriber -> {
            Boolean isOnline = NetworkUtils.isOnline();
            subscriber.onNext(isOnline);
            subscriber.onCompleted();
        });
    }
}
