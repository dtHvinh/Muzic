package com.dthvinh.test.Reflection;

import com.dthvinh.test.Reflection.Anotations.Endpoint;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class EndpointService {
    public static void registerEndpoints(HttpServer server) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections("com.dthvinh.test.Endpoints");

        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Endpoint.class);

        for (Class<?> clazz : types) {
            if (HttpHandler.class.isAssignableFrom(clazz)) {
                Endpoint ep = clazz.getAnnotation(Endpoint.class);
                String route = ep.route();

                if (route.isEmpty()) {
                    route = "/" + clazz.getSimpleName()
                            .toLowerCase()
                            .replace("endpoint", "")
                            .replace("handler", "");
                }

                route = ("/" + route).replaceAll("//+", "/");

                HttpHandler handler = (HttpHandler) clazz.getDeclaredConstructor().newInstance();
                server.createContext(route, handler);

                System.out.println("Registered: " + route + " → " + clazz.getSimpleName());
            }
        }

    }
}
