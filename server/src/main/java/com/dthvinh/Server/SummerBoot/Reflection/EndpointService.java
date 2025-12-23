package com.dthvinh.Server.SummerBoot.Reflection;

import com.dthvinh.Server.SummerBoot.Anotations.Endpoint;
import com.dthvinh.Server.SummerBoot.DI.ServiceContainer;
import com.dthvinh.Server.SummerBoot.Security.Cors;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.reflections.Reflections;

import java.util.Set;

public class EndpointService {

    private final ServiceContainer container;

    public EndpointService(ServiceContainer container) {
        this.container = container;
    }

    public void registerEndpoints(HttpServer server) {
        
        Reflections reflections =
                new Reflections("com.dthvinh.Server.Endpoints");

        Set<Class<?>> types =
                reflections.getTypesAnnotatedWith(Endpoint.class);

        for (Class<?> clazz : types) {

            if (!HttpHandler.class.isAssignableFrom(clazz)) {
                continue;
            }

            Endpoint ep = clazz.getAnnotation(Endpoint.class);
            String route = ep.route();

            if (route.isEmpty()) {
                route = "/" + clazz.getSimpleName()
                        .toLowerCase()
                        .replace("endpoint", "")
                        .replace("handler", "");
            }

            route = ("/" + route).replaceAll("//+", "/");

            HttpHandler handler = (HttpHandler) container.injectThenNewInstance(clazz);

            server.createContext(route, Cors.withCors(handler));

            System.out.println(
                    "Registered: " + route + " → " + clazz.getSimpleName()
            );
        }
    }
}

