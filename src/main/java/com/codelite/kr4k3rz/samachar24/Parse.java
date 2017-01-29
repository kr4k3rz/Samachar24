package com.codelite.kr4k3rz.samachar24;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;

import com.codelite.kr4k3rz.samachar24.model.ItemItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


/* a class that handles parsing*/
class Parse {

    static final EmptyImageGetter EMPTY_IMAGE_GETTER = new EmptyImageGetter();

    static String parseImg(String html) {
        String url = null;
        if (html != null) {
            Document doc = Jsoup.parse(html);
            Element imageElement = doc.select("img").first();

            if (!(imageElement == null)) {
                url = imageElement.attr("src");
            }
        } else {
            Log.d("TAG", "Image is Null");
        }


        return url;
    }

    static String getSource(String link) throws MalformedURLException {
        URL myUrl = new URL(link);
        String check = myUrl.getHost();
        int dot1, dot2;
        dot1 = check.indexOf(".");
        if (dot1 == -1) {
            return "";
        }
        dot2 = check.indexOf(".", dot1 + 1);
        if (dot2 == -1) {
            return check.substring(0, dot1);
        }
        return "" + check.substring(dot1 + 1, dot2);

    }


    static String convertImgUrl(String url) throws MalformedURLException {
        if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
            if (url.toLowerCase().contains(".png".toLowerCase())) {
                URL url1 = new URL(url);
                String tempUrl = url1.getHost() + ".rsz.io" + url1.getPath() + "?format=jpg";
                url = "http://images.weserv.nl/?url=" + tempUrl + "&w=300&h=300&q=10";
                Log.i("PNG TAG", "" + url);
            } else {
                url = "http://images.weserv.nl/?url=" + url.replace("http://", "") + "&w=300&h=300&q=10";
                Log.i("TAG", " String to be shows : " + url);
            }
        } else Log.i("TAG", " String is null");
        return url;
    }

    static List<ItemItem> sortByTime(List<ItemItem> entries) {

        Collections.sort(entries, new Comparator<ItemItem>() {
            @Override
            public int compare(ItemItem entry, ItemItem t1) {
                long d1 = 0;
                long d2 = 0;
                @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                try {
                    d1 = formatter.parse(entry.getPubDate()).getTime();
                    d2 = formatter.parse(t1.getPubDate()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return (d1 > d2) ? -1 : 1;
            }
        });
        return entries;
    }

    static List<ItemItem> deleteDuplicate(List<ItemItem> entries) {
        ArrayList<ItemItem> newFeeds = new ArrayList<>();
        Set<ItemItem> hs = new HashSet<>();
        if (entries != null)
            hs.addAll(entries);
        newFeeds.clear();
        newFeeds.addAll(hs);
        return newFeeds;
    }

    static String convertLongDateToAgoString(Long createdDate, Long timeNow) {
        Long timeElapsed = timeNow - createdDate;

        // For logging in Android for testing purposes
        /*
        Date dateCreatedFriendly = new Date(createdDate);
        Log.d("MicroR", "dateCreatedFriendly: " + dateCreatedFriendly.toString());
        Log.d("MicroR", "timeNow: " + timeNow.toString());
        Log.d("MicroR", "timeElapsed: " + timeElapsed.toString());*/

        // Lengths of respective time durations in Long format.
        Long oneMin = 60000L;
        Long oneHour = 3600000L;
        Long oneDay = 86400000L;
        Long oneWeek = 604800000L;

        String finalString = "Now";
        String unit;

        if (timeElapsed < oneMin) {
            // Convert milliseconds to seconds.
            double seconds = (double) ((timeElapsed / 1000));
            // Round up
            seconds = Math.round(seconds);
            // Generate the friendly unit of the ago time
            if (seconds == 1) {
                unit = "now";
            } else {
                unit = "now";
            }
            finalString = unit;
        } else if (timeElapsed < oneHour) {
            double minutes = (double) ((timeElapsed / 1000) / 60);
            minutes = Math.round(minutes);
            if (minutes == 1) {
                unit = "m";
            } else {
                unit = "m";
            }
            finalString = String.format(Locale.ENGLISH, "%.0f", minutes) + unit;
        } else if (timeElapsed < oneDay) {
            double hours = (double) ((timeElapsed / 1000) / 60 / 60);
            hours = Math.round(hours);
            if (hours == 1) {
                unit = "h";
            } else {
                unit = "h";
            }
            finalString = String.format(Locale.ENGLISH, "%.0f", hours) + unit;
        } else if (timeElapsed < oneWeek) {
            double days = (double) ((timeElapsed / 1000) / 60 / 60 / 24);
            days = Math.round(days);
            if (days == 1) {
                unit = "d";
            } else {
                unit = "d";
            }
            finalString = String.format(Locale.ENGLISH, "%.0f", days) + unit;
        } else if (timeElapsed > oneWeek) {
            double weeks = (double) ((timeElapsed / 1000) / 60 / 60 / 24 / 7);
            weeks = Math.round(weeks);
            if (weeks == 1) {
                unit = "w";
            } else {
                unit = "w";
            }
            finalString = String.format(Locale.ENGLISH, "%.0f", weeks) + unit;
        }
        return finalString;
    }

    static List<ItemItem> deleteEnglishFeeds(List<ItemItem> entries) {
        for (int i = 0; i < entries.size(); i++) {
            boolean atLeastOneAlpha = entries.get(i).getTitle().matches(".*[a-zA-Z]+.*");
            if (atLeastOneAlpha) {
                entries.remove(i);
            }
        }
        return entries;
    }

    public static List<ItemItem> deleteNonEngFeeds(List<ItemItem> entries) {
        ArrayList<ItemItem> tempList = new ArrayList<>();
        tempList.clear();
        for (ItemItem e : entries) {
            String s = e.getTitle();
            boolean atLeastOneAlpha = s.matches(".*[a-zA-Z]+.*");
            if (atLeastOneAlpha) {
                tempList.add(e);
            }
        }
        return tempList;
    }

    static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
    }

    private static class EmptyImageGetter implements Html.ImageGetter {
        private static final Drawable TRANSPARENT_DRAWABLE = new ColorDrawable(Color.TRANSPARENT);

        @Override
        public Drawable getDrawable(String source) {
            return TRANSPARENT_DRAWABLE;
        }
    }

}