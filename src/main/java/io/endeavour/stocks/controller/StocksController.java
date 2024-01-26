package io.endeavour.stocks.controller;

import io.endeavour.stocks.service.MarketAnalyticsService;
import io.endeavour.stocks.vo.StockFundamentalsWithNamesVO;
import io.endeavour.stocks.vo.StockPriceHistoryRequestVO;
import io.endeavour.stocks.vo.StocksPriceHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/stocks")
public class StocksController {

    private static final Logger LOGGER= LoggerFactory.getLogger(StocksController.class);

    @Autowired
    MarketAnalyticsService marketAnalyticsService;
    @GetMapping("/getSamplePriceHistory")
public StocksPriceHistoryVO getSamplePriceHistory(){
    StocksPriceHistoryVO stocksPriceHistoryVO= new StocksPriceHistoryVO();
    stocksPriceHistoryVO.setTickerSymbol("V");
    stocksPriceHistoryVO.setTradingDate(LocalDate.now());
    stocksPriceHistoryVO.setOpenPrice(new BigDecimal("154.34"));
    stocksPriceHistoryVO.setClosePrice(new BigDecimal("155.93"));
    stocksPriceHistoryVO.setVolume(890634L);

    return stocksPriceHistoryVO;
}

@GetMapping(value ="getSingleStockPriceHistory/{tickerSymbol}" )
public List<StocksPriceHistoryVO> getSingleStockPriceHistory(@PathVariable(name = "tickerSymbol") String tickerSymbol,
                                                             @RequestParam(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                             @RequestParam(name = "toDate") @DateTimeFormat(pattern ="yyyy-MM-dd" ) LocalDate toDate,
                                                             @RequestParam(name = "sortField") Optional<String>  sortFieldOptional,
                                                             @RequestParam(name = "sortDirection") Optional<String>  sortDirectionOptional){
        if (fromDate.isAfter(toDate)){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"fromDate cannot be greater than toDate");
        }
    System.out.println("From the request, the parameter TickerSymbol: "+tickerSymbol+", fromDate : "+fromDate+", toDate :"+toDate);
        LOGGER.debug("From the request, the parameter TickerSymbol : {}, fromDate : {}, toDate : {}",tickerSymbol,fromDate,toDate);
        return marketAnalyticsService.getSingleStockPriceHistory(tickerSymbol, fromDate, toDate,sortFieldOptional,sortDirectionOptional);
}

@PostMapping(value = "/getMultipleStockPriceHistory")
public List<StocksPriceHistoryVO> getMultipleStockPriceHistory(@RequestBody StockPriceHistoryRequestVO stockPriceHistoryRequestVO){
        LOGGER.info("Values received from the Http Request are : tickerSymbol {}, fromDate {}, toDate {}",
                stockPriceHistoryRequestVO.getTickersList(),stockPriceHistoryRequestVO.getFromDate(),
                stockPriceHistoryRequestVO.getToDate());
        return marketAnalyticsService.getMultipleStockPriceHistory(stockPriceHistoryRequestVO.getTickersList(),
                stockPriceHistoryRequestVO.getFromDate(),
                stockPriceHistoryRequestVO.getToDate());

}

@GetMapping(value = "/getAllStockFundamentalsJDBC")
public List<StockFundamentalsWithNamesVO> getAllStockFundamentalsJDBC(){
        LOGGER.debug("In the getAllStockFundamentsaJDBC() method of the class{} ", getClass());
        return marketAnalyticsService.getAllStockFundamentals();
}

@PostMapping(value = "/getAllSpecificStocks")
public List<StockFundamentalsWithNamesVO> getAllSpecificStocks(@RequestBody List<String> tickerSymbols){
        LOGGER.debug("got tickerSymbols from JSON into getAllSpecificStocks controller : {}",tickerSymbols);
        return marketAnalyticsService.getAllStockFundamentalsForSpecificTickerSymbols(tickerSymbols);
}

@PostMapping(value = "/getAllSpecificStocksUsingSqlQuery")
public List<StockFundamentalsWithNamesVO> getAllSpecificStocksUsingSqlQuery(@RequestBody List<String> tickerSymbols){
    LOGGER.debug("got tickerSymbols from JSON into getAllSpecificStocksUsingSqlQuery controller : {}",tickerSymbols);
        return marketAnalyticsService.getAllStockFundamentalsForGivenTickerSymbolsWithSQLQuery(tickerSymbols);
}
@ExceptionHandler({IllegalArgumentException.class, SQLException.class, NullPointerException.class})
public ResponseEntity generateExceptionResponse(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
}

}
