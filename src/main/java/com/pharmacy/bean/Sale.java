package com.pharmacy.bean;

import java.util.Date;

/**
 * 药品销售实体类
 * 对应数据库表：sale
 */
public class Sale {
    /**
     * 销售ID
     */
    private Integer saleId;
    /**
     * 药品名称
     */
    private String drugsName;
    /**
     * 单价
     */
    private Double price;
    /**
     * 销售数量
     */
    private Integer num;
    /**
     * 总价
     */
    private String total;
    /**
     * 销售日期
     */
    private Date saledate;
    /**
     * 备注
     */
    private String marks;

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public String getDrugsName() {
        return drugsName;
    }

    public void setDrugsName(String drugsName) {
        this.drugsName = drugsName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Date getSaledate() {
        return saledate;
    }

    public void setSaledate(Date saledate) {
        this.saledate = saledate;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}