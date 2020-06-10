package com.example.myapplication;

public class PatientListData {
    private String Score;
    private String Pname;
    private String Pnum;
    private String Cname;
    private String Date;

    public PatientListData(String Score, String Pname, String Pnum, String Cname, String Date) {
        this.Score = Score;
        this.Pname = Pname;
        this.Pnum = Pnum;
        this.Cname = Cname;
        this.Date = Date;
    }

    public String getScore() {
        return this.Score;
    }

    public String getPnum() {
        return this.Pnum;
    }

    public String getPname() {
        return this.Pname;
    }

    public String getCname() {
        return this.Cname;
    }

    public String getDate() {
        return Date;
    }
}
