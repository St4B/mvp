package com.quadible.mvp;

import com.quadible.mvp.mocks.PresenterMock;

import org.junit.Test;

import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class PresenterProviderTest {

    private final PresenterProvider mPresenterProvider = PresenterProvider.newInstance();

    private final PresenterMock mPresenter = PresenterMock.mock();

    private final UUID mUuid = UUID.randomUUID();

    @Test
    public void add() {
        PresenterMock retrieved = mPresenterProvider.get(mUuid);
        assertNull(retrieved);

        mPresenterProvider.add(mUuid, mPresenter);
        retrieved = mPresenterProvider.get(mUuid);

        assertEquals(mPresenter, retrieved);
    }

    @Test
    public void get() {
        mPresenterProvider.add(mUuid, mPresenter);

        PresenterMock retrieved = mPresenterProvider.get(mUuid);
        assertEquals(mPresenter, retrieved);
    }

    @Test
    public void remove() {
        mPresenterProvider.add(mUuid, mPresenter);
        mPresenterProvider.remove(mUuid);

        PresenterMock retrieved = mPresenterProvider.get(mUuid);
        assertNull(retrieved);
    }
}