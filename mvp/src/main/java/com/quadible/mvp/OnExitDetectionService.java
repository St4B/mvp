package com.quadible.mvp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 *
 * https://stackoverflow.com/questions/21040339/how-to-know-when-my-app-has-been-killed
 *
 * Created by v.tsitsonis on 23/4/2018.
 */

public class OnExitDetectionService extends Service {

    private final IPresenterProvider mPresenterProvider = PresenterProvider.newInstance();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        mPresenterProvider.clear();
        stopSelf();
    }
}
