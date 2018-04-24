package com.quadible.mvp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

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
        presenter.onRestore();
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
        ActionsCacheProvider.newInstance().provide(uuid).delete();
    }

}
