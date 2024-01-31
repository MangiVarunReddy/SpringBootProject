package io.endeavour.stocks.service;

import io.endeavour.stocks.dao.StockFundamentalsWithNamesDao;
import io.endeavour.stocks.dao.StockPriceHistoryDao;
import io.endeavour.stocks.entity.stocks.*;
import io.endeavour.stocks.repository.stocks.*;
import io.endeavour.stocks.vo.StockFundamentalsWithNamesVO;
import io.endeavour.stocks.vo.StocksPriceHistoryVO;
import io.endeavour.stocks.vo.TopStockBySectorVO;
import io.endeavour.stocks.vo.TopThreeStocksVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MarketAnalyticsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MarketAnalyticsService.class);
    StockPriceHistoryDao stockPriceHistoryDao;

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

        List<SectorNew> finalResult = new ArrayList<>(sectorMap.values());

        // Print the final result
        System.out.println(finalResult);

        return finalResult;

    }
}


