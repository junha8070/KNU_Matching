package com.example.knu_matching;

import java.util.Date;

public class postInfo {
    private String str_Title;
    private String str_date;
    private String str_Number;
    private String str_post;
    private Date date_date;

    public postInfo(String str_Title, String str_date, String str_Number, String str_post){
        this.str_Title = str_Title;
        this.str_date = str_date;
        this.str_Number = str_Number;
        this.str_post = str_post;
        //this.date_date = date_date;
    }
    public String getStr_Title(){return this.str_Title;}
    public void setStr_Title(String str_Title){this.str_Title = str_Title;}
    public String getStr_date(){return this.str_date;}
    public void setStr_date(String str_date){this.str_date = str_date;}
    public String getStr_Number(){return this.str_Number;}
    public void setStr_Number(String str_Number){this.str_Number = str_Number;}
    public String getStr_post(){return this.str_post;}
    public void setStr_post(String str_post){this.str_post = str_post;}
    //public Date getDate_date(){return this.date_date;}
    //public void setDate_date(Date date_date) {this.date_date = date_date;}
}