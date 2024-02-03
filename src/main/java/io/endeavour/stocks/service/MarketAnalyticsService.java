package io.endeavour.stocks.service;

import io.endeavour.stocks.dao.StockFundamentalsWithNamesDao;
import io.endeavour.stocks.dao.StockPriceHistoryDao;
import io.endeavour.stocks.entity.stocks.*;
import io.endeavour.stocks.remote.vo.CRWSInputVO;
import io.endeavour.stocks.remote.vo.CRWSOutputVO;
import io.endeavour.stocks.repository.stocks.*;
import io.endeavour.stocks.vo.*;
import io.endeavour.stocks.remote.*;
import io.endeavour.stocks.vo.webServiceVO.StocksListVO;
import io.endeavour.stocks.vo.webServiceVO.SubSectorVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MarketAnalyticsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MarketAnalyticsService.class);
    StockPriceHistoryDao stockPriceHistoryDao;

    @Autowired
    StockCalculationsClient stockCalculationsClient;

    @Autowired
    StockFundamentalsWithNamesDao stockFundamentalsWithNamesDao;

    @Autowired
    StockFundamentalsRepository stockFundamentalsRepository;

    @Autowired
    SectorLookupRepository sectorLookupRepository;

    @Autowired
    SubSectorRepository subSectorRepository;

    @Autowired
    StocksLookupRepository stocksLookupRepository;

    @Autowired
    StockPriceHistoryRepository stockPriceHistoryRepository;



    @Autowired
    public MarketAnalyticsService(StockPriceHistoryDao stockPriceHistoryDao) {
        this.stockPriceHistoryDao = stockPriceHistoryDao;
    }

    @Autowired
    public TopThreeStocksRepository topThreeStocksRepository;

    public List<StocksPriceHistoryVO> getSingleStockPriceHistory(String tickerSymbol, LocalDate fromDate, LocalDate toDate,
                                                                 Optional<String> sortFieldOptional, Optional<String> sortDirectionOptional) {
        List<StocksPriceHistoryVO> stockPriceHistoryList = stockPriceHistoryDao.getSingleStockPriceHistory(tickerSymbol, fromDate, toDate);
        String fieldToSortBy = sortFieldOptional.orElse("TradingDate");
        String directionToSortBy = sortDirectionOptional.orElse("asc");

        Comparator sortComparator = switch (fieldToSortBy.toUpperCase()) {
            case ("OPENPRICE") -> Comparator.comparing(StocksPriceHistoryVO::getOpenPrice);
            case ("CLOSEPRICE") -> Comparator.comparing(StocksPriceHistoryVO::getClosePrice);
            case ("VOLUME") -> Comparator.comparing(StocksPriceHistoryVO::getVolume);
            case ("TRADINGDATE") -> Comparator.comparing(StocksPriceHistoryVO::getTradingDate);
            default -> {
                LOGGER.error("Value entered for sortField is incorrect. Value entered is {} : ", fieldToSortBy);
                throw new IllegalArgumentException("Value Entered for Sort field is Incorrect. Value entered is: " + fieldToSortBy);

            }

        };
        if (directionToSortBy.equalsIgnoreCase("desc")) {
            sortComparator = sortComparator.reversed();
        }
        stockPriceHistoryList.sort(sortComparator);

        return stockPriceHistoryList;
    }

    public List<StocksPriceHistoryVO> getMultipleStockPriceHistory(List<String> tickerList, LocalDate fromDate, LocalDate toDate) {
        return stockPriceHistoryDao.getMultipleStockPriceHistory(tickerList, fromDate, toDate);
    }

    public List<StockFundamentalsWithNamesVO> getAllStockFundamentals() {
        LOGGER.debug("Entered the method getAllStockFundamentals() of the class {}", getClass());
        List<StockFundamentalsWithNamesVO> stockFundamentalsList = stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO();
        return stockFundamentalsList;
    }

    public List<StockFundamentalsWithNamesVO> getAllStockFundamentalsForSpecificTickerSymbols(List<String> tickerSymbolList) {
        List<StockFundamentalsWithNamesVO> allStockFundamentalsWithNamesVOList = stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO();
        List<StockFundamentalsWithNamesVO> listOfRequiredStocks = allStockFundamentalsWithNamesVOList.stream()
                .filter(tickerList -> tickerSymbolList.contains(tickerList.getTickerSymbol()))
                .collect(Collectors.toList());
        return listOfRequiredStocks;
    }

    public List<StockFundamentalsWithNamesVO> getAllStockFundamentalsForGivenTickerSymbolsWithSQLQuery(List<String> tickerSymbolList) {

        return stockFundamentalsWithNamesDao.gellStockFundamentalDetailsWithNames(tickerSymbolList);
    }

    public List<StockFundamentals> getAllStockFundamentalsWithJPA() {
        return stockFundamentalsRepository.findAll();
    }

    public List<SectorLookup> getAllSectorsWithItsSubSectors() {
        return sectorLookupRepository.findAll();
    }

    public List<SubSectorLookup> getAllSubSectors() {
        return subSectorRepository.findAll();
    }

    //To get all stocksLookup data
    public List<StocksLookup> getAllStocksLookup() {
        return stocksLookupRepository.findAll();
    }

    public Optional<StockPriceHistory> getStockPriceHistory(String tickerSymbol, LocalDate tradingDate) {
        StockPRiceHistoryKey primaryKeyObj = new StockPRiceHistoryKey();
        primaryKeyObj.setTickerSymbol(tickerSymbol);
        primaryKeyObj.setTradingDate(tradingDate);
        return stockPriceHistoryRepository.findById(primaryKeyObj);
    }

    public List<TopStockBySectorVO> getTopStockBySector() {
        return stockFundamentalsRepository.getTopStockBySector();
    }


    public List<SectorNew> getTopThree(){
        List<TopThreeStocksVO> topThreeStocksForEachSectorList = topThreeStocksRepository.getTopThreeStocksForEachSector();
        List<SectorLookup> allSectorsList = sectorLookupRepository.findAll();

        Map<Integer, SectorNew> sectorMap = new HashMap<>();

        for (SectorLookup sectorLookup : allSectorsList) {
            List<TopThreeStocksVO> topThreeStocksForSector = topThreeStocksForEachSectorList.stream()
                    .filter(stock -> sectorLookup.getSectorID().equals(stock.getSectorID()))
                    .collect(Collectors.toList());


            SectorNew sector = new SectorNew(sectorLookup.getSectorID(), sectorLookup.getSectorName(), topThreeStocksForSector);
            sectorMap.put(sectorLookup.getSectorID(), sector);
        }

        List<SectorNew> finalResult = (List<SectorNew>) sectorMap.values();

        System.out.println(sectorMap);
        return finalResult;

    }

    public List<StockFundamentals> getTopNStocksNativeSQL(Integer num){
        return stockFundamentalsRepository.getTopNStocksNativeSQL(num);
    }

    public List<StockFundamentals> getTopNStocksJpql(Integer num){
        return stockFundamentalsWithNamesDao.getTopNStocksJPQL(num);
    }

    public List<StockFundamentals> getNotNullCurrentRatioStocks(){
        return stockFundamentalsRepository.getNotNullCurrentRatioStocks();
    }

    public List<StockFundamentals> getTopNStocksCriteriaAPI(Integer num){
        return stockFundamentalsWithNamesDao.getTopNStocksCriteriaAPI(num);
    }
