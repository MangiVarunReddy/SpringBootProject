package io.endeavour.stocks.service;

import io.endeavour.stocks.controller.StocksController;
import io.endeavour.stocks.dao.StockFundamentalsWithNamesDao;
import io.endeavour.stocks.dao.StockPriceHistoryDao;
import io.endeavour.stocks.vo.StockFundamentalsWithNamesVO;
import io.endeavour.stocks.vo.StocksPriceHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MarketAnalyticsService {

    private final static Logger LOGGER= LoggerFactory.getLogger(MarketAnalyticsService.class);
    StockPriceHistoryDao stockPriceHistoryDao;

    @Autowired
    StockFundamentalsWithNamesDao stockFundamentalsWithNamesDao;

    @Autowired
    public MarketAnalyticsService(StockPriceHistoryDao stockPriceHistoryDao) {
        this.stockPriceHistoryDao = stockPriceHistoryDao;
    }

    public List<StocksPriceHistoryVO> getSingleStockPriceHistory(String tickerSymbol, LocalDate fromDate, LocalDate toDate,
                                                                 Optional<String> sortFieldOptional, Optional<String> sortDirectionOptional){
        List<StocksPriceHistoryVO> stockPriceHistoryList = stockPriceHistoryDao.getSingleStockPriceHistory(tickerSymbol, fromDate, toDate);
        String fieldToSortBy = sortFieldOptional.orElse("TradingDate");
        String directionToSortBy = sortDirectionOptional.orElse("asc");

       Comparator sortComparator= switch (fieldToSortBy.toUpperCase()){
            case ("OPENPRICE")-> Comparator.comparing(StocksPriceHistoryVO::getOpenPrice);
            case ("CLOSEPRICE")->Comparator.comparing(StocksPriceHistoryVO::getClosePrice);
            case ("VOLUME")->Comparator.comparing(StocksPriceHistoryVO::getVolume);
            case ("TRADINGDATE")->Comparator.comparing(StocksPriceHistoryVO::getTradingDate);
            default -> {
                LOGGER.error("Value entered for sortField is incorrect. Value entered is {} : ",fieldToSortBy);
                throw new IllegalArgumentException("Value Entered for Sort field is Incorrect. Value entered is: " + fieldToSortBy);

            }

        };
       if (directionToSortBy.equalsIgnoreCase("desc")){
           sortComparator=sortComparator.reversed();
       }
        stockPriceHistoryList.sort(sortComparator);

        return stockPriceHistoryList;
    }

    public List<StocksPriceHistoryVO> getMultipleStockPriceHistory(List<String> tickerList, LocalDate fromDate, LocalDate toDate){
        return stockPriceHistoryDao.getMultipleStockPriceHistory(tickerList,fromDate,toDate);
    }

    public List<StockFundamentalsWithNamesVO> getAllStockFundamentals(){
        LOGGER.debug("Entered the method getAllStockFundamentals() of the class {}",getClass());
        List<StockFundamentalsWithNamesVO> stockFundamentalsList = stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO();
        return  stockFundamentalsList;
    }
}
