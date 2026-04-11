package com.pharmacy.vo;

/**
 * 饼图数据VO类
 * 用于前端ECharts饼图数据展示
 */
public class PieVO {
    /**
     * 名称
     */
    private String name;
    /**
     * 数值
     */
    private int value;

    public PieVO() {
    }

    /**
     * 构造方法
     * @param name 名称
     * @param value 数值
     */
    public PieVO(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}