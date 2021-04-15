# vlingo-xoom-developer-challenge

## Table of contents
[1. Overview](#1-Overview)\
[2. Architecture Decision Log](#1-Architecture-Decision-Log)\
[2. Vlingo XOOM Designer - Initial Design](#2-Vlingo-XOOM-Designer---Initial-Design)

## 1. Overview

### Domain - Accounting:

- Working with a domain expert (accountant), to build an integrated system focusing on:
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
  ![Employee Context](docs/images/01.png)
- Purchase Context:
  ![Employee Context](docs/images/01.png)

# Run solution with infrastructure

- First build the solution using:
  `
  ./gradlew build
  `
- Then execute :
  `
  docker-compose -f docker-compose.yml -f docker-compose.override.yml up -d
  `
