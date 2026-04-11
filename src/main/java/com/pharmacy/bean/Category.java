package com.pharmacy.bean;

/**
 * 药品分类实体类
 * 对应数据库表：category
 */
public class Category {
    /**
     * 分类ID
     */
    private Integer categoryId;
    /**
     * 药品分类名称
     */
    private String categoryName;
    /**
     * 状态（1可用 0不可用）
     */
    private Integer status;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}