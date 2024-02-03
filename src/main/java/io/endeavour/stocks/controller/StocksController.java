package io.endeavour.stocks.controller;

import io.endeavour.stocks.entity.stocks.*;
import io.endeavour.stocks.service.MarketAnalyticsService;
import io.endeavour.stocks.vo.*;
import io.endeavour.stocks.vo.webServiceVO.SubSectorVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Stocks API", description = "This api genrates cool trends related to US stock market")
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
@Operation(method = "StocksPriceHistoryVO",
        description = "This method returns stock price history of a single stock between specific dates and we also have a sort functionality")
@ApiResponse(responseCode = "200",description = "Returns a list for same stock how the stock performed every day between given fromDate and toDate")
@ApiResponse(responseCode = "400", description = "Returns 400 bad request if any of the input is not provided or if the to date is before fromDate")
public List<StocksPriceHistoryVO> getSingleStockPriceHistory(
        @PathVariable(name = "tickerSymbol")
        @Schema(name ="tickerSymbol",description = "Ticker Symbol to which stock you want to check its performance",example = "AAPL")
        String tickerSymbol,
        @RequestParam(name = "fromDate")
        @Schema(name = "fromDate",description = "Provide from date which is nothing but from what date you want to check the performance",example = "2023-01-01")
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
        @RequestParam(name = "toDate")
        @Schema(name = "fromDate",description = "Provide to date which is nothing but to what date you want to check the performance",example = "20023-01-10")
        @DateTimeFormat(pattern ="yyyy-MM-dd" ) LocalDate toDate,
        @RequestParam(name = "sortField")
        @Schema(name = "sortFieldOptional",description = "Provide the column name by which the table has to be sorted",example = "tickerSymbol")
        Optional<String> sortFieldOptional,
        @RequestParam(name = "sortDirection")
        @Schema(name = "sortDirectionOptional",description = "Provide asc or desc, asc: sorts table in ascending and desc: sorts the table in descending order ",example = "desc")
        Optional<String>  sortDirectionOptional){
        if (fromDate.isAfter(toDate)){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"fromDate cannot be greater than toDate");
        }
    System.out.println("From the request, the parameter TickerSymbol: "+tickerSymbol+", fromDate : "+fromDate+", toDate :"+toDate);
        LOGGER.debug("From the request, the parameter TickerSymbol : {}, fromDate : {}, toDate : {}",tickerSymbol,fromDate,toDate);
        return marketAnalyticsService.getSingleStockPriceHistory(tickerSymbol, fromDate, toDate,sortFieldOptional,sortDirectionOptional);
}

@PostMapping(value = "/getMultipleStockPriceHistory")
@Operation(method = "GetMultipleStockPriceHistory", description = "This API will return stock price history for the given" +
        " list of stocks for given time frame")
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
@Operation(method = "GetSomeStockFundamentals",description = "Gets the stock information for the given TickerSymbols")
@ApiResponse(responseCode = "200",description = "Returns List of Stock Fundamental information for the given ticker Symbol")
@ApiResponse(responseCode = "400",description = "Returns Http 400 Bad Request if the input ticker symbols list is not sent or is empty")
public List<StockFundamentalsWithNamesVO> getAllSpecificStocksUsingSqlQuery(
        @RequestBody
        @Schema(name = "TickersList",description = "List of thicker symbols for which the stock information needs to be retrieved",
        example = "[\"AAPL\",\"MSFT\",\"NVDA\"]") // here \ is called escape character
        Optional<List<String>> tickerSymbols){
    LOGGER.debug("got tickerSymbols from JSON into getAllSpecificStocksUsingSqlQuery controller : {}",tickerSymbols);
    if (tickerSymbols.isPresent()){
        List<String> tickerList = tickerSymbols.get();
        if (tickerList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TickersList is empty or not sent");
        }else {
            return marketAnalyticsService.getAllStockFundamentalsForGivenTickerSymbolsWithSQLQuery(tickerList);
        }
    }else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No TickersList empty or not sent");
    }


}

