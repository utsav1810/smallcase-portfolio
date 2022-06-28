package com.smallcase.portfolio.controllers;

import com.smallcase.portfolio.models.ReturnResponse;
import com.smallcase.portfolio.models.Stock;
import com.smallcase.portfolio.models.StockTradeResponse;
import com.smallcase.portfolio.services.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get summary of all stocks in the portfolio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fetched details successfully",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class)) })})
    @GetMapping
    public List<Stock> listAll() {
        return portfolioService.fetchPortfolio();
    }

    @Operation(summary = "Get summary of stock with given ticker symbol in the portfolio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fetched details successfully",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))})})
    @GetMapping("/{ticker}")
    public Stock getOne(@PathVariable String ticker) {
        return portfolioService.fetchStockInfo(ticker);
    }

    @Operation(summary = "Get returns from current state of the portfolio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched details successfully",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnResponse.class))})})
    @GetMapping("/returns")
    public ReturnResponse fetchReturns() {
        return portfolioService.fetchReturns();
    }
}