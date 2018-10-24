package com.quadible.mvp;

import com.quadible.mvp.mocks.ApplicationMock;
import com.quadible.mvp.mocks.DataContainerMock;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class DataCacheTest {

    private DataCache mDataCache;

    private final UUID mUuid = UUID.randomUUID();

    private final DataContainerMock mDataContainer = DataContainerMock.mock();

    private final DataContainerMock mEmptyDataContainer = DataContainerMock.empty();

    @Before
    public void setUp() {
        mDataCache = new DataCache(ApplicationMock.getApplication());
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