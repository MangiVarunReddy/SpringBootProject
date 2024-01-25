package io.endeavour.stocks.controller;

import io.endeavour.stocks.service.MarketAnalyticsService;
import io.endeavour.stocks.vo.StockPriceHistoryRequestVO;
import io.endeavour.stocks.vo.StocksPriceHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/stocks")
public class StocksController {

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
                                                             @RequestParam(name = "toDate") @DateTimeFormat(pattern ="yyyy-MM-dd" ) LocalDate toDate){
        return marketAnalyticsService.getSingleStockPriceHistory(tickerSymbol, fromDate, toDate);
}

@PostMapping(value = "/getMultipleStockPriceHistory")
public List<StocksPriceHistoryVO> getMultipleStockPriceHistory(@RequestBody StockPriceHistoryRequestVO stockPriceHistoryRequestVO){
        return marketAnalyticsService.getMultipleStockPriceHistory(stockPriceHistoryRequestVO.getTickersList(),
                stockPriceHistoryRequestVO.getFromDate(),
                stockPriceHistoryRequestVO.getToDate());

}
}
