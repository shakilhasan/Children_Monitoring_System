package com.example.authapplication;

public class History{
    private  String id;
    private  String domain;
    private String url;



    public History(){}
   /// public History(ShowHistory showHistory, List<History> historyList) {}

    public History(String id,String domain, String url) {
        this.domain = domain;
        this.url = url;
        this.id = id;
    }

    public History(String domain, String url) {
        this.domain = domain;
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public String getUrl() {
        return url;
    }
    public  String getId(){
        return  id;
    }

}

