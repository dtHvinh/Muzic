package com.dthvinh.Server.SummerBoot.Reflection;

import com.dthvinh.Server.SummerBoot.Anotations.Endpoint;
import com.dthvinh.Server.SummerBoot.Security.Cors;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import org.reflections.Reflections;

public class EndpointService {

    public static void registerEndpoints(HttpServer server)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {

        Reflections reflections = new Reflections("com.dthvinh.Server.Endpoints");
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
                server.createContext(route, Cors.withCors(handler));

                System.out.println("Registered: " + route + " → " + clazz.getSimpleName());
            }
        }
    }
}
