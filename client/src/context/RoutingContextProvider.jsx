import {useState} from "react";
import {RoutingContext} from "./RoutingContext";

/**
 * Contain all routing logic for this app.
 *
 * The route mapping object will look like:
 *
 * {
 *    { route: 'home', component: React.Component }
 *    { route: 'library', component: React.Component }
 *    { route: 'etc', component: React.Component }
 * }
 */

export const RoutingContextProvider = ({children}) => {
    const [currentRoute, setCurrentRoute] = useState("home");
    const [__routeMapping, __setRouteMapping] = useState({});
    const [__fallbackComponent, __setFallbackComponent] = useState();
    const [__queryParams, __setQueryParams] = useState({});
    const [queryChangeFlag, __setQueryChangeFlag] = useState(true);

    const mapRoute = (route, component) => {
        __setRouteMapping((prev) => ({
            ...prev,
            [route]: component,
        }));
    };

    const getRouteContent = (route) => {
        return route
            ? __routeMapping[route] ?? __fallbackComponent
            : __routeMapping[currentRoute] ?? __fallbackComponent;
    };

    const setFallback = (component) => {
        __setFallbackComponent(component);
    };

    const getQuery = (query) => {
        return __queryParams[query];
    };

    const getAllQueries = () => {
        return __queryParams;
    };

    const setQuery = (value) => {
        __setQueryChangeFlag(!queryChangeFlag);
        __setQueryParams((cur) => ({
            ...cur,
            ...value,
        }));
    };

    const clearAllQuery = () => {
        __setQueryParams({});
    };

    const navigateTo = (route, queryObj) => {
        setCurrentRoute(route);
        clearAllQuery();
        setQuery(queryObj);
    };

    return (
        <RoutingContext.Provider
            value={{
                currentRoute,
                setCurrentRoute,
                mapRoute,
                getRouteContent,
                setFallback,

                navigateTo,
                setQuery,
                getAllQueries,
                getQuery,
                clearAllQuery,
            }}
        >
            {children}
        </RoutingContext.Provider>
    );
};
