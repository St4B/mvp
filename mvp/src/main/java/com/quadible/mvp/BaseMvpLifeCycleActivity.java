package com.quadible.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * fixme
 * Created by v.tsitsonis on 24/10/2017.
 */

public abstract class BaseMvpLifeCycleActivity<U extends UiElement<P>, P extends Presenter<U>>
        extends AppCompatActivity implements UiElement<P>{

    private MvpLifecycleObserver mvpLifecycleObserver;

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpLifecycleObserver = new MvpLifecycleObserver(this);
        getLifecycle().addObserver(mvpLifecycleObserver);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvpLifecycleObserver.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mvpLifecycleObserver.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) mvpLifecycleObserver.clearResources();
    }

    @Override
    public abstract P createPresenter();

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

}
