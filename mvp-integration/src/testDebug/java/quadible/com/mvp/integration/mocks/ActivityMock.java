package quadible.com.mvp.integration.mocks;

import android.os.Bundle;

import com.quadible.mvp.BaseMvpActivity;

import quadible.com.mvp.integration.R;

public class ActivityMock extends BaseMvpActivity<UiElementMock, PresenterMock> {

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integration_app_compat);
    }

    @Override
    public PresenterMock createPresenter() {
        return PresenterMock.mock();
    }

    public PresenterMock getPresenter() {
        return mPresenter;
    }

}
