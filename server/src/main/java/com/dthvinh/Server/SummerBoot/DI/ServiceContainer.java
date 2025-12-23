package com.dthvinh.Server.SummerBoot.DI;

import com.dthvinh.Server.SummerBoot.Reflection.ClassReflectionHelper;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ServiceContainer {

    private final Map<Class<?>, Object> singletons = new HashMap<>();

    public <T> void register(Class<T> type, T instance) {
        singletons.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        return (T) singletons.get(type);
    }

    @SuppressWarnings("unchecked")
    public <T> T injectThenNewInstance(Class<T> type) {
        try {
            Constructor<?> constructor = ClassReflectionHelper.getConstructorWithMostParams(type);
            Class<?>[] paramTypes = constructor.getParameterTypes();

            Object[] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                Object dep = singletons.get(paramTypes[i]);
                if (dep == null) {
                    throw new RuntimeException(
                            "No dependency registered for " + paramTypes[i].getName()
                    );
                }
                args[i] = dep;
            }

            return (T) constructor.newInstance(args);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create " + type.getName(), e);
        }
    }
}