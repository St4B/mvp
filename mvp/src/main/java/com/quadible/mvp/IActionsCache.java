package com.quadible.mvp;

import com.quadible.mvp.Presenter.UiAction;

import java.util.ArrayList;

/**
 * Created by v.tsitsonis on 23/4/2018.
 */

public interface IActionsCache<A extends UiAction> {

    void saveActions(ArrayList<A> actions);

    ArrayList<A> restoreActions();

    void delete();

}
