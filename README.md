# Package Finder
> A logistics company has contracted you to build a service that will be used to track packages. Possible package statuses are PICKED_UP, IN_TRANSIT, WAREHOUSE, DELIVERED. It’s possible for a package to be IN_TRANSIT, and WAREHOUSE multiple times but it’s not possible to be PICKED_UP and DELIVERED more than once.

Package finder is a basic tracking service.
It works by
1. Registering the package on the system
2. Updating the package status
3. Fetching/tracking package status
4. Also provides an API to view package status history

Here's a link to the design document: https://drive.google.com/file/d/1mq4fmMiVtfYUWRSilIV_M6Vm3pwsPbbh/view?usp=sharing

## Installing / Getting started

###Prerequisites
- Install Docker
- Java 11

### Initial Configuration

The ports for the application and the database can be found on the docker-compose.yml file in the root directory

### Running

Here are the steps for running Package finder:

```shell
git clone https://github.com/NneomaBlessyn/package-finder.git
cd package-finder (optional if you are in the project directory already)
./gradlew clean build
docker compose up
```

Yippie yayyy!!! You can now access the service.
Use the swagger link, http://localhost:8419/swagger-ui.html#, to get started

### Automated Tests
To run the tests on the project:
```shell
./gradlew test
```

### Testing
Steps:
1. Create a user using the create user API
2. Login with user using the user login API
3. Copy bearer token at the bottom of payload
4. Use token for authentication (ps: add "Bearer " before token. Eg. "Bearer hfhjdfkdjhdhjlsks" )

Things to test
- Register a package
- Fetch package by order ID
- Fetch all packages on system (Paginated)
- Update package status
- Track package
- Fetch all status history for package (Paginated)

## Features
* Tracking of package using order ID
* See package status history
* Update package status
* Register package on the system

## Links
- Design Doc: https://drive.google.com/file/d/1mq4fmMiVtfYUWRSilIV_M6Vm3pwsPbbh/view?usp=sharing
- Swagger UI: http://localhost:8419/swagger-ui.html#

## Improvements
- Using a DB migration tool like flyway to manage database schemas
- Adding analytic APIs
