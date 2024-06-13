# Prerequisites

### Business logic
* it is assumed that this project is made for a small local restaurant
therefore entity modeling involved creating a delivery address model
where only street, building and apartment number is required as usually
restaurant deliveries work within a certain city only
* customer phone number is of type string to allow input of prefixes (eg. +48)
* address apartment is not required - for houses building number is sufficient

### Implementation
* order search has been implemented this way due to H2 restrictions, in
real, production environment it would be replaced with full text indices
on real SQL database or completely changed to ElasticSearch for higher 
data volume applications

# Project description

# Running project

### Swagger API docs
```
http://localhost:8080/api-docs
http://localhost:8080/api-docs.yaml
http://localhost:8080/swagger-ui/index.html
```