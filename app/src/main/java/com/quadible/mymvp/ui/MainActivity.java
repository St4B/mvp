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
package com.quadible.mymvp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;

import com.quadible.mvp.BaseMvpActivity;
import com.quadible.mymvp.R;
import com.quadible.mymvp.presenter.MainPresenter;
import com.quadible.mymvp.uiElement.MainElement;

/**
 * <p>
 *     Example of {@link BaseMvpActivity}. It has button that when it is pressed calls
 *     {@link MainPresenter#setNewText()}. Also it uses a {@link TimerFragment}.
 * </p>
 */
public class MainActivity
        extends BaseMvpActivity<MainElement, MainPresenter> implements MainElement{

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.btnUpdateLabel);
        mButton.setOnClickListener(v -> mPresenter.setNewText());
        addFragment();
    }

    private void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        TimerFragment fragment =
                (TimerFragment) fragmentManager.findFragmentByTag(TimerFragment.TAG);
        if (fragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragment = new TimerFragment();
            fragmentTransaction.add(R.id.fragment_container, fragment, TimerFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void updateText(String text) {
        mButton.setText(text);
    }
}
