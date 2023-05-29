package com.example.leaderboard;

public class PersonalRank {
    private  String mydivision, myusername;
    private int myintegral;

    public PersonalRank(String mydivision, String myusername, int myintegral) {
        this.mydivision = mydivision;
        this.myusername = myusername;
        this.myintegral = myintegral;
    }

    public String getMydivision() { return mydivision; }

    public String getMyusername() { return myusername; }

    public int getMyintegral() { return myintegral; }
}
