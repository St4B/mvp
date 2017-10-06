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

import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * <p>
 *     Orchestrates the mvp implementation. This class is responsible for creating, destroying,
 *     attaching, detaching, storing and restoring the {@link Presenter} of a {@link UiElement} in
 *     the right time. We storing the presenters in order to not stop their tasks and cache their
 *     actions on UI. When the presenters are restored, they execute their pending {@link Presenter.UiAction}.
 * <ul>
 *     <li>
 *          We create a presenter when the corresponding UI element is created for the first time.
 *          At the creation of a Presenter we get an UUID in order to bind the created presenter with
 *          the UI element. Whenever the UI element is recreated from the operating system, the new
 *          instance of the UI element restores its presenter based on this UUID
 *     </li>
 *     <li>
 *          We destroy a presenter when the corresponding UI element is going to be destroyed
 *          permanently. (Not for configuration changes)
 *     </li>
 *     <li>
 *          We attach a presenter when the corresponding UI element becomes visible. When a
 *          presenter is attached, it can perform UI actions.
 *     </li>
 *     <li>
 *         We detach a presenter when the corresponding UI element becomes invisible. When a
 *         presenter is detached, it can not perform UI actions. The UI actions are cached and they
 *         will be executed when the presenter is reattached.
 *     </li>
 *     <li>
 *         We store a presenter when it is detached. The presenter can finish its tasks even
 *         if the UI element is not currently visible and post the pending UI actions when it is
 *         attached again
 *     </li>
 *     <li>
 *         We restore a presenter in order to attach it.
 *     </li>
 * </ul>
 * </p>
 */
public class Mvp<U extends UiElement<P>, P extends Presenter<U>> {

    private static final String BUNDLE_KEY_PRESENTER_UUID  = "presenterUuid";

    private ParcelUuid mParcelUuid;

    private PresenterProvider mPresenterProvider = PresenterProvider.newInstance();

    /**
     * Start the mvp implementation for the {@link UiElement}. Basically we create a
     * {@link Presenter} and assign a UUID to use for storing/restoring presenters while UI element's
     * visibility change.
     * @param ui The UI element that we want to use mvp pattern
     */
    public void setUp(U ui) {
        UUID uuid = UUID.randomUUID();
        mParcelUuid = new ParcelUuid(uuid);
        P presenter = ui.createPresenter();
        mPresenterProvider.add(uuid, presenter);
        ui.setPresenter(presenter);
    }

    /**
     * Used to inform that the visibility of the {@link UiElement} has change and that we need to
     * attach or detach the {@link Presenter} from it.
     * @param ui The UI element
     * @param isVisible If visibility changes to visible
     */
    public void onUiVisibilityChanged(U ui, boolean isVisible) {
        if (isVisible) {
            attachPresenter(ui);
        } else {
            detachPresenter();
        }
    }

    /**
     * Attach {@link Presenter} to the given {@link UiElement}. We restore it with the help of
     * {@link PresenterProvider} based on the assigned UUID.
     * @param ui The UI element to which we want to attach the presenter.
     */
    private void attachPresenter(U ui) {
        UUID uuid = mParcelUuid.getUuid();
        P presenter = mPresenterProvider.get(uuid);
        presenter.attach(ui);
        ui.setPresenter(presenter);
    }

    /**
     * Detach {@link Presenter} from its corresponding UI element. We store it with the help of
     * {@link PresenterProvider} in order to restore it when the {@link UiElement} becomes visible
     * again.
     */
    private void detachPresenter() {
        UUID uuid = mParcelUuid.getUuid();
        P presenter = mPresenterProvider.get(uuid);
        presenter.detach();
    }

    /**
     * Used in order to store the {@link Presenter}'s UUID to the corresponding {@link UiElement}.
     * When the UI element is recreated it will retrieve its presenter based on this UUID.
     * @param outState Where to save the UUID.
     */
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(BUNDLE_KEY_PRESENTER_UUID, mParcelUuid);
    }

    /**
     * Used in order to retrieve the {@link Presenter}'s UUID from the corresponding
     * {@link UiElement} in order to retrieve its presenter based on this UUID from
     * {@link PresenterProvider}.
     * @param savedInstanceState From where to retrieve UUID.
     */
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        mParcelUuid = savedInstanceState.getParcelable(BUNDLE_KEY_PRESENTER_UUID);
    }

    /**
     * Remove {@link Presenter} from {@link PresenterProvider}. The {@link UiElement} is removed and
     * as a result the presenter is not needed any more.
     */
    public void destroy() {
        UUID uuid = mParcelUuid.getUuid();
        mPresenterProvider.remove(uuid);
    }

}
