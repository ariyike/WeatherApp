/**
 * NAME: ANJOLAOLUWA ARIYIKE ADETIMEHIN
 * STUDENT NO.: S1719003
 * **/

package org.me.gcu.weatherapp.utils;

import android.util.Xml;

import org.me.gcu.weatherapp.models.Item;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//THIS CLASS WAS WRITTEN USING THE OFFICIAL ANDROID XMLPULLPARSER DOCUMENTATION: https://developer.android.com/training/basics/network-ops/xml.html#java

public class WeatherXMLParser {
    private static final String ns = null; //setting the namespace as null across the class

    //takes in an input stream and parses the xml in that stream
    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equalsIgnoreCase("item")) {
                entries.add(readEntry(parser));
            } else {
//                skip(parser);
            }
        }
        return entries;
    }


    // Parses the contents of an entry. If it encounters a title, pubDate, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Item readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String pubDate = null;
        String link = null;
        String desc = null;
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
                System.out.println(title);
            }
            else if (name.equals("pubDate")) {
                pubDate = readPubDate(parser);
            }
            else if (name.equals("link")) {
                parser.nextText();
                link=parser.getText();
            }
            else if (name.equals("description")) {
                desc = readDesc(parser);
            }
            else {
                skip(parser);
            }
        }
        return new Item (title, link, pubDate, desc);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes description tags in the feed.
    private String readDesc(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");

        String description = readText(parser);

        String[] descArray = description.split(",", 9);
        descArray = Arrays.copyOf(descArray, descArray.length-1); //then delete the last part


//        String desc = Arrays.toString(descArray).replace(",", "");
        String desc = Arrays.toString(descArray);

        parser.require(XmlPullParser.END_TAG, ns, "description");
        return desc;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")){
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // Processes pubDate tags in the feed.
    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
//        String pubDate = readText(parser);
        String pub = readText(parser);
        String[] pubArray = pub.split(" ", 5); //first split the string with the date
        pubArray = Arrays.copyOf(pubArray, pubArray.length-1); //then delete the last part


        String pubDate = Arrays.toString(pubArray).replace(",", ""); //convert the array to a string

        System.out.println(pubDate);

        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return pubDate;
    }

    // For the tags title and pubDate, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
