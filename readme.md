# Ship Management
## _The Ship Information maintenance application_

Web application to manage the ship information.

The application comprises of both frontend and backend in the respective paths /app and /api

✨Technology ✨
- FRONT: Angular
- BACK: Spring boot
- Database: H2

## Features
- Create a Ship information
- Update a ship information
- Read a ship information
- Delete a ship information

## Addtional Features
- Search
- Sort
- Filters

## Access URL
The Ship Management application will run in the below mentioned port

> Frontend: http://localhost:4200
> Backend: http://localhost:8080

> Swagger: http://localhost:8080/swagger-ui/#/
> Actuator: http://localhost:8080/actuator

## Installation

The application requires [Node.js](https://nodejs.org/) to run.

## Backend
Need to run below commands in the backend to pull dependencies
```sh
api> mvn clean install
```

Command to run unit test
```sh
api> mvn test
```

Command to run unit test
```sh
api> mvn integration-test
```


## Front End
To start application
```
app> npm i
app> npm start
```

To run test

```sh
app> ng test 
```
To run headless chrome
```
app> npm run test-headless
```