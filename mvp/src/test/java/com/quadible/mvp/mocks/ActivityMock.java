package com.quadible.mvp.mocks;

import com.quadible.mvp.BaseMvpActivity;

public class ActivityMock extends BaseMvpActivity<UiElementMock, PresenterMock> {

    @Override
    public PresenterMock createPresenter() {
        return PresenterMock.mock();
    }

}