@GetMapping(value = "/getAllStockFundamentalsJPA")
public List<StockFundamentals> getAllStockFundamentalsJPA(){
        return  marketAnalyticsService.getAllStockFundamentalsWithJPA();
}

//"1) Write a GET API to get Sector and Subsector details from the database using JPA

    @GetMapping(value = "/getSectorsWithSubSectorsList")
    public List<SectorLookup> getSectorsWithSubSectorsList(){
        return marketAnalyticsService.getAllSectorsWithItsSubSectors();
    }

    @GetMapping(value = "/getAllSubSectors")
    public List<SubSectorLookup> getAllSubSectors(){
       return marketAnalyticsService.getAllSubSectors();
    }

    @GetMapping(value = "/getAllStocksLookupData")
    public List<StocksLookup> getAllStocksLookupData(){
        return marketAnalyticsService.getAllStocksLookup();
    }

    @GetMapping(value = "/getStockPriceHistory/{tickerSymbol}")
    public ResponseEntity<StockPriceHistory> getStockPriceHistory(@PathVariable(value = "tickerSymbol") String tickerSymbol,
                                                                  @RequestParam(value = "tradingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate tradingDate){
       return ResponseEntity.of(marketAnalyticsService.getStockPriceHistory(tickerSymbol, tradingDate));
    }

    @GetMapping(value = "/getTopStockBySector")
    public List<TopStockBySectorVO> getTopStockBySector(){
        return marketAnalyticsService.getTopStockBySector();
    }



    @GetMapping(value = "/getTopThreeStockBySector")
    public List<SectorNew> getTopThreeStockBySector(){
        return marketAnalyticsService.getTopThree();
    }

    @GetMapping(value = "/getTopNStocksNativeSQL")
public List<StockFundamentals> getTopNStocksNativeSQL(@RequestParam(value = "num") Integer num){
        return marketAnalyticsService.getTopNStocksNativeSQL(num);
}

    @GetMapping(value = "/getTopNStocksJpql")
    public List<StockFundamentals> getTopNStocksJpql(@RequestParam(value = "num") Integer num){
        return marketAnalyticsService.getTopNStocksJpql(num);
    }

    @GetMapping(value = "/getNotNullCurrentRatioStocks")
    public List<StockFundamentals> getNotNullCurrentRatioStocks(){
        return marketAnalyticsService.getNotNullCurrentRatioStocks();
    }

    @GetMapping(value = "/getTopNStocksCriteriaAPI")
    public List<StockFundamentals> getTopNStocksCriteriaAPI(@RequestParam(value = "num") Integer num){
        return marketAnalyticsService.getTopNStocksCriteriaAPI(num);
    }

    @GetMapping(value = "/getStockBetweenSpecificDates")
    public StockPriceHistoryResultVO getStockBetweenSpecificDates(
            @RequestParam(value = "tickerSymbol") String tickerSymbol,
           @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
           @RequestParam(value = "toDate")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate){
        return marketAnalyticsService.getStockBetweenSpecificDates(tickerSymbol,fromDate,toDate);

    }

    @GetMapping(value ="/getTopNPerformingStocks/{num}" )
    public List<StockFundamentalsWithNamesVO> getTopNPerformingStocks(
              @PathVariable(value = "num")  Integer num,
               @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
              @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
              @RequestParam(value = "marketCapLimit")  Long marketCapLimit){
       return marketAnalyticsService.getTopNPerformingStocks(num, fromDate, toDate, marketCapLimit);
    }

    @GetMapping(value ="/getTopFivePerformingStocksForEachSubSector/{num}" )
    public
    List<SubSectorVO> getTopFivePerformingStocksForEachSubSector(
            @PathVariable(value = "num")  Integer num,
            @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate
    ){
        return marketAnalyticsService.getTopFivePerformingStocksForEachSubSector(num, fromDate, toDate);
    }

@ExceptionHandler({IllegalArgumentException.class, SQLException.class, NullPointerException.class})
public ResponseEntity generateExceptionResponse(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
}

}
