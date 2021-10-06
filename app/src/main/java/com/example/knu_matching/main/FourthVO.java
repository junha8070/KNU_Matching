package com.example.knu_matching.main;

public class FourthVO {
    private String authKey;//인증키
    private String startPage;//시작위치 기본1
    private String display;//출력건수 기본10
    //검색조건 게시판
    private String type;// A:지원정책, B:교육훈련, C:대외활동, D:창업지원, E:공공일자리, F:탐나는기업, G:채용정보
    //검색조건 > 공통(채용정보 제외)
    private String srchLegalCd;//지역 - 샘플에 코드값 안내 후 코드값으로(잡아바와 동일하게) > !!!공공일자리는 텍스트로 받아서 한개의 검색값만.
    //검색조건 > 지원정책, 교육훈련, 대외활동, 창업
    private String srchLfcCd;	//대상별 (생애주기) > 수혜자1 - 샘플에 코드값 안내 후 코드값으로(잡아바와 동일하게) > 지원,교육,대외만 존재
    private String srchSpclzTrgetCd; //대상별 (특화대상) > 수혜자2 - 샘플에 코드값 안내 후 코드값으로(잡아바와 동일하게) > 지원,교육,대외만 존재
    private String schCl1Cd;	//카테고리(각메뉴별) - - 샘플에 코드값 안내 후 코드값으로(잡아바와 동일하게)

    // 검색조건 > 채용정보
    private String srchCareer; 	// 경력 - 샘플에 코드값 안내 후 코드값으로(잡아바와 동일하게)
    private String srchAcdmcr; 	// 학력 - 샘플에 코드값 안내 후 코드값으로(잡아바와 동일하게)
    private String srchArea;	// 지역 - 샘플에 코드값 안내 후 코드값으로(잡아바와 동일하게)
    private String srchEmpTpCd;	// 공고형태 - 샘플에 코드값 안내 후 코드값으로(잡아바와 동일하게)
    private String srchClCd;	// 모집분야 - 샘플에 코드값 안내 후 코드값으로(잡아바와 동일하게)

    public String getAuthKey() {
        return authKey;
    }
    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
    public String getStartPage() {
        return startPage;
    }
    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }
    public String getDisplay() {
        return display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSrchLegalCd() {
        return srchLegalCd;
    }
    public void setSrchLegalCd(String srchLegalCd) {
        this.srchLegalCd = srchLegalCd;
    }
    public String getSrchLfcCd() {
        return srchLfcCd;
    }
    public void setSrchLfcCd(String srchLfcCd) {
        this.srchLfcCd = srchLfcCd;
    }
    public String getSrchSpclzTrgetCd() {
        return srchSpclzTrgetCd;
    }
    public void setSrchSpclzTrgetCd(String srchSpclzTrgetCd) {
        this.srchSpclzTrgetCd = srchSpclzTrgetCd;
    }
    public String getSchCl1Cd() {
        return schCl1Cd;
    }
    public void setSchCl1Cd(String schCl1Cd) {
        this.schCl1Cd = schCl1Cd;
    }

    public String getSrchCareer() {
        return srchCareer;
    }
    public void setSrchCareer(String srchCareer) {
        this.srchCareer = srchCareer;
    }
    public String getSrchAcdmcr() {
        return srchAcdmcr;
    }
    public void setSrchAcdmcr(String srchAcdmcr) {
        this.srchAcdmcr = srchAcdmcr;
    }
    public String getSrchArea() {
        return srchArea;
    }
    public void setSrchArea(String srchArea) {
        this.srchArea = srchArea;
    }
    public String getSrchEmpTpCd() {
        return srchEmpTpCd;
    }
    public void setSrchEmpTpCd(String srchEmpTpCd) {
        this.srchEmpTpCd = srchEmpTpCd;
    }
    public String getSrchClCd() {
        return srchClCd;
    }
    public void setSrchClCd(String srchClCd) {
        this.srchClCd = srchClCd;
    }

}
