package com.smallcase.portfolio.controllers;

import com.smallcase.portfolio.models.StockTradeResponseList;
import com.smallcase.portfolio.models.Trade;
import com.smallcase.portfolio.services.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PortfolioService portfolioService;

    @Operation(summary = "Execute a trade")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trade executed successfully",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = Trade.class))})})
    @PostMapping
    public Trade executeTrade(@RequestBody Trade trade) {
        return portfolioService.executeTrade(trade);
    }

    @Operation(summary = "Delete previous trade for given ticker symbol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trade deleted successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Trade.class))})})
    @DeleteMapping
    public Trade deleteTrade(@RequestParam String ticker) {
        return portfolioService.deleteTrade(ticker);
    }

    @Operation(summary = "Update previous trade for given ticker symbol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trade udpated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Trade.class))})})
    @PutMapping
    public Trade updateTrade(@RequestBody Trade trade) {
        return portfolioService.updateTrade(trade);
    }

    @Operation(summary = "Fetch all the securities and trades corresponding to it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trade udpated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockTradeResponseList.class))})})
    @GetMapping("/list")
    public StockTradeResponseList fetchAllDetails() {
        return portfolioService.fetchAllTrades();
    }
}

