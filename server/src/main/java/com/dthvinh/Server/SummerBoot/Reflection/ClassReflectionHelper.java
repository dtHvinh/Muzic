package com.dthvinh.Server.SummerBoot.Reflection;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;

public class ClassReflectionHelper {
    public static Constructor<?> getConstructorWithMostParams(Class<?> type) {
        Constructor<?>[] constructors = type.getDeclaredConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        return Arrays.stream(constructors)
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow();
    }
}
