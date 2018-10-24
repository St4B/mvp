package com.quadible.mvp;

import android.support.annotation.VisibleForTesting;

@VisibleForTesting
public class MvpRuntimeEnvironment {

    public static void cleanUp() {
        Mvp.clearRuntimeData();
    }

}
