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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * <p>
 *     Implement Mvp pattern to Fragments.
 * </p>
 */
public abstract class BaseMvpFragment <U extends UiElement<P>, P extends Presenter<U>>
        extends Fragment implements UiElement<P> {

    private Mvp<U, P> mMvpDelegation = new Mvp<>();

    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMvpDelegation.setUp((U)this, savedInstanceState);
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
