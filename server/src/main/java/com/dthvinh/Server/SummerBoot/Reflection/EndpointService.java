package com.dthvinh.Server.SummerBoot.Reflection;

import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.dthvinh.Server.SummerBoot.Anotations.Endpoint;
import com.dthvinh.Server.SummerBoot.DI.ServiceContainer;
import com.dthvinh.Server.SummerBoot.Filters.BaseFilter;
import com.dthvinh.Server.SummerBoot.Filters.GlobalErrorHandler;
import com.dthvinh.Server.SummerBoot.Filters.PerformanceMeter;
import com.dthvinh.Server.SummerBoot.Security.Cors;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class EndpointService {

    private final ServiceContainer container;

    public EndpointService(ServiceContainer container) {
        this.container = container;
    }

    public void registerEndpoints(HttpServer server) {

        Reflections reflections = new Reflections("com.dthvinh.Server.Endpoints");

        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Endpoint.class);

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

            HttpContext context = server.createContext(route, Cors.withCors(handler));

            context.getFilters().addAll(getFilters());

            System.out.println(
                    "Registered: " + route + " → " + clazz.getSimpleName());
        }
    }

    private List<BaseFilter> getFilters() {
        BaseFilter ft1 = new GlobalErrorHandler();
        BaseFilter ft2 = new PerformanceMeter();

        return List.of(ft1, ft2);
    }
}
