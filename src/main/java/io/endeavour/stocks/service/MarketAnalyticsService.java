package io.endeavour.stocks.service;

import io.endeavour.stocks.dao.StockPriceHistoryDao;
import io.endeavour.stocks.vo.StocksPriceHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MarketAnalyticsService {

    StockPriceHistoryDao stockPriceHistoryDao;

    @Autowired
    public MarketAnalyticsService(StockPriceHistoryDao stockPriceHistoryDao) {
        this.stockPriceHistoryDao = stockPriceHistoryDao;
    }

    public List<StocksPriceHistoryVO> getSingleStockPriceHistory(String tickerSymbol, LocalDate fromDate, LocalDate toDate){
        return stockPriceHistoryDao.getSingleStockPriceHistory(tickerSymbol,fromDate,toDate);
    }

    public List<StocksPriceHistoryVO> getMultipleStockPriceHistory(List<String> tickerList, LocalDate fromDate, LocalDate toDate){
        return stockPriceHistoryDao.getMultipleStockPriceHistory(tickerList,fromDate,toDate);
    }
}
