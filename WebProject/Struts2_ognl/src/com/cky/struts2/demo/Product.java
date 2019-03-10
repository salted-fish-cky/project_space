package com.cky.struts2.demo;

public class Product {

    private int ProductId;
    private String ProductName;
    private String ProductDesc;
    private int ProductPrice;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductDesc() {
        return ProductDesc;
    }

    public void setProductDesc(String productDesc) {
        ProductDesc = productDesc;
    }

    public int getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(int ProductPrice) {
        ProductPrice = ProductPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "ProductId=" + ProductId +
                ", ProductName='" + ProductName + '\'' +
                ", ProductDesc='" + ProductDesc + '\'' +
                ", ProdectPrice='" + ProductPrice + '\'' +
                '}';
    }

    public String testTag(){
        setProductId(1001);
        setProductName("productName");
        setProductDesc("desc");
        setProductPrice(100);

        return "success";
    }
}
