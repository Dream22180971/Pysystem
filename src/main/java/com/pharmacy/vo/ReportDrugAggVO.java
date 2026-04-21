package com.pharmacy.vo;

/**
 * 药品维度报表聚合 VO：可用于销售/采购/库存等。
 */
public class ReportDrugAggVO {
    private String drugsName;
    private int qty;
    /**
     * 金额（销售额等）；无金额口径时为 0
     */
    private double amount;

    public String getDrugsName() {
        return drugsName;
    }

    public void setDrugsName(String drugsName) {
        this.drugsName = drugsName;
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

