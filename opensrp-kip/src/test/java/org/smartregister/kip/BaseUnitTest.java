package org.smartregister.kip;

import android.os.Build;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


@RunWith (PowerMockRunner.class)
@PowerMockRunnerDelegate (RobolectricTestRunner.class)
@Config (application = TestKipApplication.class, sdk = Build.VERSION_CODES.O_MR1)
@PowerMockIgnore ({"org.mockito.*", "org.robolectric.*", "android.*"})
public abstract class BaseUnitTest {

    protected static final String DUMMY_USERNAME = "myusername";
    protected static final String DUMMY_PASSWORD = "mypassword";
}
