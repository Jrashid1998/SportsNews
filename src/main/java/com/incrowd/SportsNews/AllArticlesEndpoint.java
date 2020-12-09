package com.incrowd.SportsNews;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AllArticlesEndpoint {

    @Autowired
    DatabaseAccess DBA;

    @RequestMapping(value = "/allArticles", method = RequestMethod.GET)
    @ResponseBody
    public String retrieveAllArticles() {
        //A simple retrieve all and parse the JSON into a string
        StringBuilder JSONString = new StringBuilder();
        DBCursor cursor = DBA.collection.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            JSONString.append(obj);
        }
        return JSONString.toString();
    }

}