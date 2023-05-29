package com.example.leaderboard;

public class Ranklist {
    private  String division, username;
    private int integral;

    public Ranklist(String division, String username, int integral) {
        this.division = division;
        this.username = username;
        this.integral = integral;
    }

    public String getDivision() { return division; }

    public String getUsername() { return username; }

    public int getIntegral() { return integral; }
}
