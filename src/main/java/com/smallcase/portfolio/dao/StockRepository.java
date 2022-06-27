package com.smallcase.portfolio.dao;

import com.smallcase.portfolio.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author utsav
 * This class is used to run queries on stock table in Postgres
 */
@Transactional
public interface StockRepository extends JpaRepository<Stock, Integer> {
    Stock findByTicker(String ticker);
    void deleteByTicker(String ticker);
}
