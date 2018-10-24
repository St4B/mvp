package quadible.com.mvp.integration;

import android.os.Build;
import android.os.Bundle;

import com.quadible.mvp.Mvp;
import com.quadible.mvp.MvpRuntimeEnvironment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import quadible.com.mvp.integration.mocks.ActivityMock;
import quadible.com.mvp.integration.mocks.PresenterMock;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1) //fixme remove whenever robolectric supports api 28
public class ActivityTest {

    @Before
    public void setUp() {
        RuntimeEnvironment.application.setTheme(R.style.AppTheme);
        Mvp.install(RuntimeEnvironment.application);
    }

    @Test
    public void createPresenter() {
        ActivityController<ActivityMock> controller = Robolectric.buildActivity(ActivityMock.class);
        controller.create()
                .visible();

        ActivityMock activityMock = controller.get();
        Assert.assertNotNull(activityMock.getPresenter());
    }


    @Test
    public void restoreOnConfigurationChange() {
        ActivityController<ActivityMock> controller = Robolectric.buildActivity(ActivityMock.class);
        controller.create()
                .visible();

        PresenterMock presenterBefore = controller.get().getPresenter();

        PresenterMock presenterAfter = controller.pause()
                .resume()
                .get()
                .getPresenter();

        //same reference! Presenters are not recreated during configuration changes
        //They are just detached and attached to to the corresponding ui element.
        assertTrue(presenterBefore == presenterAfter);
    }

    @Test
    public void restoreAfterKill() {
        ActivityController<ActivityMock> controller = Robolectric.buildActivity(ActivityMock.class);
        controller.create()
                .visible();

        PresenterMock presenterBefore = controller.get().getPresenter();
        presenterBefore.setInt(111);

        Bundle bundle = new Bundle();
        controller.saveInstanceState(bundle);
        controller.pause(); //In order to store presenter

        MvpRuntimeEnvironment.cleanUp();

        PresenterMock presenterAfter = Robolectric.buildActivity(ActivityMock.class)
                .create(bundle)
                .visible()
                .get()
                .getPresenter();

        assertEquals(presenterBefore, presenterAfter);
    }

}
