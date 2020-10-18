/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 9:51 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 7:56 AM
 *
 */

package id.co.santridev.simplechat.core.utils.extension;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import rx.subjects.BehaviorSubject;

/**
 * Created on : August 18, 2016
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public abstract class QiscusPresenter<V extends QiscusPresenter.View> {
    private final BehaviorSubject<QiscusPresenterEvent> lifecycleSubject = BehaviorSubject.create();

    protected V view;

    public QiscusPresenter(V view) {
        this.view = view;
        lifecycleSubject.onNext(QiscusPresenterEvent.CREATE);
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, QiscusPresenterEvent.DETACH);
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(QiscusPresenterEvent qiscusPresenterEvent) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, qiscusPresenterEvent);
    }

    public void detachView() {
        view = null;
        lifecycleSubject.onNext(QiscusPresenterEvent.DETACH);
    }

    public interface View {
        void showError(String errorMessage);

        void showLoading();

        void dismissLoading();
    }
}
