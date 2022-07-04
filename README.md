# README #

This repository contains a Spring-boot microservice setup.

*  For database access, plain JDBC is used, but feel free  to change it.
*  You need to create a `credentials.properties` file in the resources/ fodler  with the credentials to access the database:

```
db.username=...
db.password=...
```

* The repo defines a single service `/hotel/<id>`  that returns the name of the requested hotel. 
* Logging uses SLf4J
