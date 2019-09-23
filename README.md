# Getting Started - Index Calculation Service

### Requirement
Company ABC, as one of the leading providers of financial indexes, consumes and uses real-time trading prices
of tens of thousands of financial instruments from more than 100 exchanges over the world.
In order to ensure integrity of our index calculation and proper input data, our operations team needs a
restful API to monitor the incoming prices. The main use case for that API is to provide real-time price
statistic from the last 60 seconds.
There will be three APIs:
-   one of them is called every time we receive a tick. It is also the sole input of this rest API.
-   The second one returns the statistics based on the ticks of all instruments of the last 60 seconds
-   The third one returns the statistics based on the ticks of one instrument of the last 60 seconds.

## Assumptions
-   If the tick timestamp is 60 seconds older, the instrument will not be added and 
return the status code 204
-   In one post tick request, request body will contain only single tick object.
We do not support addition of list of ticks in single request

## Solution
-   I have provided solution of the API's using in-memory H2 DB
-   I have also provided the solution for an in-memory solution without DB
-   This is the endpoint with aggregated statistics for all ticks across all instruments, this endpoint has to
  execute in constant time and memory (O(1)). I have achieved this using ConcurrentHashMao

## What would you improve if you had more time
-   A complete test suit to do e2e testing
-   100% test coverage
-   Code review and refactoring. I will be doing the above steps when I get some more time


## Build and Execute
```
$ gradle install

$ gradle build
```
or
```
$ gradle bootRun
```

## Build and Execute using Intellij
### Pre-requisite:

JDK 1.8 To check, run java -version:
```
 $ java -version
 java version "1.8.0_121"
``` 
Any IDE, preferably Intellij

### Installation and Setup:

-   Go to the github url - https://github.com/vijujoseph/index-calculation-service.git
-   select 'Clone or download', copy the URL
-   Open intelliJ - File -> New -> Project From Version Control -> git
-   Enter URL - which was cloned in step 2 and click clone button
-   IntelliJ will show and option to Import gradle projects, choose the option and refresh all gradle dependencies
-   Click build
-   Run bootRun


## Guidance - Api Specification:
```
http://localhost:8080/ticks    
Request type - POST
Request body 
============
{
"instrument": "Oracle",
"price": 37.5,
"timestamp": 1569271781072
}

http://localhost:8080/statistics
Request type - GET

http://localhost:8080/statistics/oracle
Request type - GET
``` 
Use chrome or any rest clients for REST request and response

**Few Examples are below:**  
1. http://localhost:8080/statistics - GET
    ```
     {
     "avg": 572.5,
     "max": 1000,
     "min": 25,
     "sum": 2862.5,
     "count": 5
     }
    ```
2. http://localhost:8080/statistics/oracle - GET
    ```
      {
      "avg": 490.625,
      "max": 1000,
      "min": 25,
      "sum": 1962.5,
      "count": 4
      }
    ```
3. http://localhost:8080/ticks - POST
   ```
   Request Body
    {
    "instrument": "IBM.N",
    "price": 900,
    "timestamp": 1569275719407
    }
    
    Response Status
    201
    or 204
    ```