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
package com.quadible.mymvp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quadible.mvp.BaseMvpFragment;
import com.quadible.mymvp.R;
import com.quadible.mymvp.presenter.TimerPresenter;
import com.quadible.mymvp.uiElement.TimerElement;

/**
 * <p>
 *     Example of {@link BaseMvpFragment}. Whenever the fragment it is created for the first time
 *     it calls the {@link TimerPresenter#startTimer()} to start calculating the seconds that passed
 *     after its creation.
 * </p>
 */
public class TimerFragment
        extends BaseMvpFragment<TimerElement, TimerPresenter> implements TimerElement {

    public static final String TAG = TimerFragment.class.getName();

    private TextView mTime;

    @Override
    public TimerPresenter createPresenter() {
        return new TimerPresenter();
    }

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();
        mPresenter.startTimer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTime = view.findViewById(R.id.tvTimer);
    }

    @Override
    public void setTime(int seconds) {
        mTime.setText(Integer.toString(seconds));
    }
}
