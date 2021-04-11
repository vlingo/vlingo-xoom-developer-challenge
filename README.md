# vlingo-xoom-developer-challenge

On your marks. Set. XOOM! <-- Will keep this here... :)

# Overview

## Vlingo XOOM Designer:

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
  ![Customer Context](docs/images/01.png)

# Run solution with infrastructure

- First build the solution using:
  `
  ./gradlew build
  `
- Then execute :
  `
  docker-compose -f docker-compose.yml -f docker-compose.override.yml up -d
  `
