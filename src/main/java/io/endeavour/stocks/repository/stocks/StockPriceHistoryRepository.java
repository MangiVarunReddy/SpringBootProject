package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StockPRiceHistoryKey;
import io.endeavour.stocks.entity.stocks.StockPriceHistory;
import io.endeavour.stocks.vo.StockPriceHistoryWithStockFundamentals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StockPriceHistoryRepository extends JpaRepository<StockPriceHistory, StockPRiceHistoryKey> {

    @Query(name = "StocksPriceHistory.StockPriceHistoryWithStockFundamentals", nativeQuery = true)
    public List<StockPriceHistoryWithStockFundamentals> getAllStockHistoryWithFundamentals(
            @Param("tickerSymbol") String tickerSymbol,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}
