# Carshop Service
A carshop wants to manage its inventory of cars. A car consists of
- brand
- color 
- vehicle identification number
- listed price
- date of purchase
- if it is reserved by a customer

Brand, color, vehicle identification number are modelled as `String` to allow 
for flexibility. Listed price is modelled as `BigDecimal` which can be safely used 
in calculations. Date of purchase is modelled as `OffsetDateTime` which allows to save
time zone specific date offsets. If a car is reserved by a customer is modelled as a `boolean` since 
the requirements do not state to save anything about the customer.

## DATABASE
The cars are saved in a PostgreSQL database. The database can be started by
```
docker-compose up postgres
```
The port `5432` is mapped to localhost.

## REST API
Start the application locally or dockerized and open
```
localhost:8080/swagger-ui.html
```
in a browser to see the documentation.

## DOCKER
The application is dockerized, an image can be created by
```
./gradlew clean build docker
```
Afterwards the application can be started in docker by
```
docker-compose -f docker-compose-manual.yml up car
```
which exposes a port at `localhost:8080`.

NOTE: The file docker-compose.yml is used in integration tests in order to provide 
a dockerized PostgreSQL database. 

## TEST
In order to start integration tests from within the IDE, a postgres database
must be provided with port mapped to `localhost:5432`.
