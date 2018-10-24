package com.quadible.mvp;

import com.quadible.mvp.mocks.ApplicationMock;
import com.quadible.mvp.mocks.PresenterMock;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class DataProviderTest {

    private IDataProvider mDataProvider;

    private final UUID mUuid = UUID.randomUUID();

    private final PresenterMock mPresenter = PresenterMock.mock();

    private final PresenterMock mEmptyPresenter = PresenterMock.empty();

    @Before
    public void setUp() {
        DataProvider.init(ApplicationMock.getApplication());
        mDataProvider = DataProvider.newInstance();
    }

    @Test
    public void store() {
        mDataProvider.store(mUuid, mPresenter);
        mDataProvider.restore(mUuid, mEmptyPresenter);
        assertEquals(mPresenter, mEmptyPresenter);
    }

}