package com.example.rohit.top10downloader;

import android.util.Log;
import android.widget.Switch;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by ROHIT on 23-02-2018.
 */

public class ParseApplications {
    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry> applications;

    public ParseApplications() {
        this.applications=new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public boolean parse(String xmlData){
        boolean status=true;
        FeedEntry currentRecord=null;
        boolean inEntry=false;
        String textValue="";

        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp=factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType=xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                String tagName=xpp.getName();
//                Log.d(TAG, "parse: This is the Tag Name  "+tagName);
                switch (eventType){
                    case XmlPullParser.START_TAG:
//                        Log.d(TAG, "parse: Starting tag for"+tagName);
                        if("entry".equalsIgnoreCase(tagName)){
                            inEntry=true;
                            currentRecord=new FeedEntry();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue=xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
//                        Log.d(TAG, "parse: Ending tag for"+tagName);
                        if(inEntry){
                            if ("entry".equalsIgnoreCase(tagName)){
                                applications.add(currentRecord);
                                inEntry=false;
                            }else if ("name".equalsIgnoreCase(tagName)){
                                currentRecord.setName(textValue);
                            }else  if ("artist".equalsIgnoreCase(tagName)){
                                currentRecord.setArtist(textValue);
                            }else  if ("releaseDate".equalsIgnoreCase(tagName)){
                                currentRecord.setReleaseDate(textValue);
                            }else  if ("Summary".equalsIgnoreCase(tagName)){
                                currentRecord.setSummary(textValue);
                            }else  if ("image".equalsIgnoreCase(tagName)){
                                currentRecord.setImageUrl(textValue);
                            }
                        }
                        break;
                        default:
                            //nothing else to do
                }
                eventType=xpp.next();
            }
//            for (FeedEntry app:applications){
//                Log.d(TAG, "**********");
//                Log.d(TAG, app.toString());
//            }
        }catch (Exception e){
            status=false;
            e.printStackTrace();
        }
        return status;
    }
}
