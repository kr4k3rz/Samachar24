package com.codelite.kr4k3rz.samachar24;


import java.util.ArrayList;

class NewspaperEN {
    private final ArrayList<NewspaperEN> newspapers = new ArrayList<>();
    private String name;
    private String link;
    private static  final String front = "https://api.rss2json.com/v1/api.json?rss_url=";
    private static final String end = "&api_key=7knsnj8fs3fkrshezfy84jkhihkch6ovvfenagkh&order_by=&order_dir=desc&count=20";

    private NewspaperEN(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public NewspaperEN() {
        newspapers.add(new NewspaperEN("TheHimalayanTimes", front + "http://thehimalayantimes.com/feed" + end));
        newspapers.add(new NewspaperEN("OnlineKhabar", front + "http://english.onlinekhabar.com/feed" + end));
        newspapers.add(new NewspaperEN("DainikPost", front + "http://dainikpost.com/feed" + end));
    }


    public ArrayList<NewspaperEN> getNewspapersList() {
        return newspapers;
    }

    public ArrayList<String> getLinksList() {
        ArrayList<String> linksList = new ArrayList<>();
        for (int i = 0; i < newspapers.size(); i++) {
            NewspaperEN newspaper = newspapers.get(i);
            linksList.add(newspaper.link);
        }
        return linksList;
    }


}
