package com.quadible.mvp.mocks;

import android.app.Application;

import com.quadible.mvp.Mvp;

public class ApplicationMock extends Application {

    private static ApplicationMock application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Mvp.install(this);
    }

    public static ApplicationMock getApplication() {
        return application;
    }
}
