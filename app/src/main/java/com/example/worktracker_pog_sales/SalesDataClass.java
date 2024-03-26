package com.example.worktracker_pog_sales;

public class SalesDataClass {

    String salesDate, customer, product;
    int unit;
    double price, value;

    public String getSalesDate() {
        return salesDate;
    }

    public String getCustomer() {
        return customer;
    }

    public String getProduct() {
        return product;
    }

    public int getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public double getValue() {
        return value;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public SalesDataClass(String salesDate, String customer, String product, int unit, double price, double value) {
        this.salesDate = salesDate;
        this.customer = customer;
        this.product = product;
        this.unit = unit;
        this.price = price;
        this.value = value;
    }
}
