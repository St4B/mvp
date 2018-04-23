package com.quadible.mvp;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by v.tsitsonis on 23/4/2018.
 */

public class NullActionsCache<A extends Presenter.UiAction> implements IActionsCache<A> {

    private static final String TAG = NullActionsCache.class.getName();

    @Override
    public void saveActions(ArrayList<A> actions) {
        Log.d(TAG, "saveActions() called with: actions = [" + actions + "]");
        throw new RuntimeException("Tried to save with NullActionsCache");
    }

    @Override
    public ArrayList<A> restoreActions() {
        Log.d(TAG, "restoreActions() called");
        return new ArrayList<>();
    }

    @Override
    public void delete() {
        Log.d(TAG, "delete() called");
    }
}
