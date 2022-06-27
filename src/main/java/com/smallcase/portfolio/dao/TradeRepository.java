package com.smallcase.portfolio.dao;

import com.smallcase.portfolio.models.Trade;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author utsav
 * This class is used to run queries on trade table in Postgres
 */
public interface TradeRepository extends JpaRepository<Trade, Integer> {
    List<Trade> findByTicker(String ticker, Sort by);
    Trade findTopByTickerOrderByTimestampDesc(String ticker);
}
