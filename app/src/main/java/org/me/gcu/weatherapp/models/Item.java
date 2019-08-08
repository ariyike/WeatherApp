/**
 * NAME: ANJOLAOLUWA ARIYIKE ADETIMEHIN
 * STUDENT NO.: S1719003
 * **/

package org.me.gcu.weatherapp.models;

import java.io.Serializable;

public class Item implements Serializable {

        public final String title;
        public final String link;
        public final String pubDate;
        public final String desc;


        public Item(String title, String link, String pubDate, String desc) {
        this.title = title;
        this.pubDate = pubDate;
        this.link = link;
        this.desc = desc;
        }
}


