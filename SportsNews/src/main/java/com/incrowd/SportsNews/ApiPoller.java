package com.incrowd.SportsNews;

import com.mongodb.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/***
 * NOTES: Unfortunately running into errors where there are duplicate entries despite the supposed duplication
 * management I added around lines 68 to 82 in this class. I am new to using MongoDB so would love to hear feedback
 * on how to better manage this. Thanks, Jawad
 */

@Component
public class ApiPoller implements Runnable {

    public ApiPoller(DatabaseAccess dba) {
        collection = dba.getCollection();
        database = dba.getDatabase();
    }

    private final String BASE_URL = "https://www.brentfordfc.com/api/";
    private final String EXTENSION_URL = "incrowd/getnewlistinformation?count=50";

    DBCollection collection;
    DB database;
    OkHttpClient client = new OkHttpClient();

    //Get the raw XML Data from the URL
    public String getXMLData() throws IOException {

        Request request = new Request.Builder()
                .url(BASE_URL + EXTENSION_URL)
                .build();

        Response responses = client.newCall(request).execute();
        return responses.body().string();

    }

    //Parse the retrieved XML into a JSON Object
    private JSONObject parseXMLToJSON(String XMLData) {
        return XML.toJSONObject(XMLData);
    }


    //Add this to the database
    private void insertDataToDB(JSONObject xmlJSONObj) {

        //Turn the JSON Object into a DBObject so we can add it in
        Object o = com.mongodb.util.JSON.parse(xmlJSONObj.toString());
        DBObject dbObj = (DBObject) o;

        //Breakdown the object into its key's and value's
        Map<String, BasicDBObject> newList = (Map) dbObj.get("NewListInformation");

        //Gets all the newsletter items
        BasicDBList newDBList = (BasicDBList) newList.get("NewsletterNewsItems").get("NewsletterNewsItem");

        //Adds the newly retrieved list to the database given it doesn't already exist
        for (Object entry : newDBList) {
            Map<String, String> entryMap = (Map<String, String>) entry;
            JSONObject newObject = new JSONObject(entryMap);

            BasicDBObject fields = new BasicDBObject();
            fields.put("NewsArticleID", newObject.get("NewsArticleID"));
            DBCursor cursor = collection.find(fields);

            //The news article doesnt exist so add it in
            if (!cursor.hasNext()) {
                newObject.put("_id", UUID.randomUUID());
                Object articleToBeInserted = com.mongodb.util.JSON.parse(entry.toString());
                DBObject ObjectToBeInserted = (DBObject) articleToBeInserted;
                System.out.println("New entry inserted - Article ID: " + newObject.get("NewsArticleID"));
                collection.insert(ObjectToBeInserted);
            }
        }

    }

    /**
     * This runnable gets called every minute
     */
    @Override
    @Scheduled(fixedRate = 60)
    public void run() {
        try {
            JSONObject jsonObject = parseXMLToJSON(getXMLData());
            insertDataToDB(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}