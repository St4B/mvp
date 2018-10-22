package com.quadible.mvp;

import static org.junit.Assert.*;

import com.quadible.mvp.mocks.PresenterMock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.UUID;

@RunWith(RobolectricTestRunner.class)
public class DataProviderTest {

    private IDataProvider mDataProvider;

    private final UUID mUuid = UUID.randomUUID();

    private final PresenterMock mPresenter = PresenterMock.mock();

    private final PresenterMock mEmptyPresenter = PresenterMock.empty();

    @Before
    public void setUp() {
        DataProvider.init(RuntimeEnvironment.application);
        mDataProvider = DataProvider.newInstance();
    }

    @Test
    public void store() {
        mDataProvider.store(mUuid, mPresenter);
        mDataProvider.restore(mUuid, mEmptyPresenter);
        assertEquals(mPresenter, mEmptyPresenter);
    }

}