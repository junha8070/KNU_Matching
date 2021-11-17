package com.example.knu_matching;

import java.util.Date;

public class postInfo2 {

    private String str_Nickname2;
    private String str_email2;
    private String str_comment2;
    private String str_time;

    public postInfo2(String str_email2, String str_comment2, String str_Nickname2, String str_time){
        this.str_email2 = str_email2;
        this.str_comment2 = str_comment2;
        this.str_Nickname2 = str_Nickname2;
        this.str_time = str_time;
    }

    public String getStr_email2() {
        return str_email2;
    }
    public void setStr_email2(String str_email2) {
        this.str_email2 = str_email2;
    }

    public String getStr_Nickname2(){return this.str_Nickname2;}
    public void setStr_Nicknamed2(String str_Nickname2){this.str_Nickname2 = str_Nickname2;}

    public String getStr_comment2(){return this.str_comment2;}
    public void setStr_comment2(String str_date2){this.str_comment2 = str_comment2;}

    public String getStr_time() {
        return str_time;
    }
    public void setStr_time(String str_time) {
        this.str_time = str_time;
    }

}
