# 1. Prerequisites

### 1.1. Business logic
* it is assumed that this project is made for a small local restaurant
therefore entity modeling involved creating a delivery address model
where only street, building and apartment number is required as usually
restaurant deliveries work within a certain city only
* customer phone number is of type string to allow input of prefixes (eg. +48)
* address apartment is not required - for houses building number is sufficient

### 1.2. Implementation
* order search has been implemented this way due to H2 restrictions, in
real, production environment it would be replaced with full text indices
on real SQL database or completely changed to ElasticSearch for higher 
data volume applications

# 2. Project description



# 3. Running project

### 3.1. From IntelliJ IDE
when running from intellij, pass `-Dapikey=any-value` VM option

### 3.2. From Docker
make sure to check `-Dapikey=any-value` VM option in Dockerfile

### 3.3. Running tests
add -Duser.country=US -Duser.language=en

# 4. Additional 

### 4.1. Swagger API docs
```
http://localhost:8080/api-docs
http://localhost:8080/api-docs.yaml
http://localhost:8080/swagger-ui/index.html
```

### 4.2. Postman testing collection
```
/root/pilotes_rest.postman.collection.json
```