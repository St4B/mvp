package com.quadible.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by v.tsitsonis on 14/12/2017.
 */

public abstract class BaseMvpFragmentActivity<U extends UiElement<P>, P extends Presenter<U>>
        extends FragmentActivity implements UiElement<P>{

    private Mvp<U, P> mMvpDelegation = new Mvp<>();

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) mMvpDelegation.setUp((U)this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMvpDelegation.onUiVisibilityChanged((U)this, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMvpDelegation.onUiVisibilityChanged((U)this, false);
        if (isFinishing()) mMvpDelegation.destroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMvpDelegation.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMvpDelegation.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public abstract P createPresenter();

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

}
