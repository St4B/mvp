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

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.quadible.mvp.Presenter.UiAction;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * <p>
 *     Implementation of ({@link IActionsCache}) that uses the shared preferences as a persistent
 *     storage.
 *     <br/>
 *     It creates a different shared preferences file per {@link Presenter} using it's id in order
 *     to distinguish them.
 *     <br/>
 *     The logic here is to save the actions using as key the position of the {@link UiAction} inside
 *     the pending action's list. This helps us to avoid the overhead of continuous serializing/
 *     deserializing when we had a new pending action, if we saved the list at a whole.
 * </p>
 */
class ActionsCache<A extends UiAction> implements IActionsCache<A> {

    private static final String PREFERENCE_ACTIONS_SUFFIX = ".mvpActions";

    private static final String KEY_ACTIONS_TYPES = "types";

    private static final String ACTIONS_SEPARATOR = ",";

    private final SharedPreferences mActionsPreferences;

    private final Application mApplication;

    private final String mFileName;

    ActionsCache(Application application, UUID uuid) {
        mApplication = application;
        mFileName = uuid.toString() + PREFERENCE_ACTIONS_SUFFIX;
        mActionsPreferences = mApplication.getSharedPreferences(mFileName, Context.MODE_PRIVATE);
    }

    /**
     * Save {@link Presenter}'s pending actions in a persistent storage.
     * @param actions Actions that we want to save.
     */
    @Override
    public void saveActions(ArrayList<A> actions) {
        if (actions == null || actions.size() == 0) return;

        StringBuilder actionTypes = new StringBuilder();
        Gson gson = new Gson();

        for (int i = 0, size = actions.size(); i < size; i++) {
            Presenter.UiAction action = actions.get(i);
            actionTypes.append(action.getClass().getName());
            actionTypes.append(ACTIONS_SEPARATOR);
            String serializedAction = gson.toJson(action);
            mActionsPreferences.edit().putString(Integer.toString(i), serializedAction).commit();
        }

        //Remove last separator
        actionTypes.setLength(actionTypes.length() - ACTIONS_SEPARATOR.length());

        mActionsPreferences.edit().putString(KEY_ACTIONS_TYPES, actionTypes.toString()).commit();
    }

    /**
     * Return pending actions from the persistent storage. Used after {@link Presenter} was restored.
     * @return The pending actions that were stored.
     */
    @Override
    public ArrayList<A> restoreActions() {
        String typesInString = mActionsPreferences.getString(KEY_ACTIONS_TYPES, "");

        if (TextUtils.isEmpty(typesInString)) {
            return new ArrayList<>();
        }

        String[] types = typesInString.split(ACTIONS_SEPARATOR);
        Gson gson = new Gson();
        ArrayList<A> actions = new ArrayList<>();

        for (int i = 0; i < types.length; i++) {
            String typeName = types[i];
            String serializedAction = mActionsPreferences.getString(Integer.toString(i), "{}");
            Class<? extends Presenter.UiAction> cls;
            try {
                cls = (Class<? extends A>) Class.forName(typeName);
                A action = (A) gson.fromJson(serializedAction, cls);
                actions.add(action);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return actions;
    }

    /**
     * Delete actions from persistent storage.
     */
    @Override
    public void delete() {
        File file = mApplication.getFilesDir();
        String path = file.getParent() + "/shared_prefs/" + mFileName + ".xml";
        File sharedPrefs = new File(path);
        sharedPrefs.delete();
    }
}
