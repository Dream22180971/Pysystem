package com.pharmacy.bean;

/**
 * 仓库信息实体类
 * 对应数据库表：repertory
 */
public class Repertory {
    /**
     * 仓库ID
     */
    private Integer rid;
    /**
     * 仓库位置
     */
    private String place;
    /**
     * 状态
     */
    private Integer status;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
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