package com.quadible.mvp;

import androidx.annotation.VisibleForTesting;

@VisibleForTesting
public class MvpRuntimeEnvironment {

    public static void cleanUp() {
        Mvp.clearRuntimeData();
    }

}
