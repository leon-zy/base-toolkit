package com.insaic.toolkit.model;

import java.io.Serializable;

/**
 * PageParamMO
 * Created by leon_yan on 2018/4/19
 */
public abstract class PageParamMO implements Serializable {

    private static final long serialVersionUID = 1530387788727380086L;

    //当前页
    private Integer pageNumber;
    //每页大小
    private Integer pageSize;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}