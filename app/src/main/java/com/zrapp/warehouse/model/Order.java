package com.zrapp.warehouse.model;

public class Order {
    private String id_order;
    private String id_staff;
    private String kindOfOrder;
    private String date;

    public Order(String id_order, String kindOfOrder) {
        this.id_order = id_order;
        this.kindOfOrder = kindOfOrder;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getId_staff() {
        return id_staff;
    }

    public void setId_staff(String id_employ) {
        this.id_staff = id_employ;
    }

    public String getKindOfOrder() {
        return kindOfOrder;
    }

    public void setKindOfOrder(String kindOfOrder) {
        this.kindOfOrder = kindOfOrder;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Order() {
    }
}
