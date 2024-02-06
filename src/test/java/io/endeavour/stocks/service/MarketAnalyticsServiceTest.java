package io.endeavour.stocks.service;

import io.endeavour.stocks.StockException;
import io.endeavour.stocks.UnitTestBase;
import io.endeavour.stocks.dao.StockFundamentalsWithNamesDao;
import io.endeavour.stocks.remote.vo.CRWSOutputVO;
import io.endeavour.stocks.vo.StockFundamentalsWithNamesVO;
import io.endeavour.stocks.remote.StockCalculationsClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


class MarketAnalyticsServiceTest extends UnitTestBase {
    @Autowired
    MarketAnalyticsService marketAnalyticsService;

 @MockBean
    StockFundamentalsWithNamesDao stockFundamentalsWithNamesDao;

 @MockBean
 StockCalculationsClient stockCalculationsClient;

 @Test
 public void topNPerformingStocks_HappyPath(){
     List<StockFundamentalsWithNamesVO> dummyStockOutputList = List.of(
             createStockFundamentals("AAPL", new BigDecimal("10000"), new BigDecimal("2.3")),
             createStockFundamentals("MSFT", new BigDecimal("11000"), new BigDecimal("1.98")),
             createStockFundamentals("GOOGL", new BigDecimal("8000"), new BigDecimal("1.99")),
             createStockFundamentals("AMD", new BigDecimal("1000"), new BigDecimal("1.11")),
             createStockFundamentals("V", new BigDecimal("455"), new BigDecimal("3.33"))
     );

     //This code intercepts the method call of the mocked bean, and returns our dummy list as the output list
     //The actual database call is bypasses in the business code and replaced with the mock list which returns List of dummyStockOutputList
     Mockito.when(stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO())
             .thenReturn(dummyStockOutputList);

     List<CRWSOutputVO> dummyCRWSOutputList = List.of(
             createCRWSOutput("AAPL", new BigDecimal("1.90")),
             createCRWSOutput("V", new BigDecimal("2.12")),
             createCRWSOutput("MSFT", new BigDecimal("3.15")),
             createCRWSOutput("GOOGL", new BigDecimal("1.63")),
             createCRWSOutput("AMD", new BigDecimal("1.10"))
     );

     Mockito.when(stockCalculationsClient.getCumulativeReturns(Mockito.any(),Mockito.any(),Mockito.any()))
             .thenReturn(dummyCRWSOutputList);

     List<StockFundamentalsWithNamesVO> topNPerformingStocksList = marketAnalyticsService.getTopNPerformingStocks(3,
             LocalDate.now().minusMonths(6),
             LocalDate.now(), 0L);

     //This assert checks if top 3 stocks were returned or not
     assertEquals(3,topNPerformingStocksList.size());

     //I want to check if the stocks are sorted by cumulativeReturn Desc
     assertEquals("V",topNPerformingStocksList.get(1).getTickerSymbol());

     //I want to check if GOOGL or AMD are excluded or not
     StockFundamentalsWithNamesVO dummyAMDStock=new StockFundamentalsWithNamesVO();
     dummyAMDStock.setTickerSymbol("AMD");
     assertFalse(topNPerformingStocksList.contains(dummyAMDStock));
 }

 @Test
 public void topNPerformingStocks_UnMatchedData(){
     List<StockFundamentalsWithNamesVO> dummyStockOutputList = List.of(
             createStockFundamentals("AAPL", new BigDecimal("10000"), new BigDecimal("2.3")),
             createStockFundamentals("MSFT", new BigDecimal("11000"), new BigDecimal("1.98")),
             createStockFundamentals("GOOGL", new BigDecimal("8000"), new BigDecimal("1.99")),
             createStockFundamentals("AMD", new BigDecimal("1000"), new BigDecimal("1.11")),
             createStockFundamentals("V", new BigDecimal("455"), new BigDecimal("3.33"))
     );

     //This code intercepts the method call of the mocked bean, and returns our dummy list as the output list
     //The actual database call is bypasses in the business code and replaced with the mock list which returns List of dummyStockOutputList
     Mockito.when(stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO())
             .thenReturn(dummyStockOutputList);

     List<CRWSOutputVO> dummyCRWSOutputList = List.of(
             createCRWSOutput("AAPL", new BigDecimal("1.90")),
             createCRWSOutput("V", new BigDecimal("2.12")),
             createCRWSOutput("MSFT", new BigDecimal("3.15"))
     );

     Mockito.when(stockCalculationsClient.getCumulativeReturns(Mockito.any(),Mockito.any(),Mockito.any()))
             .thenReturn(dummyCRWSOutputList);

     List<StockFundamentalsWithNamesVO> topNPerformingStocksList = marketAnalyticsService.getTopNPerformingStocks(4, LocalDate.now().minusMonths(6),
             LocalDate.now(), 0L);

     assertEquals(3,topNPerformingStocksList.size());
 }

 @Test
 public void topNPerformingStocks_WSDown(){
             List<StockFundamentalsWithNamesVO> dummyStockOutputList = List.of(
             createStockFundamentals("AAPL", new BigDecimal("10000"), new BigDecimal("2.3")),
             createStockFundamentals("MSFT", new BigDecimal("11000"), new BigDecimal("1.98")),
             createStockFundamentals("GOOGL", new BigDecimal("8000"), new BigDecimal("1.99")),
             createStockFundamentals("AMD", new BigDecimal("1000"), new BigDecimal("1.11")),
             createStockFundamentals("V", new BigDecimal("455"), new BigDecimal("3.33"))
     );

     //This code intercepts the method call of the mocked bean, and returns our dummy list as the output list
     //The actual database call is bypasses in the business code and replaced with the mock list which returns List of dummyStockOutputList
     Mockito.when(stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO())
             .thenReturn(dummyStockOutputList);

     //This call simulates the webservice being down as it returns Empty List
     Mockito.when(stockCalculationsClient.getCumulativeReturns(Mockito.any(),Mockito.any(),Mockito.any()))
             .thenReturn(Collections.emptyList());

     assertThrows(StockException.class,()-> marketAnalyticsService.getTopNPerformingStocks(5,LocalDate.now().minusMonths(6),
             LocalDate.now(),0L));

     Exception exception = assertThrows(StockException.class, () -> marketAnalyticsService.getTopNPerformingStocks(5, LocalDate.now().minusMonths(1),
             LocalDate.now(), 0L));
     String expectedMessage="Webservice is Down";
     String actualMessage=exception.getMessage();

     assertTrue(actualMessage.toUpperCase().contains(expectedMessage.toUpperCase()));
 }

 private StockFundamentalsWithNamesVO createStockFundamentals(String tickerSymbol,
                                                              BigDecimal marketCap,
                                                              BigDecimal currentRatio){
     StockFundamentalsWithNamesVO stockFundamentalsWithNamesVO=new StockFundamentalsWithNamesVO();
     stockFundamentalsWithNamesVO.setTickerSymbol(tickerSymbol);
     stockFundamentalsWithNamesVO.setMarketCap(marketCap);
     stockFundamentalsWithNamesVO.setCurrentRatio(currentRatio);

   return   stockFundamentalsWithNamesVO;
 }

 private CRWSOutputVO createCRWSOutput(String tickerSymbol,
                                       BigDecimal cumulativeReturn){
     CRWSOutputVO crwsOutputVO =new CRWSOutputVO();
     crwsOutputVO.setTickerSymbol(tickerSymbol);
     crwsOutputVO.setCumulativeReturn(cumulativeReturn);

     return crwsOutputVO;
 }


}