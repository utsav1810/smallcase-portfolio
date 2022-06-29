# REST API example application

This file documents different portfolio tracking API which allow adding/deleting/updating trades and can do basic return calculations etc.
For simplicity, we have assumed there is only one user who can have single portfolio.

## Tech Stack

Language: `Java` 

Framework: `Spring` 

Database: `Postgres` 

Deployed on : `Heroku` 

Note - Since application is deployed on free-tier resources, it will take about 30 seconds for the server to start on your first request.

# REST API

All REST APIs are exposed from as a swagger web page - http://smallcase-portfolio-mgt1.herokuapp.com/swagger-ui/index.html
You can directly use the swagger page to execute APIs or use the curl commands given below.

## Get summary of all stocks in the portfolio

### Request

`GET : http://smallcase-portfolio-mgt1.herokuapp.com/portfolio`

    curl -X 'GET' 'http://smallcase-portfolio-mgt1.herokuapp.com/portfolio' -H 'accept: application/json'

### Response headers

    connection: keep-alive 
    content-type: application/json 
    date: Wed,29 Jun 2022 09:14:54 GMT 
    server: Cowboy 
    transfer-encoding: chunked 
    via: 1.1 vegur

### Response body

    [{
        "ticker": "TCS",                //  ticker symbol of the stock
        "quantity": 10,                 //  quantity of the stock owned
        "averageBuyPrice": 60           //  average buying price
    }]

## Get summary of stock with given ticker symbol in the portfolio

### Request

`GET : http://smallcase-portfolio-mgt1.herokuapp.com/portfolio/{ticker}`

    curl -X 'GET' 'http://smallcase-portfolio-mgt1.herokuapp.com/portfolio/TCS' -H 'accept: application/json'

### Request param

    ticker :  ticker symbol of the stock - TCS/WIPRO/GODREJIND

### Response headers

    connection: keep-alive 
    date: Wed,29 Jun 2022 09:16:01 GMT 
    server: Cowboy 
    transfer-encoding: chunked 
    via: 1.1 vegur

### Response body

    {
        "ticker": "TCS",                //  ticker symbol of the stock
        "quantity": 10,                 //  quantity of the stock owned
        "averageBuyPrice": 60           //  average buying price
    }

### Validation

    1. Ticker must be a valid value - [TCS, WIPRO, GODREJIND]

## Get cumulative returns at any point of time of a particular portfolio

### Request

`GET : http://smallcase-portfolio-mgt1.herokuapp.com/portfolio/returns`

    curl -X 'GET' 'http://smallcase-portfolio-mgt1.herokuapp.com/portfolio/returns' -H 'accept: application/json'

### Response headers

    connection: keep-alive 
    content-type: application/json 
    date: Wed,29 Jun 2022 09:17:59 GMT 
    server: Cowboy 
    transfer-encoding: chunked 
    via: 1.1 vegur

### Response body

    {
        "returnAmount": 0,                  //  total cumulative returns in terms of amount
        "returnPercentage": 0               //  total cumulative returns in terms of percentage
    }

## Execute a trade

### Request

`POST : http://smallcase-portfolio-mgt1.herokuapp.com/trade`

    curl -X 'POST' 'http://smallcase-portfolio-mgt1.herokuapp.com/trade' -H 'accept: application/json' -H 'Content-Type: application/json' -d '{"ticker": "TCS","price": 60,"type": "BUY","quantity": 10}'

### Request body

    {
        "ticker": "TCS",            //  ticker symbol to trade - TCS/WIPRO/GODREJIND
        "quantity": 100,            //  quantity of the stock to perform trade on
        "price": 30,                //  price of the stock to perform trade on 
        "type": "SELL"              //  type of the trade - BUY/SELL
    }

### Response headers

    connection: keep-alive 
    content-type: application/json 
    date: Wed,29 Jun 2022 09:26:19 GMT 
    server: Cowboy 
    transfer-encoding: chunked 
    via: 1.1 vegur

### Response body

    {
        "ticker": "TCS",                                    //  ticker symbol of the stock to trade
        "errorMessage": null,                               //  error message during trade if any
        "status": "SUCCESS",                                //  status of the trade - SUCCESS/FAILED
        "price": 60,                                        //  price at which stock was bought/sold
        "type": "BUY",                                      //  type of the trade - BUY/SELL
        "timestamp": "2022-06-29T09:26:19.272+00:00",       //  timestamp at which trade was executed
        "quantity": 10                                      //  quantity of the stock bought/sold
    }

