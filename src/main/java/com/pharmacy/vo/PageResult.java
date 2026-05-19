package com.pharmacy.vo;

import java.util.List;

/**
 * 通用分页返回：items + total。
 * page/size 由调用方（Controller）自行回传或在前端维护即可。
 */
public class PageResult<T> {
    private List<T> items;
    private long total;

    public PageResult() {
    }

    public PageResult(List<T> items, long total) {
        this.items = items;
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}

