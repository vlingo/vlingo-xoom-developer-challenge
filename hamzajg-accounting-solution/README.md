# vlingo-xoom-developer-challenge
## DOMA Accounting Solution

## Table of contents
[1. Overview](#1-Overview)\
[2. Architecture Decision Log](#2-Architecture-Decision-Log)\
[3. Vlingo XOOM Designer - Initial Design](#3-Vlingo-XOOM-Designer---Initial-Design)\
[4. Run solution with infrastructure](4-Run-solution-with-infrastructure)\
[5. Testing the system APIs using curl](5-Testing-the-system-APIs-using-curl)

## 1. Overview

### Domain - Accounting:

Working with a domain expert (accountant), to build an integrated system focusing on:
- Customers
- Assets
- Rental
- Bank
- Sale
- Purchase
- Employee expenses

## 2. Architecture Decision Log

All Architectural Decisions (AD) are documented in the [Architecture Decision Log (ADL)](docs/architecture-decision-log).

## 3. Vlingo XOOM Designer - Initial Design:

- Customer Context:
  ![Customer Context](docs/images/01.png)
- Assets Context:
  ![Assets Context](docs/images/02.png)
  ![Assets Context](docs/images/03.png)
- Rental Context:
  ![Rental Context](docs/images/04.png)
- Bank Context:
  ![Customer Context](docs/images/01.png)
- Employee Context:
  ![Employee Context](docs/images/01.png)
- Sale Context:
  ![Sale Context](docs/images/01.png)
- Purchase Context:
  ![Purchase Context](docs/images/01.png)

### 4. Run solution with infrastructure

- First build the solution using:
  `
  ./gradlew build
  `
- Then execute :
  `
  docker-compose -f docker-compose.yml -f docker-compose.override.yml up -d
  `
- List Services:
  `
  docker-compose -f docker-compose.yml -f docker-compose.override.yml ps
  `
```console
                   Name                                 Command               State                                             Ports
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
hamzajg-accounting-solution_api-gateway_1    /docker-entrypoint.sh ngin ...   Up      80/tcp, 0.0.0.0:8080->8080/tcp
hamzajg-accounting-solution_assets-api_1     /bin/sh -c java -Dcom.sun. ...   Up      18080/tcp
hamzajg-accounting-solution_bank-api_1       /bin/sh -c java -Dcom.sun. ...   Up      18080/tcp
hamzajg-accounting-solution_customer-api_1   /bin/sh -c java -Dcom.sun. ...   Up      18080/tcp
hamzajg-accounting-solution_employee-api_1   /bin/sh -c java -Dcom.sun. ...   Up      18080/tcp
hamzajg-accounting-solution_purchase-api_1   /bin/sh -c java -Dcom.sun. ...   Up      18080/tcp
hamzajg-accounting-solution_rental-api_1     /bin/sh -c java -Dcom.sun. ...   Up      18080/tcp
hamzajg-accounting-solution_sale-api_1       /bin/sh -c java -Dcom.sun. ...   Up      18080/tcp
rabbitmq                                     docker-entrypoint.sh rabbi ...   Up      15671/tcp, 0.0.0.0:15672->15672/tcp, 25672/tcp, 4369/tcp, 5671/tcp, 0.0.0.0:5672->5672/tcp
```
### 5. Testing the system APIs using curl:
