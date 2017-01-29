package com.codelite.kr4k3rz.samachar24;

import java.util.ArrayList;

class NewspaperNP {
    private static final String front = "https://query.yahooapis.com/v1/public/yql?q=select * from xml where url = '";
    private static final String end = "'&format=json";
    private final ArrayList<NewspaperNP> newspapers = new ArrayList<>();
    private String name;
    private String link;

    private NewspaperNP(String name, String link) {
        this.name = name;
        this.link = link;
    }

    NewspaperNP() {

        newspapers.add(new NewspaperNP("OnlineKhabar", front + "http://www.onlinekhabar.com/feed" + end));//
        newspapers.add(new NewspaperNP("NepaliHeadlines", front + "http://nepaliheadlines.com/feed" + end));//
        // newspapers.add(new NewspaperNP("NayaSamachar", front + "http://nayasamachar.com/?feed=rss2" + end));
        newspapers.add(new NewspaperNP("SambadMedia", front + "http://www.sambadmedia.com/?feed=rss2" + end));//
        newspapers.add(new NewspaperNP("TajaOnlinekhabar", front + "http://www.tajaonlinekhabar.com/feed" + end));//
        //newspapers.add(new NewspaperNP("Lokaantar", front + "http://lokaantar.com/feed" + end));//
        newspapers.add(new NewspaperNP("MediaNp", front + "http://medianp.com/feed" + end));//
        newspapers.add(new NewspaperNP("eNepaliKhabar", front + "http://www.enepalikhabar.com/feed" + end));//
        newspapers.add(new NewspaperNP("NepalAaja", front + "http://nepalaaja.com/feed" + end));//
        // newspapers.add(new NewspaperNP("ONSNepal", front + "http://www.onsnews.com/feed" + end));
        // newspapers.add(new NewspaperNP("NayaPage", front + "http://www.nayapage.com/feed" + end));
        newspapers.add(new NewspaperNP("SouryaDaily", front + "http://www.souryadaily.com/feed" + end));//
        newspapers.add(new NewspaperNP("RajdhaniDaily", front + "http://rajdhanidaily.com/feed" + end));//
        newspapers.add(new NewspaperNP("Chardisa", front + "http://chardisha.com/feed" + end));//
        // newspapers.add(new NewspaperNP("NepaliHealth", front + "http://www.nepalihealth.com/feed" + end));//
        newspapers.add(new NewspaperNP("BizKhabar", front + "http://www.bizkhabar.com/feed" + end));//
        newspapers.add(new NewspaperNP("EverestDainik", front + "http://www.everestdainik.com/feed" + end));
        // newspapers.add(new NewspaperNP("KathmanduToday", front + "http://kathmandutoday.com/feed" + end));


    }


    public ArrayList<NewspaperNP> getNewspapersList() {
        return newspapers;
    }

    ArrayList<String> getLinksList() {
        ArrayList<String> linksList = new ArrayList<>();
        for (int i = 0; i < newspapers.size(); i++) {
            NewspaperNP newspaper = newspapers.get(i);
            linksList.add(newspaper.link);
        }
        return linksList;
    }


}
