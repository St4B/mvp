package com.quadible.mvp.mocks;

import android.text.TextUtils;

import com.quadible.mvp.Presenter;
import com.quadible.mvp.annotation.Persistent;

import java.util.HashMap;

public class PresenterMock extends Presenter<UiElementMock> {

    @Persistent
    private int mInt;

    @Persistent
    private String mString;

    @Persistent
    private HashMap<String, Integer> mMap = new HashMap<>();

    private PresenterMock() {}

    public void setInt(int value) {
        mInt = value;
    }

    @Override
    public boolean equals(Object obj) {
        PresenterMock compared = (PresenterMock) obj;
        if (mInt != compared.mInt) {
            return false;
        }

        if (!TextUtils.equals(mString, compared.mString)) {
            return false;
        }

        return mMap == null ? compared.mMap == null : mMap.equals(compared.mMap);
    }

    public static PresenterMock mock() {
        PresenterMock presenter = new PresenterMock();
        presenter.mInt = 5;
        presenter.mString = "mocked";
        presenter.mMap.put("key", 11111);
        return presenter;
    }

    public static PresenterMock empty() {
        return new PresenterMock();
    }

}
