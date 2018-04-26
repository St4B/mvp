package com.quadible.mymvp.uiElement;

import com.quadible.mvp.UiElement;
import com.quadible.mymvp.presenter.TimerPresenter;

/**
 * <p>
 *     The actions that {@link TimerPresenter} can take (on {@link com.quadible.mymvp.ui.TimerFragment})
 * </p>
 */
public interface TimerElement extends UiElement<TimerPresenter> {

    void setTime(int seconds);

}
