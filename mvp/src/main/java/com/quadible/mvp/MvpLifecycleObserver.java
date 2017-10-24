package com.quadible.mvp;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;

import static android.arch.lifecycle.Lifecycle.Event.ON_PAUSE;
import static android.arch.lifecycle.Lifecycle.Event.ON_RESUME;

/**
 *
 * fixme
 *
 * Created by v.tsitsonis on 9/10/2017.
 */

public class MvpLifecycleObserver<U extends UiElement<P>, P extends Presenter<U>>
        implements LifecycleObserver {

    private Mvp<U, P> mMvpDelegation = new Mvp<>();

    private U mUi;

    public MvpLifecycleObserver(U ui){
        mMvpDelegation.setUp(ui);
        mUi = ui;
    }

    @OnLifecycleEvent(ON_RESUME)
    private void onViewBecomesVisible() {
        mMvpDelegation.onUiVisibilityChanged(mUi, true);
    }

    @OnLifecycleEvent(ON_PAUSE)
    private void onViewBecomesInvisible() {
        mMvpDelegation.onUiVisibilityChanged(mUi, false);
    }

    protected void onSaveInstanceState(Bundle outState) {
        mMvpDelegation.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mMvpDelegation.onRestoreInstanceState(savedInstanceState);
    }

    public void clearResources() {
        mMvpDelegation.destroy();
    }

}
