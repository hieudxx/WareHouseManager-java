package com.zrapp.warehouse.model;

public class Staff {
    private String id, name, username, pass, tel, img, post;
    private boolean status;

    public Staff() {
    }

    public Staff(String id, String name, String username, String pass, String tel, String img, String post, boolean status) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.pass = pass;
        this.tel = tel;
        this.img = img;
        this.post = post;
        this.status = status;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
