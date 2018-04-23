package com.quadible.mvp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.quadible.mvp.Presenter.UiAction;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by v.tsitsonis on 23/4/2018.
 */

class ActionsCache<A extends UiAction> implements IActionsCache<A> {

    private static final String PREFERENCE_ACTIONS_SUFFIX = ".mvpActions";

    private static final String KEY_ACTIONS_TYPES = "types";

    private static final String ACTIONS_SEPARATOR = ",";

    private final SharedPreferences mActionsPreferences;

    private final Application mApplication;

    private final String mFileName;

    public ActionsCache(Application application, UUID uuid) {
        mApplication = application;
        mFileName = uuid.toString() + PREFERENCE_ACTIONS_SUFFIX;
        mActionsPreferences = mApplication.getSharedPreferences(mFileName, Context.MODE_PRIVATE);
    }

    @Override
    public void saveActions(ArrayList<A> actions) {
        if (actions.size() == 0) return;

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

    @Override
    public ArrayList<A> restoreActions() {
        String typesInString = mActionsPreferences.getString(KEY_ACTIONS_TYPES, "");
        String[] types = typesInString.split(ACTIONS_SEPARATOR);
        Gson gson = new Gson();
        ArrayList<A> actions = new ArrayList<>();

        for (int i = 0; i < types.length; i++) {
            String typeName = types[i];
            String serializedAction = mActionsPreferences.getString(Integer.toString(i), "");
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

    @Override
    public void delete() {
        File file = mApplication.getFilesDir();
        String path = file.getParent() + "/shared_prefs/" + mFileName + ".xml";
        File sharedPrefs = new File(path);
        sharedPrefs.delete();
    }
}
