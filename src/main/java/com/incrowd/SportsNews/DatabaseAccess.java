package com.incrowd.SportsNews;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

@Component
public class DatabaseAccess {

    DBCollection collection;
    DB database;

    DatabaseAccess() throws UnknownHostException {
        //Standard port and host so don't need to config
        MongoClient mongoClient = new MongoClient();
        database = mongoClient.getDB("SportsNews");
        collection = database.getCollection("SportsNews");
    }

    public DB getDatabase() {
        return database;
    }

    public DBCollection getCollection() {
        return collection;
    }
}
