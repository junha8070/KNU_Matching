package com.example.knu_matching;

public class ParticipateUser {
    private String str_participate_Nickname;
    private String str_participate_Major;
    private String str_participate_StudentId;

    public ParticipateUser(String str_participate_Nickname, String str_participate_Major, String str_participate_StudentId){
        this.str_participate_Nickname = str_participate_Nickname;
        this.str_participate_Major = str_participate_Major;
        this.str_participate_StudentId = str_participate_StudentId;
    }

    public String getStr_participate_Nickname(){return this.str_participate_Nickname;}
    public void setStr_participate_Nickname(String str_participate_Nickname){this.str_participate_Nickname = str_participate_Nickname;}

    public String getStr_participate_Major(){return this.str_participate_Major;}
    public void setStr_participate_Major(String str_participate_Major){this.str_participate_Major = str_participate_Major;}

    public String getStr_participate_StudentId(){return this.str_participate_StudentId;}
    public void setStr_participate_StudentId(String str_participate_StudentId){this.str_participate_StudentId = str_participate_StudentId;}


}
