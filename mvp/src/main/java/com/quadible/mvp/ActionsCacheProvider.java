package com.quadible.mvp;

import android.app.Application;

import com.quadible.mvp.Presenter.UiAction;

import java.util.UUID;

/**
 * Created by v.tsitsonis on 23/4/2018.
 */

public class ActionsCacheProvider {

    private final Application mApplication;

    private static ActionsCacheProvider sInstance;

    public ActionsCacheProvider(Application application) {
        mApplication = application;
    }

    protected static ActionsCacheProvider newInstance() {
        if (sInstance == null) {
            throw new RuntimeException(
                    "You must call Mvp.install(Application application) in on create of your Application");
        }
        return sInstance;
    }

    public static void init(Application application) {
        if (sInstance == null) sInstance = new ActionsCacheProvider(application);
    }

    public <A extends UiAction> IActionsCache<A> provide(UUID uuid) {
        if (uuid == null) {
            return new NullActionsCache<>();
        } else {
            return new ActionsCache<>(mApplication, uuid);
        }
    }

}
