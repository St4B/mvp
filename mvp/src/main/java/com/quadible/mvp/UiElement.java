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
package com.quadible.mvp;

/**
 * <p>
 *     Defines the main actions of a UI element that a {@link Presenter} can execute to it.
 *     Basically this is a contract between a presenter and a ui.
 * </p>D
 */
public interface UiElement<P extends Presenter> {

    /**
     * It is used in order to define the way that {@link Mvp} will create the {@link Presenter} for
     * the current ui element.
     * @return The created presenter.
     */
    P createPresenter();

    /**
     * It is used in order to set the {@link Presenter} to the current ui when it becomes visible.
     * @param presenter Ui's presenter
     */
    void setPresenter(P presenter);

    void onPresenterCreated();

}
