package com.quadible.mymvp.uiElement;

import com.quadible.mvp.UiElement;
import com.quadible.mymvp.presenter.TimerPresenter;

/**
 * Created by v.tsitsonis on 23/4/2018.
 */

public interface TimerElement extends UiElement<TimerPresenter> {

    void setTime(int seconds);

}
