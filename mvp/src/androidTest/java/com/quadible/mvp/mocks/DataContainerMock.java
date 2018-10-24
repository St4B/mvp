package com.quadible.mvp.mocks;

import android.text.TextUtils;

import com.quadible.mvp.annotation.DataContainer;

import java.util.HashMap;

public class DataContainerMock implements DataContainer {

    private int mInt;

    private String mString;

    private HashMap<String, Integer> mMap = new HashMap<>();

    private DataContainerMock() {}

    @Override
    public boolean equals(Object obj) {
        DataContainerMock compared = (DataContainerMock) obj;
        if (mInt != compared.mInt) {
            return false;
        }

        if (!TextUtils.equals(mString, compared.mString)) {
            return false;
        }

        return mMap == null ? compared.mMap == null : mMap.equals(compared.mMap);
    }

    public static DataContainerMock mock() {
        DataContainerMock dataContainer = new DataContainerMock();
        dataContainer.mInt = 5;
        dataContainer.mString = "mocked";
        dataContainer.mMap.put("key", new Integer(13));
        return dataContainer;
    }

    public static DataContainerMock empty() {
        return new DataContainerMock();
    }

}
