package com.quadible.mymvp.presenter;

import android.os.AsyncTask;

import com.quadible.mvp.Presenter;
import com.quadible.mymvp.uiElement.TimerElement;

/**
 * Created by v.tsitsonis on 23/4/2018.
 */

public class TimerPresenter extends Presenter<TimerElement> {

    private int mSeconds = 0;

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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mSeconds++;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            post(new UpdateTime(mSeconds));
            new Timer().execute();
        }
    }

    private class UpdateTime extends UiAction<TimerElement> {

        private final int mSeconds;

        private UpdateTime(int seconds) {
            mSeconds = seconds;
        }

        @Override
        public void act() {
            getUi().setTime(mSeconds);
        }
    }
}
