package com.smallcase.portfolio.controllers;

import com.smallcase.portfolio.models.ReturnResponse;
import com.smallcase.portfolio.models.Stock;
import com.smallcase.portfolio.models.StockTradeResponse;
import com.smallcase.portfolio.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping
    public List<Stock> listAll() {
        return portfolioService.fetchPortfolio();
    }

    @GetMapping("/{ticker}")
    public Stock getOne(@PathVariable String ticker) {
        return portfolioService.fetchStockInfo(ticker);
    }

    @GetMapping("/details")
    public List<StockTradeResponse> fetchAllDetails() {
        return portfolioService.fetchAllTrades();
    }

    @GetMapping("/returns")
    public ReturnResponse fetchReturns() {
        return portfolioService.fetchReturns();
    }
}