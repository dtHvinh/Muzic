package com.dthvinh.Server.Lib.SummerBoot.Filters;

import com.dthvinh.Server.Lib.SummerBoot.Mornitoring.Logger;
import com.sun.net.httpserver.Filter;

public abstract class BaseFilter extends Filter {
    protected final Logger logger = Logger.getInstance();
}
