package com.quadible.mvp;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *     Util class for doing checks over objects. The logic was to deliver
 *     {@link Objects#requireNonNull(Object)} (which works for Api &gt; 19) to previous Apis. This
 *     class is used in order to add another checks in the future if we want.
 * </p>
 */

public final class Check {

    private Check() {
        //No need to instantiate
    }

    /**
     * Checks that the specified object reference is not {@code null}. It is based on
     * {@link Objects#requireNonNull(Object)} which works for Api &gt; 19.
     * @param obj the object reference to check for nullity
     * @param <T> the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }

    /**
     * Checks that the specified key reference does not already exists in map reference.
     * @param key the key reference to check if exists
     * @param map the map reference to check for key existence
     * @param <T> the type of the key reference
     * @throws NullPointerException if {@code key} or {@code map} are {@code null}
     * @throws IllegalArgumentException if {@code key} exists in {@code map}
     */
    static <T> void requireNotExist(T key, Map<T, ?> map) {
        requireNonNull(key);
        requireNonNull(map);
        if (map.containsKey(key)) {
            throw new IllegalArgumentException();
        }
    }
}
