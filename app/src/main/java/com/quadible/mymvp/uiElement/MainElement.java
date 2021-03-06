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
package com.quadible.mymvp.uiElement;

import com.quadible.mvp.UiElement;
import com.quadible.mymvp.presenter.MainPresenter;

/**
 * <p>
 *     The actions that {@link MainPresenter} can take (on {@link com.quadible.mymvp.ui.MainActivity})
 * </p>
 */
public interface MainElement extends UiElement<MainPresenter> {

    void updateText(String text);

}
