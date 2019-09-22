/*
 * Project: ipcommunity
 * File: BaseSearchCondition.java
 * Date: 2018/7/19
 * Copyright 2018 合肥龙图腾信息技术有限公司版权所有
 */
package com.soft.eva.dto;

import java.io.Serializable;

/**
 * 查询条件基础类
 */
public class BaseSearchCondition implements Serializable {
    private static final long serialVersionUID = 8273800701051690401L;
    /**
     * 搜索关键字
     */
    private String keyword;
    /**
     * 当前页
     */
    private Integer page;
    /**
     * 每页数据条数
     */
    private Integer limit;



    public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "BaseSearchCondition{" +
                "keyword='" + keyword + '\'' +
                ", page=" + page +
                ", limit=" + limit +
                '}';
    }


}
