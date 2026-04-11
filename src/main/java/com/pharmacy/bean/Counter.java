package com.pharmacy.bean;

/**
 * 柜台信息实体类
 * 对应数据库表：counter
 */
public class Counter {
    /**
     * 柜台ID
     */
    private Integer cid;
    /**
     * 柜台位置
     */
    private String place;
    /**
     * 状态
     */
    private Integer status;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}