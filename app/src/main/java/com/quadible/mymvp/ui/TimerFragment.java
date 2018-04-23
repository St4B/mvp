package com.quadible.mymvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quadible.mvp.BaseMvpFragment;
import com.quadible.mymvp.R;
import com.quadible.mymvp.presenter.TimerPresenter;
import com.quadible.mymvp.uiElement.TimerElement;

/**
 * Created by v.tsitsonis on 23/4/2018.
 */

public class TimerFragment
        extends BaseMvpFragment<TimerElement, TimerPresenter> implements TimerElement {

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
