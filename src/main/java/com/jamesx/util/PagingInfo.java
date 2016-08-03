package com.jamesx.util;

/**
 * Created by jamesx on 6/24/2016.
 */
public class PagingInfo {
    public Integer start;
    public Integer pageSize;
    public Integer totalCount;
    public String orderBy;
    public String asc;
    public Object serverData;

    public PagingInfo() { }

    public PagingInfo(int start, int pageSize, String orderBy, String asc) {
        this.start = start;
        this.pageSize = pageSize;
        //this.totalCount = totalCount;
        this.orderBy = orderBy;
        this.asc = asc;
    }
}
