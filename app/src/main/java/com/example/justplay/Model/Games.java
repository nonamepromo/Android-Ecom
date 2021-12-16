package com.example.justplay.Model;

public class Games {
    private String gName, description, price, image, console, gId, date, time;

    public Games(){
    }

    public Games(String gName, String description, String price, String image, String console, String gId, String date, String time) {
        this.gName = gName;
        this.description = description;
        this.price = price;
        this.image = image;
        this.console = console;
        this.gId = gId;
        this.date = date;
        this.time = time;
    }

    public String getGname() { return gName; }

    public void setGname(String gName) {
        this.gName = gName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public String getGid() {
        return gId;
    }

    public void setGid(String gId) {
        this.gId = gId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
