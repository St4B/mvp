/**
 * Copyright 2017 Quadible Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quadible.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

/**
 * <p>
 *     Implement Mvp pattern to Dialog Fragments.
 * </p>
 */
public abstract class BaseMvpDialogFragment <U extends UiElement<P>, P extends Presenter<U>>
        extends DialogFragment implements UiElement<P> {

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
        mMvpDelegation.destroy();
    }

    @Override
    public abstract P createPresenter();

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

}
