package com.example.worktracker_pog_sales;

public class DataClass {

    String Date, Month, Type, Inventory;

    public String getDate() {
        return Date;
    }

    public String getMonth() {
        return Month;
    }

    public String getType() {
        return Type;
    }

    public String getInventory() {
        return Inventory;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public void setMonth(String Month) {
        this.Month = Month;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public void setInventory(String Inventory) {
        this.Inventory = Inventory;
    }

    public DataClass(String Date, String Month, String Type, String Inventory) {
        this.Date = Date;
        this.Month = Month;
        this.Type = Type;
        this.Inventory = Inventory;
    }
}
