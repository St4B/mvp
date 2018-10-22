package com.quadible.mvp;

import static org.junit.Assert.assertEquals;

import com.quadible.mvp.mocks.DataContainerMock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.UUID;

@RunWith(RobolectricTestRunner.class)
public class DataCacheTest {

    private DataCache mDataCache;

    private final UUID mUuid = UUID.randomUUID();

    private final DataContainerMock mDataContainer = DataContainerMock.mock();

    private final DataContainerMock mEmptyDataContainer = DataContainerMock.empty();

    @Before
    public void setUp() {
        mDataCache = new DataCache(RuntimeEnvironment.application);
    }

    @Test
    public void put() {
        DataContainerMock retrieved = mDataCache.get(mUuid, DataContainerMock.class);
        assertEquals(retrieved, mEmptyDataContainer);

        mDataCache.put(mUuid, mDataContainer);
        retrieved = mDataCache.get(mUuid, DataContainerMock.class);

        assertEquals(mDataContainer, retrieved);
    }

    @Test
    public void get() {
        mDataCache.put(mUuid, mDataContainer);

        DataContainerMock retrieved = mDataCache.get(mUuid, DataContainerMock.class);
        assertEquals(mDataContainer, retrieved);
    }

    @Test
    public void remove() {
        mDataCache.put(mUuid, mDataContainer);
        mDataCache.remove(mUuid);

        DataContainerMock retrieved = mDataCache.get(mUuid, DataContainerMock.class);
        assertEquals(retrieved, mEmptyDataContainer);
    }

}