package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StockPRiceHistoryKey;
import io.endeavour.stocks.entity.stocks.StockPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPriceHistoryRepository extends JpaRepository<StockPriceHistory, StockPRiceHistoryKey> {
}
