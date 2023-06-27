package com.zrapp.warehouse.model;

public class OrderDetails {
    private Product prod;
    private Order order;
    private int qty;

    public OrderDetails() {
    }

    public OrderDetails(Product prod, Order order, int qty) {
        this.prod = prod;
        this.order = order;
        this.qty = qty;
    }

    public Product getProd() {
        return prod;
    }

    public void setProd(Product prod) {
        this.prod = prod;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
