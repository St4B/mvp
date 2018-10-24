package quadible.com.mvp.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
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
