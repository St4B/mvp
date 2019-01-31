package com.quadible.mymvp.presenter;

import android.os.AsyncTask;

import com.quadible.mvp.Presenter;
import com.quadible.mvp.annotation.Persistent;
import com.quadible.mymvp.uiElement.TimerElement;

/**
 * <p>
 *     The {@link Presenter} of {@link com.quadible.mymvp.ui.TimerFragment}. Whenever the
 *     {@link TimerPresenter#startTimer()} is called the presenter waits for a while and then
 *     uses {@link com.quadible.mvp.Presenter.UiAction} to change text of its corresponding
 *     UiElement a.k.a {@link com.quadible.mymvp.ui.TimerFragment}.
 * </p>
 */
public class TimerPresenter extends Presenter<TimerElement> {

    @Persistent
    private int mSeconds = 0;

    private static final int SLEEP_SECONDS = 1000;

    public void startTimer() {
        new Timer().execute();
    }

    @Override
    protected void onRestore() {
        super.onRestore();
        startTimer();
    }

    private class Timer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(SLEEP_SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mSeconds++;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            post((ui) -> ui.setTime(mSeconds));
            new Timer().execute();
        }
    }

}
