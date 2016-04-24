package org.pasut.android.socialpricing.services;

import java.lang.reflect.Type;

/**
 * Created by boot on 3/24/16.
 */
public interface PreferencesService {
    <T> T get(String key, Class<T> clazz);
    <T> T get(String key, Type type);
    <T> void put(String key, T value);
    boolean contain(String key);
    void delete(String key);
}
