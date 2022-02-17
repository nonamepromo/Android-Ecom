package com.example.justplay.Model;

public class Wished {

    private String gameName;
    private String gameConsole;
    private int id;

    public Wished(){
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameConsole() {
        return gameConsole;
    }

    public void setGameConsole(String gameConsole) {
        this.gameConsole = gameConsole;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Wished(String gameName, String gameConsole, String gamePrice) {
        this.gameName = gameName;
        this.gameConsole = gameConsole;
    }

}

