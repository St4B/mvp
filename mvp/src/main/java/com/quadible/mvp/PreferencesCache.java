package com.quadible.mvp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.quadible.mvp.Presenter.UiAction;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 *
 * //fixme
 * also delegate work
 *
 * Created by v.tsitsonis on 14/12/2017.
 */

class PreferencesCache implements ICache {

    private static final String PREFERENCE_PRESENTERS_SUFFIX = ".mvpPresenters";

    private static final String PREFERENCE_TYPES_SUFFIX = ".mvpTypes";

    private static final String PREFERENCE_ACTIONS_SUFFIX = ".mvpActions";

    private static final String KEY_ACTIONS_TYPES = "types";

    private static final String ACTIONS_SEPARATOR = ",";

    private static PreferencesCache sInstance;

    private final SharedPreferences mPresentersPreferences;

    private final SharedPreferences mTypesPreferences;

    private final String PREFIX_NAME;

    private final Application mApplication;

    private PreferencesCache(Application application) {
        mApplication = application;
        PREFIX_NAME = mApplication.getPackageName();
        String prefsName = PREFIX_NAME + PREFERENCE_PRESENTERS_SUFFIX;
        mPresentersPreferences = mApplication.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        String typesName = PREFIX_NAME + PREFERENCE_TYPES_SUFFIX;
        mTypesPreferences = mApplication.getSharedPreferences(typesName, Context.MODE_PRIVATE);
    }

    protected static synchronized PreferencesCache newInstance() {
        if (sInstance == null) {
            throw new RuntimeException(
                    "You must call Mvp.install(Application application) in on create of your Application");
        }
        return sInstance;
    }

    protected static synchronized PreferencesCache newInstance(Application application) {
        if (sInstance == null) {
            sInstance = new PreferencesCache(application);
        }
        return sInstance;
    }

    /**
     * fixme
     * @return
     */
    @Override
    public ArrayList<UUID> getCachedKeys() {
        ArrayList<UUID> actualUuids = new ArrayList<>();

        Set<String> uuids = mPresentersPreferences.getAll().keySet();
        for (String uuid : uuids) {
            UUID actualUuid = UUID.fromString(uuid);
            actualUuids.add(actualUuid);
        }

        return actualUuids;
    }

    /**
     * fixme
     * @param uuid
     * @param presenter
     */
    @Override
    public void cache(UUID uuid, Presenter presenter) {
        Gson gson = new Gson();
        String serializedPresenter = gson.toJson(presenter);
        String uuidString = uuid.toString();

        mPresentersPreferences.edit()
                .putString(uuidString, serializedPresenter)
                .commit();

        mTypesPreferences.edit()
                .putString(uuidString, presenter.getClass().getName())
                .commit();

        saveActions(uuid, presenter.getPendingActions());
    }

    /**
     * fixme
     * @param uuid
     * @param type
     * @param <T>
     * @return
     */
    @Override
    public <T extends Presenter>Presenter get(UUID uuid, Class<T> type) {
        String uuidString = uuid.toString();
        String serializedPresenter = mPresentersPreferences.getString(uuidString, "");
        Gson gson = new Gson();
        T presenter =  gson.fromJson(serializedPresenter, type);
        restoreActions(uuid, presenter);
        return presenter;
    }

    /**
     * fixme
     * @param uuid
     * @param <T>
     * @return
     */
    @Override
    public <T extends Presenter> Class<T> getType(UUID uuid) {
        String uuidString = uuid.toString();
        String typeName = mTypesPreferences.getString(uuidString, "");
        Class<T> cls = null;
        try {
            cls = (Class<T>) Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return cls;
    }

    /**
     * fixme
     * @param uuid
     */
    @Override
    public void clear(UUID uuid) {
        String uuidString = uuid.toString();
        mPresentersPreferences.edit().remove(uuidString).apply();
        mTypesPreferences.edit().remove(uuidString).apply();
        deleteActionsFile(uuid);
    }

    /**
     * fixme
     * @param uuid
     * @param actions
     */
    private void saveActions(UUID uuid, ArrayList<UiAction> actions) {
        if (actions.size() == 0) return;
        
        SharedPreferences actionsPrefs = getActionsPrefs(uuid);

        StringBuilder actionTypes = new StringBuilder();
        Gson gson = new Gson();

        for (int i = 0, size = actions.size(); i < size; i++) {
            UiAction action = actions.get(i);
            actionTypes.append(action.getClass().getName());
            actionTypes.append(ACTIONS_SEPARATOR);
            String serializedAction = gson.toJson(action);
            actionsPrefs.edit().putString(Integer.toString(i), serializedAction).commit();
        }

        //Remove last separator
        actionTypes.setLength(actionTypes.length() - ACTIONS_SEPARATOR.length());

        actionsPrefs.edit().putString(KEY_ACTIONS_TYPES, actionTypes.toString()).commit();
    }

    /**
     * fixme
     * @param uuid
     * @param presenter
     */
    private void restoreActions(UUID uuid, Presenter presenter) {
        SharedPreferences actionsPrefs = getActionsPrefs(uuid);

        String typesInString = actionsPrefs.getString(KEY_ACTIONS_TYPES, "");
        String[] types = typesInString.split(ACTIONS_SEPARATOR);
        Gson gson = new Gson();
        ArrayList<UiAction> actions = new ArrayList<>();

        for (int i = 0; i < types.length; i++) {
            String typeName = types[i];
            String serializedAction = actionsPrefs.getString(Integer.toString(i), "");
            Class<? extends UiAction> cls;
            try {
                cls = (Class<? extends UiAction>) Class.forName(typeName);
                UiAction action = gson.fromJson(serializedAction, cls);
                actions.add(action);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        presenter.setPendingActions(actions);
        presenter.onRestore();

        deleteActionsFile(uuid);
    }

    /**
     * fixme
     * @param uuid
     * @return
     */
    private SharedPreferences getActionsPrefs(UUID uuid) {
        String actionsName = getActionsPrefsName(uuid);
        return mApplication.getSharedPreferences(actionsName, Context.MODE_PRIVATE);
    }

    private String getActionsPrefsName(UUID uuid) {
        return uuid.toString() + PREFERENCE_ACTIONS_SUFFIX;
    }

    /**
     * @deprecated Fixme separate actions' cache from presenters' cache
     * @param uuid
     */
    @Deprecated
    @Override
    public void deleteActionsFile(UUID uuid) {
        File file = mApplication.getFilesDir();
        String path = file.getParent() + "/shared_prefs/" + getActionsPrefsName(uuid) + ".xml";
        File sharedPrefs = new File(path);
        sharedPrefs.delete();
    }
}
