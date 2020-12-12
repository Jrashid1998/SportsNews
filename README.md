# SportsNews

To use this application, please launch and it'll automatically:

1) Take the XML from the specified URL
2) Convert it into a JSON
3) Break it down into the individual JSON Objects
4) For each object, check if it already exists in the database - If it does, ignore. If it doesn't, add a UUID and submit to Mongo

To query the database via endpoints:

-All Articles:
append "/allArticles" to the URL
example: http://localhost:8080/allArticles

-Article By ID
append "/articleById/" followed by id you'd like to retrieve
example: http://localhost:8080/articleById/534259
