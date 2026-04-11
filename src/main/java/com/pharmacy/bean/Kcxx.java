package com.pharmacy.bean;

/**
 * 药品库存实体类
 * 对应数据库表：kcxx
 */
public class Kcxx {
    /**
     * 库存ID
     */
    private Integer kid;
    /**
     * 药品名称
     */
    private String drugsName;
    /**
     * 库存数量
     */
    private Integer num;
    /**
     * 仓库ID
     */
    private Integer rid;
    /**
     * 备注
     */
    private String marks;

    public Integer getKid() {
        return kid;
    }

    public void setKid(Integer kid) {
        this.kid = kid;
    }

    public String getDrugsName() {
        return drugsName;
    }

    public void setDrugsName(String drugsName) {
        this.drugsName = drugsName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}