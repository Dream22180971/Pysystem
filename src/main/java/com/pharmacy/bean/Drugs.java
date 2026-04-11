package com.pharmacy.bean;

/**
 * 药品信息实体类
 * 对应数据库表：drugs
 */
public class Drugs {
    /**
     * 药品ID
     */
    private Integer id;
    /**
     * 药品名称
     */
    private String drugsName;
    /**
     * 药品编号
     */
    private Integer nums;
    /**
     * 药品图片路径
     */
    private String drugsImage;
    /**
     * 药品分类ID
     */
    private Integer categoryId;
    /**
     * 价格
     */
    private Double price;
    /**
     * 适用人群
     */
    private String people;
    /**
     * 使用方法
     */
    private String useMethod;
    /**
     * 柜台ID
     */
    private Integer cid;
    /**
     * 仓库ID
     */
    private Integer rid;
    /**
     * 生产日期
     */
    private String productTime;
    /**
     * 保质期(月)
     */
    private Integer saveTime;
    /**
     * 状态（1上架 0下架）
     */
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDrugsName() {
        return drugsName;
    }

    public void setDrugsName(String drugsName) {
        this.drugsName = drugsName;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public String getDrugsImage() {
        return drugsImage;
    }

    public void setDrugsImage(String drugsImage) {
        this.drugsImage = drugsImage;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getUseMethod() {
        return useMethod;
    }

    public void setUseMethod(String useMethod) {
        this.useMethod = useMethod;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

    public Integer getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Integer saveTime) {
        this.saveTime = saveTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}