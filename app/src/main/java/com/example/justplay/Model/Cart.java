package com.example.justplay.Model;

public class Cart {

    private String gId, gName, price, discount, console;

    public Cart(){
    }

    public Cart(String gId, String gName, String price, String discount, String console) {
        this.gId = gId;
        this.gName = gName;
        this.price = price;
        this.discount = discount;
        this.console = console;
    }

    public String getGid() {
        return gId;
    }

    public void setGid(String gId) {
        this.gId = gId;
    }

    public String getGname() {
        return gName;
    }

    public void setGname(String gName) {
        this.gName = gName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }
}