//"GET /stock-fundamentals/{tickerSymbol}/{fromDate}/{toDate}
//
//    {
//        ""marketCap"": 100,
//            ""currentRatio: 1,
//            ""tradingHistory"": [
//        {
//            ""tickerSymbol"": ""AAPL"",
//                ""tradingDate"": ""2023-01-03"",
//                ""closePrice"": 125.07,
//                ""volume"": 112117500
//        },
//        {
//            ""tickerSymbol"": ""AAPL"",
//                ""tradingDate"": ""2023-01-05"",
//                ""closePrice"": 124.07,
//                ""volume"": 112117500
//        }
//    ]
//    }"
    public StockPriceHistoryResultVO getStockBetweenSpecificDates(String tickerSymbol, LocalDate fromDate, LocalDate toDate){
        //retrived all the raw data
        List<StockPriceHistoryWithStockFundamentals> listOfNewStockPriceHistory=  stockPriceHistoryRepository.getAllStockHistoryWithFundamentals(tickerSymbol,fromDate,toDate);

        //This is the VO how my data should look like
        StockPriceHistoryResultVO stockPriceHistoryResultVO=new StockPriceHistoryResultVO();

        //I have grouped the raw data by marketcap and currentratio
        //I got a map which has an inner map
        Map<BigDecimal, Map<BigDecimal, List<StockPriceHistoryWithStockFundamentals>>> mapOfStockHistoryGroupedByMarketAndCurrentRatio = listOfNewStockPriceHistory.stream()
                .collect(Collectors.groupingBy(StockPriceHistoryWithStockFundamentals::getMarketCap,
                        Collectors.groupingBy(StockPriceHistoryWithStockFundamentals::getCurrentRatio)));

    //I have to get the map key which is marketCap
        BigDecimal firstKey = mapOfStockHistoryGroupedByMarketAndCurrentRatio.keySet().iterator().next();

        //Retrived the inner map using the key
        Map<BigDecimal, List<StockPriceHistoryWithStockFundamentals>> innerMap = mapOfStockHistoryGroupedByMarketAndCurrentRatio.get(firstKey);

        //retrived the inner map key
        BigDecimal secondKey = innerMap.keySet().iterator().next();

        //I have set the marketCap and Current Ratio keys into my result object values
        stockPriceHistoryResultVO.setMarketCap(firstKey);
        stockPriceHistoryResultVO.setCurrentRatio(secondKey);



        //This is to store all the map values into a list
        List<StockPriceHistoryDataChildVO> stockList = new ArrayList<>();


        for (StockPriceHistoryWithStockFundamentals stockPriceHistoryWithStockFundamental : innerMap.get(secondKey)) {
            //created an object to store the list data
            StockPriceHistoryDataChildVO stockPriceHistoryDataChildVO=new StockPriceHistoryDataChildVO();
            stockPriceHistoryDataChildVO.setTickerName(stockPriceHistoryWithStockFundamental.getTickerName());
            stockPriceHistoryDataChildVO.setTickerSymbol(stockPriceHistoryWithStockFundamental.getTickerSymbol());
            stockPriceHistoryDataChildVO.setTradingDate(stockPriceHistoryWithStockFundamental.getTradingDate());
            stockPriceHistoryDataChildVO.setClosePrice(stockPriceHistoryWithStockFundamental.getClosePrice());
            stockPriceHistoryDataChildVO.setOpenPrice(stockPriceHistoryWithStockFundamental.getOpenPrice());
            stockPriceHistoryDataChildVO.setVolume(stockPriceHistoryWithStockFundamental.getVolume());

            //set the object into a list, eventually all the list are addeed into my final list
            stockList.add(stockPriceHistoryDataChildVO);

        }
        //setting the stockPriceHistoryDataChildVO into the stockPriceHistoryResultVO
        stockPriceHistoryResultVO.setStockPriceHistoryDataChildVO(stockList);


//returning stockPriceHistoryResultVO
        return stockPriceHistoryResultVO;
    }

    public List<StockFundamentalsWithNamesVO> getTopNPerformingStocks(Integer num, LocalDate fromDate, LocalDate toDate, Long marketCapLimit){
        List<StockFundamentalsWithNamesVO> allStockList = getStockFundamentalsWithNamesVOS(fromDate, toDate);

        List<StockFundamentalsWithNamesVO> finalOutputList = allStockList.stream()
                .filter(stockFundamentals -> stockFundamentals.getCumulativeReturn() != null)
                .filter(stockFundamentals -> stockFundamentals.getMarketCap().compareTo(new BigDecimal(marketCapLimit)) > 0)
                .sorted(Comparator.comparing(StockFundamentalsWithNamesVO::getCumulativeReturn).reversed())
                .limit(num)
                .collect(Collectors.toList());

        return finalOutputList;
    }

    public List<SubSectorVO> getTopFivePerformingStocksForEachSubSector(Integer num,
                                                                        LocalDate fromDate,
                                                                        LocalDate toDate){
        List<StockFundamentalsWithNamesVO> allStockList = getStockFundamentalsWithNamesVOS(fromDate, toDate);

        Map<String, List<StockFundamentalsWithNamesVO>> stockListBySubSectorNameMap = allStockList.stream()
                .collect(Collectors.groupingBy(StockFundamentalsWithNamesVO::getSubSectorName));

        Map<String, List<StockFundamentalsWithNamesVO>> topStockListBySectorNameMap=new HashMap<>();

        stockListBySubSectorNameMap.forEach((sectorName,stocksList)->{
            List<StockFundamentalsWithNamesVO> topNPerformingList = stocksList.stream()
                    .filter(stockFundamentalsWithNamesVO -> stockFundamentalsWithNamesVO.getCumulativeReturn() != null)
                    .sorted(Comparator.comparing(StockFundamentalsWithNamesVO::getCumulativeReturn).reversed())
                    .limit(num)
                    .collect(Collectors.toList());
            topStockListBySectorNameMap.put(sectorName,topNPerformingList);
        });

        List<SubSectorVO> finalOutputList=new ArrayList<>();

        topStockListBySectorNameMap.forEach((subSectorName,topStockList)->{
            SubSectorVO subSectorVO=new SubSectorVO();
            subSectorVO.setSubSectorName(subSectorName);
            List<StocksListVO> topNStocks=new ArrayList<>();
            topStockList.forEach(stockFundamentalsWithNamesVO -> {
                subSectorVO.setSunSectorID(stockFundamentalsWithNamesVO.getSubSectorID());
                StocksListVO stocksListVO= new StocksListVO();
                stocksListVO.setTickerSymbol(stockFundamentalsWithNamesVO.getTickerSymbol());
                stocksListVO.setTickerName(stockFundamentalsWithNamesVO.getTickerName());
                stocksListVO.setMarketCap(stockFundamentalsWithNamesVO.getMarketCap());
                stocksListVO.setCumulativeReturn(stockFundamentalsWithNamesVO.getCumulativeReturn());
                topNStocks.add(stocksListVO);

            });
            subSectorVO.setTopStocks(topNStocks);
            finalOutputList.add(subSectorVO);

        });
        finalOutputList.sort(Comparator.comparing(SubSectorVO::getSubSectorName));

        return finalOutputList;
    }

    private List<StockFundamentalsWithNamesVO> getStockFundamentalsWithNamesVOS(LocalDate fromDate, LocalDate toDate) {
        //        List<StockFundamentals> allStockList = stockFundamentalsRepository.findAll();
        List<StockFundamentalsWithNamesVO> allStockList = stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO();
        List<String> allTickerList = allStockList.stream()
                .map(stockFundamentals -> stockFundamentals.getTickerSymbol())
                .collect(Collectors.toList());
//        LOGGER.info("Number of stocks that are being sent as inputs to the cumulative return web service is {}", allStockList.size());
        CRWSInputVO crwsInputVO=new CRWSInputVO();
        crwsInputVO.setTickers(allTickerList);
        List<CRWSOutputVO> cumulativeReturnsList = stockCalculationsClient.getCumulativeReturns(fromDate, toDate, crwsInputVO);
//        LOGGER.info("Number of stocks returned from the Cumulative Return web service is {}",cumulativeReturnsList.size());

        Map<String, BigDecimal> cumulativeReturnByTickerSymbolMap = cumulativeReturnsList.stream()
                .collect(Collectors.toMap(
                        CRWSOutputVO::getTickerSymbol,
                        CRWSOutputVO::getCumulativeReturn
                ));

        allStockList.forEach(stockFundamentals -> {
            stockFundamentals.setCumulativeReturn(cumulativeReturnByTickerSymbolMap.get(stockFundamentals.getTickerSymbol()));
        });
        return allStockList;
    }
}


