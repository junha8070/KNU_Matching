package com.example.knu_matching.Post;

import java.util.Date;

public class postInfo {
    private String str_Title;
    private String str_date;
    private String str_EndDate;
    private String str_Number;
    private String str_post;
    private String str_time;
    private String str_Nickname;
    private String str_email;
    private String str_Id;
    private String str_application;



    public postInfo(String str_Title, String str_date, String str_EndDate, String str_Number, String str_post, String str_time, String str_Nickname, String str_email, String str_Id, String str_application){
        this.str_Title = str_Title;
        this.str_date = str_date;
        this.str_EndDate = str_EndDate;
        this.str_Number = str_Number;
        this.str_post = str_post;
        this.str_time = str_time;
        this.str_Nickname = str_Nickname;
        this.str_email = str_email;
        this.str_Id = str_Id;
        this.str_application = str_application;

    }

    public String getStr_EndDate() {
        return str_EndDate;
    }

    public void setStr_EndDate(String str_EndDate) {
        this.str_EndDate = str_EndDate;
    }

    public String getStr_Title(){return this.str_Title;}

    public String getStr_email() {
        return str_email;
    }

    public void setStr_email(String str_email) {
        this.str_email = str_email;
    }

    public void setStr_Title(String str_Title){this.str_Title = str_Title;}

    public String getStr_date(){return this.str_date;}
    public void setStr_date(String str_date){this.str_date = str_date;}

    public String getStr_Number(){return this.str_Number;}
    public void setStr_Number(String str_Number){this.str_Number = str_Number;}

    public String getStr_post(){return this.str_post;}
    public void setStr_post(String str_post){this.str_post = str_post;}

    public String getStr_time() {
        return str_time;
    }
    public void setStr_time(String str_time) {
        this.str_time = str_time;
    }

    public String getStr_Nickname(){return this.str_Nickname;}
    public void setStr_Nickname(String str_Nickname){this.str_Nickname = str_Nickname;}

    public String getStr_Id(){return this.str_Id;}
    public void setStr_Id(String str_Id){this.str_Id = str_Id;}

    public String getStr_application(){return this.str_application;}
    public void setStr_application(String str_application){this.str_application = str_application;}


}
