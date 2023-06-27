package com.zrapp.warehouse.model;

public class Product {
    private String id;
    private String name;
    private String viTri;
    private int price;
    private int cost_price;
    private String img;

    public Product(String id, String name, int price, int cost_price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cost_price = cost_price;
    }

    public Product(String id, String name, String viTri, int price, int cost_price, String img) {
        this.id = id;
        this.name = name;
        this.viTri = viTri;
        this.price = price;
        this.cost_price = cost_price;
        this.img = img;
    }

    public Product() {
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCost_price() {
        return cost_price;
    }

    public void setCost_price(int cost_price) {
        this.cost_price = cost_price;
    }
}
