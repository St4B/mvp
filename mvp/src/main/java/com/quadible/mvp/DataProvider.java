/**
 * Copyright 2017 Quadible Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quadible.mvp;

import android.app.Application;

import com.quadible.mvp.annotation.DataContainer;
import com.quadible.mvp.annotation.Persistent;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * <p>
 *     Provides a mechanism for persistent storing and restoring the data of {@link Presenter}.
 *     Basically, the actual storing and restoring is delegated to {@link IDataCache}. This class is
 *     responsible for orchestrating the process. Specifically, it extracts the fields that were tag
 *     as {@link com.quadible.mvp.annotation.Persistent}. The operations of extracting and applying
 *     values are executed vie reflection.
 * </p>
 */
class DataProvider implements IDataProvider {

    private final IDataCache mDataCache;

    private static IDataProvider sInstance;

    private DataProvider(Application application) {
        mDataCache = new DataCache(application);
    }

    static IDataProvider newInstance() {
        if (sInstance == null) {
            throw new RuntimeException(
                    "You must call Mvp.install(Application application) in on create of your "
                            + "Application");
        }
        return sInstance;
    }

    static void init(Application application) {
        if (sInstance == null) {
            sInstance = new DataProvider(application);
        }
    }

    /**
     * Saves the {@link com.quadible.mvp.annotation.Persistent} data of the given presenter.
     * The uuid is given in order to be able to map the data whenever we call the
     * {@link IDataProvider#restore(UUID, Presenter)}.
     *
     * @param uuid      The {@link Presenter}'s uuid
     * @param presenter The {@link Presenter} from which we want to get the data.
     */
    @Override
    public void store(UUID uuid, Presenter presenter) {
        try {
            DataContainer data = extractValues(presenter);
            if (data != null) { //The presenter is not {@link Persistable}
                mDataCache.put(uuid, data);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the data which corresponds to the given uuid, to the given presenter. (We assume that
     * the uuid is the {@link Presenter}'s uuid).
     *
     * @param uuid The {@link Presenter}'s uuid (it must be the same which was use in
     *             {@link IDataProvider#store(UUID, Presenter)})
     * @param presenter The {@link Presenter} to which we want to set the data.
     * @param <T>  The type of the {@link DataContainer} which we are going to use.
     */
    @Override
    public <T extends DataContainer> void restore(UUID uuid, Presenter presenter) {
        Class<T> cls = getDataContainerType(presenter);
        if (cls != null) { //The presenter is {@link Persistable}
            DataContainer data = mDataCache.get(uuid, cls);
            try {
                applyValues(data, presenter);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Removes the stored data corresponding to this uuid. (This uuid the {@link Presenter}'s uuid).
     */
    @Override
    public void remove(UUID uuid) {
        mDataCache.remove(uuid);
    }

    /**
     * Clears all the stored data for every @{@link Presenter}). This is needed for a clean up at
     * initialization.
     */
    @Override
    public void clear() {
        mDataCache.clear();
    }

    /**
     * Extracts the values of {@link Presenter}'s {@link Persistent} fields to a {@link DataContainer}.
     * The type of {@link DataContainer} is different and depends on the type of the {@link Presenter}
     * @param presenter The presenter from which we want to extract data
     * @param <T> The type of data container
     * @return The extracted values
     * @throws IllegalAccessException
     */
    private <T extends DataContainer> DataContainer extractValues(Presenter presenter)
            throws IllegalAccessException {
        Class<T> cls = getDataContainerType(presenter);
        DataContainer data = getDataContainer(cls);

        if (data == null) { //The presenter is not {@link Persistable}
            return null;
        }

        Field[] extractedFields = data.getClass().getDeclaredFields();

        Field[] presenterFields = presenter.getClass().getDeclaredFields();
        for (Field presenterField : presenterFields) {
            if (presenterField.isAnnotationPresent(Persistent.class)) {
                boolean accessible = presenterField.isAccessible();
                presenterField.setAccessible(true);
                Object value = presenterField.get(presenter);

                for (Field extractedField : extractedFields) {
                    if (extractedField.getName().equals(presenterField.getName())) {
                        extractedField.set(data, value);
                        extractedField.setAccessible(false);
                        break;
                    }

                }
                presenterField.setAccessible(accessible);
            }

        }
        return data;
    }

    /**
     * Applies the data from {@link DataContainer} to the {@link Presenter} via reflection.
     * @param data The data to apply
     * @param presenter The presenter to which we want to set the data
     * @throws IllegalAccessException
     */
    private void applyValues(DataContainer data, Presenter presenter)
            throws IllegalAccessException {

        Field[] presenterFields = presenter.getClass().getDeclaredFields();

        Field[] fieldsToApply = data.getClass().getDeclaredFields();
        for (Field fieldToApply : fieldsToApply) {
            fieldToApply.setAccessible(true);
            Object value = fieldToApply.get(data);

            for (Field presenterField : presenterFields) {
                if (fieldToApply.getName().equals(presenterField.getName())) {
                    boolean accessible = presenterField.isAccessible();
                    presenterField.setAccessible(true);
                    presenterField.set(presenter, value);
                    presenterField.setAccessible(accessible);
                    break;
                }

            }
            fieldToApply.setAccessible(false);
        }

    }

    /**
     * Used in order to retrieve the class of given presenter's {@link DataContainer}. This class
     * was generated by the annotation processor and was named as "{@link Presenter}'s name +
     * {@link DataContainer#SUFFIX}"
     * @param presenter
     * @param <T>
     * @return
     */
    private <T extends DataContainer> Class<T> getDataContainerType(Presenter presenter) {
        String name = presenter.getClass().getCanonicalName() + DataContainer.SUFFIX;
        Class<T> cls = null;

        try {
            cls = (Class<T>) Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }

    /**
     * Instantiate {@link DataContainer} based the given class.
     */
    private <T extends DataContainer> DataContainer getDataContainer(Class<T> cls) {
        DataContainer dataContainer = null;

        try {
            dataContainer = cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataContainer;
    }

}
