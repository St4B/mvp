package quadible.com.mvp.integration.mocks;

import android.app.Application;

import com.quadible.mvp.Mvp;

public class ApplicationMock extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Mvp.install(this);
    }

}
