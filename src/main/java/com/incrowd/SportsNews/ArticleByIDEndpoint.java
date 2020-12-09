package com.incrowd.SportsNews;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ArticleByIDEndpoint {

    @Autowired
    DatabaseAccess DBA;

    @RequestMapping(value = "/articleById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String retrieveAllArticles(@PathVariable int id) {

        //Takes the id in the URL and uses it to query the db to return the article
        BasicDBObject fields = new BasicDBObject();
        fields.put("NewsArticleID", id);
        DBObject newsArticle = DBA.getCollection().findOne(fields);

        //If no article exists, tell the user
        if (newsArticle != null) {
            return newsArticle.toString();
        } else
            return "No object exists with the specified ID";
    }
}
