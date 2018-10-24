package quadible.com.mvp.integration;

import android.os.Bundle;
import android.os.ParcelUuid;

import com.quadible.mvp.mocks.ActivityMock;
import com.quadible.mvp.mocks.PresenterMock;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class ActivityTest {

    @Before
    public void setUp() {
        RuntimeEnvironment.application.setTheme(R.style.Theme_AppCompat);
        Mvp.install(RuntimeEnvironment.application);
    }

    @Test
    public void createPresenter() {
        ActivityController<ActivityMock> controller = Robolectric.buildActivity(ActivityMock.class);
        controller.create()
                .visible();

        ActivityMock activityMock = controller.get();
        Assert.assertNotNull(activityMock.mPresenter);
    }


    @Test
    public void restoreOnConfigurationChange() {
        ActivityController<ActivityMock> controller = Robolectric.buildActivity(ActivityMock.class);
        controller.create()
                .visible();

        PresenterMock presenterBefore = controller.get().mPresenter;

        PresenterMock presenterAfter = controller.pause()
                .resume()
                .get()
                .mPresenter;

        //same reference! Presenters are not recreated during configuration changes
        //They are just detached and attached to to the corresponding ui element.
        assertTrue(presenterBefore == presenterAfter);
    }

    @Test
    public void restoreAfterKill() {
        ActivityController<ActivityMock> controller = Robolectric.buildActivity(ActivityMock.class);
        controller.create()
                .visible();

        PresenterMock presenterBefore = controller.get().mPresenter;
        presenterBefore.setInt(111);

        Bundle bundle = new Bundle();
        controller.saveInstanceState(bundle) //take the bundle
                .pause(); //trigger presenter's store process

        //Remove presenter from presenter provider.
        //On kill, presenter provider would not contain any presenter
        ParcelUuid parcelUuid = bundle.getParcelable("presenterUuid");
        PresenterProvider.newInstance().remove(parcelUuid.getUuid());

        ActivityMock recreatedActivity = Robolectric.buildActivity(ActivityMock.class)
                .create(bundle)
                .get();

        assertEquals(presenterBefore, recreatedActivity.mPresenter);
    }

}
