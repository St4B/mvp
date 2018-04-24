package com.quadible.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * Created by v.tsitsonis on 24/10/2017.
 */

public abstract class BaseMvpAppCompatDialogFragment  <U extends UiElement<P>, P extends Presenter<U>>
        extends AppCompatDialogFragment implements UiElement<P> {

    private Mvp<U, P> mMvpDelegation = new Mvp<>();

    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) mMvpDelegation.setUp((U)this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMvpDelegation.onUiVisibilityChanged((U)this, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMvpDelegation.onUiVisibilityChanged((U)this, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMvpDelegation.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) mMvpDelegation.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity().isFinishing()) {
            mMvpDelegation.destroy();
        }
    }

    @Override
    public abstract P createPresenter();

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onPresenterCreated() {}

}
