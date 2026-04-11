package com.pharmacy.bean;

/**
 * 角色信息实体类
 * 对应数据库表：part
 */
public class Part {
    /**
     * 角色ID
     */
    private Integer pId;
    /**
     * 角色名称（管理员/员工）
     */
    private String pName;
    /**
     * 状态
     */
    private Integer status;

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}