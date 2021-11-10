package com.example.knu_matching;

import java.util.Date;

public class postInfo2 {

    private String str_Nickname;
    private String str_email;
    private String str_comment;

    public postInfo2(String str_email, String str_comment, String str_Nickname){
        this.str_Nickname = str_Nickname;
        this.str_email = str_email;
        this.str_comment = str_comment;
    }

    public String getStr_email() {
        return str_email;
    }
    public void setStr_email(String str_email) {
        this.str_email = str_email;
    }

    public String getStr_Nickname(){return this.str_Nickname;}
    public void setStr_Nicknamed(String str_Nickname){this.str_Nickname = str_Nickname;}

    public String getStr_comment(){return this.str_comment;}
    public void setStr_comment(String str_date){this.str_comment = str_comment;}

}
