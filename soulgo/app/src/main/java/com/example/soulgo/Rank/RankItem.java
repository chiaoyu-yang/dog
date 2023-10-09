package com.example.soulgo.Rank;

public class RankItem {
    private int division;
    private String nickname;
    private int points;

    public RankItem(int division, String nickname, int points) {
        this.division = division;
        this.nickname = nickname;
        this.points = points;
    }

    public int getDivision() {
        return division;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPoints() {
        return points;
    }
}
