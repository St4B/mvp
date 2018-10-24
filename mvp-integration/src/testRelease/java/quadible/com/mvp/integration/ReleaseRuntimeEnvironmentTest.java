package quadible.com.mvp.integration;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1) //fixme remove whenever robolectric supports api 28
public class ReleaseRuntimeEnvironmentTest {

    @Test
    public void debugClassesDoNotExist() {
        try {
            Class.forName("com.quadible.mvp.MvpRuntimeEnvironment");
            fail(); //existence cause failure
        } catch (ClassNotFoundException e) {
            //non existence is what we want in a release environment. This test passed
        }
    }

}
