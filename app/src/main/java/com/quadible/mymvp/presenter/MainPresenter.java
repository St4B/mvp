/**
 * Copyright 2017 Quadible Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quadible.mymvp.presenter;

import android.os.AsyncTask;

import com.quadible.mvp.Presenter;
import com.quadible.mymvp.uiElement.MainElement;

/**
 * <p>
 *     The {@link Presenter} of {@link com.quadible.mymvp.ui.MainActivity}. Whenever the
 *     {@link MainPresenter#setNewText()} is called the presenter waits for a while and then
 *     uses {@link UiUpdater} to change text of its corresponding UiElement a.k.a
 *     {@link com.quadible.mymvp.ui.MainActivity}.
 * </p>
 */
public class MainPresenter extends Presenter<MainElement> {

    private static final int SECONDS = 10000;

    public MainPresenter(){}

    public void setNewText(){
        new Task().execute();
    }

    private class Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //Wow! A really heavy task! We have time to put the app in the background and kill
                //it in order to check if presenter is consistent (even if the app was killed :D)
                Thread.sleep(SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            post(new UiUpdater());
        }
    }

    private class UiUpdater extends UiAction<MainElement> {

        @Override
        public void act() {
            final String text = "TSIKABOOM!";
            getUi().updateText(text);
        }
    }

}
