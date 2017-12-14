package com.quadible.mymvp;

import android.app.Application;

import com.quadible.mvp.Mvp;

/**
 * Created by v.tsitsonis on 14/12/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Mvp.install(this);
    }
}
