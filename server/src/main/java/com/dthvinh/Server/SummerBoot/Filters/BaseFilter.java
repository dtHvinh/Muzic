package com.dthvinh.Server.SummerBoot.Filters;

import com.dthvinh.Server.SummerBoot.Mornitoring.Logger;
import com.sun.net.httpserver.Filter;

public abstract class BaseFilter extends Filter {
    protected final Logger logger = Logger.getInstance();
}
