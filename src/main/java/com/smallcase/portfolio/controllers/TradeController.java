package com.smallcase.portfolio.controllers;

import com.smallcase.portfolio.models.Stock;
import com.smallcase.portfolio.models.StockTradeResponse;
import com.smallcase.portfolio.models.Trade;
import com.smallcase.portfolio.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trades")
public class TradeController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping
    public Trade executeTrade(@RequestBody Trade trade) {
        return portfolioService.executeTrade(trade);
    }

}

