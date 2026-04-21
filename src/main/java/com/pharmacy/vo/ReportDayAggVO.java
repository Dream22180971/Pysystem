package com.pharmacy.vo;

/**
 * 日期维度报表聚合 VO：用于趋势报表（按天汇总）。
 */
public class ReportDayAggVO {
    /**
     * yyyy-MM-dd
     */
    private String day;
    private int qty;
    private double amount;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

