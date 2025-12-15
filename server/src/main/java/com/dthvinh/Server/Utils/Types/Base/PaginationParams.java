package com.dthvinh.Server.Utils.Types.Base;

public class PaginationParams {
    public int limit;
    public int offset;

    public static PaginationParams create(int limit, int offset) {
        PaginationParams p = new PaginationParams();
        p.limit = limit;
        p.offset = offset;

        return p;
    }
}