### Validation

    1. ticker must be a valid value - [TCS, WIPRO, GODREJIND]
    2. quantity should be greater than 0
    3. price should be greater than 0
    4. type should be BUY/SELL
    5. If type is SELL, there should be atleast given quantity of stocks in the portfolio

## Update last executed trade for ticker symbol

### Request

`PUT : http://smallcase-portfolio-mgt1.herokuapp.com/trade`

    curl -X 'PUT' 'http://smallcase-portfolio-mgt1.herokuapp.com/trade' -H 'accept: application/json' -H 'Content-Type: application/json' -d '{"ticker": "TCS","price": 60,"type": "SELL","quantity": 2}'

### Request body

    {
        "ticker": "TCS",            //  ticker symbol to trade - TCS/WIPRO/GODREJIND
        "quantity": 100,            //  quantity of the stock to perform trade on
        "price": 30,                //  price of the stock to perform trade on 
        "type": "SELL"              //  type of the trade - BUY/SELL
    }

### Response headers

    connection: keep-alive 
    content-type: application/json 
    date: Wed,29 Jun 2022 09:30:04 GMT 
    server: Cowboy 
    transfer-encoding: chunked 
    via: 1.1 vegur

### Response body

    {
        "ticker": "TCS",                                    //  ticker symbol of the stock to trade
        "errorMessage": null,                               //  error message during trade if any
        "status": "SUCCESS",                                //  status of the trade - SUCCESS/FAILED
        "price": 60,                                        //  price at which stock was bought/sold
        "type": "SELL",                                     //  type of the trade - BUY/SELL
        "timestamp": "2022-06-29T09:26:19.272+00:00",       //  timestamp at which trade was executed
        "quantity": 2                                       //  quantity of the stock bought/sold
    }

### Validation

    1. ticker must be a valid value - [TCS, WIPRO, GODREJIND]
    2. quantity should be greater than 0
    3. price should be greater than 0
    4. type should be BUY/SELL
    5. If type is SELL, there should be atleast given quantity of stocks in the portfolio
    6. Previous trade should exist for given ticker symbol

## Delete last executed trade for ticker symbol

### Request

`DELETE : http://smallcase-portfolio-mgt1.herokuapp.com/trade?ticker=TCS`

    curl -X 'DELETE' 'http://smallcase-portfolio-mgt1.herokuapp.com/trade?ticker=TCS' -H 'accept: application/json'

### Request param

    ticker :  ticker symbol of the stock - TCS/WIPRO/GODREJIND

### Response headers

    connection: keep-alive 
    content-type: application/json 
    date: Wed,29 Jun 2022 09:32:23 GMT 
    server: Cowboy 
    transfer-encoding: chunked 
    via: 1.1 vegur

### Response body

    {
        "ticker": "TCS",                                    //  ticker symbol of the stock to trade
        "errorMessage": null,                               //  error message during trade if any
        "status": "SUCCESS",                                //  status of the trade - SUCCESS/FAILED
        "price": 60,                                        //  price at which stock was bought/sold
        "type": "SELL",                                     //  type of the trade - BUY/SELL
        "timestamp": "2022-06-29T09:26:19.272+00:00",       //  timestamp at which trade was executed
        "quantity": 2                                       //  quantity of the stock bought/sold
    }

### Validation

    1. ticker must be a valid value - [TCS, WIPRO, GODREJIND]
    2. Previous trade should exist for given ticker symbol

## Fetch all trades with securities

### Request

`GET : http://smallcase-portfolio-mgt1.herokuapp.com/trade/list`

    curl -X 'GET' 'http://smallcase-portfolio-mgt1.herokuapp.com/trade/list' -H 'accept: application/json'

### Response headers

    connection: keep-alive 
    content-type: application/json 
    date: Wed,29 Jun 2022 09:32:23 GMT 
    server: Cowboy 
    transfer-encoding: chunked 
    via: 1.1 vegur

### Response body

    {
        "stockTradeResponse": [
            {
            "ticker": "TCS",                    //  ticker symbol of stock
            "trades": [                         //  list of all trades - latest first
                {
                "ticker": "TCS",
                "errorMessage": null,
                "status": "SUCCESS",
                "price": 60,
                "type": "BUY",
                "timestamp": "2022-06-29T09:26:19.272+00:00",
                "quantity": 10
                }
            ],
            "quantity": 10,                     //  total quantity in portfolio
            "averageBuyPrice": 60               //  average buy price in portfolio
            }
        ]
    }


## Things to note

    1. Quantity should be a positive inetger
    2. Price should be a positive double value
    3. Ticker should be either of TCS/WIPRO/GODREJIND
    4. Trade type should be BUY/SELL