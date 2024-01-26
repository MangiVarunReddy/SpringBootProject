package io.endeavour.stocks.dao;

import io.endeavour.stocks.vo.StockFundamentalsWithNamesVO;
import io.endeavour.stocks.vo.StocksPriceHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockFundamentalsWithNamesDao {
    private static final Logger LOGGER= LoggerFactory.getLogger(StockFundamentalsWithNamesDao.class);

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<StockFundamentalsWithNamesVO> getAllStockFundamentalsWithNamesVO(){
        LOGGER.debug("Now in the getAllStockFundamentalsWithNamesVO() method of the class {}",getClass());
        String sqlQuery= """
                 select
                     sf.ticker_symbol,
                     sl.ticker_name,
                     sf.sector_id,
                     sl1.sector_name,
                     sf.subsector_id,
                     sl2.subsector_name,
                     sf.market_cap,
                     sf.current_ratio
                 from
                     endeavour.stock_fundamentals sf,
                     endeavour.stocks_lookup sl,
                     endeavour.sector_lookup sl1,
                     endeavour.subsector_lookup sl2
                 where
                     sf.ticker_symbol = sl.ticker_symbol
                     and sf.sector_id = sl1.sector_id
                     and sf.subsector_id = sl2.subsector_id
                 """;
        List<StockFundamentalsWithNamesVO> stockFundamentalsList=   namedParameterJdbcTemplate.query(sqlQuery,
                (rs,rowNum)->{
                    StockFundamentalsWithNamesVO stockFundamentalsWithNamesVO= new StockFundamentalsWithNamesVO();
                    stockFundamentalsWithNamesVO.setTickerSymbol(rs.getString("ticker_symbol"));
                    stockFundamentalsWithNamesVO.setTickerName(rs.getString("ticker_name"));
                    stockFundamentalsWithNamesVO.setSectorID(rs.getInt("sector_id"));
                    stockFundamentalsWithNamesVO.setSectorName(rs.getString("sector_name"));
                    stockFundamentalsWithNamesVO.setSubSectorID(rs.getInt("subsector_id"));
                    stockFundamentalsWithNamesVO.setSubSectorName(rs.getString("subsector_name"));
                    stockFundamentalsWithNamesVO.setMarketCap(rs.getBigDecimal("market_cap"));
                    stockFundamentalsWithNamesVO.setCurrentRatio(rs.getBigDecimal("current_ratio"));
                    return stockFundamentalsWithNamesVO;
                });

LOGGER.debug("The stockFundamentalsList is {}: ",stockFundamentalsList);

        return stockFundamentalsList;
    }

    public List<StockFundamentalsWithNamesVO> gellStockFundamentalDetailsWithNames(List<String> tickerSymbolList){
        String sqlQuery= """
                select
                     sf.ticker_symbol,
                     sl.ticker_name,
                     sf.sector_id,
                     sl1.sector_name,
                     sf.subsector_id,
                     sl2.subsector_name,
                     sf.market_cap,
                     sf.current_ratio
                 from
                     endeavour.stock_fundamentals sf,
                     endeavour.stocks_lookup sl,
                     endeavour.sector_lookup sl1,
                     endeavour.subsector_lookup sl2
                 where
                     sf.ticker_symbol = sl.ticker_symbol
                     and sf.sector_id = sl1.sector_id
                     and sf.subsector_id = sl2.subsector_id
                     and sf.ticker_symbol IN(:tickerList)
                """;
        MapSqlParameterSource mapSqlParameterSource=new MapSqlParameterSource();
        mapSqlParameterSource.addValue("tickerList", tickerSymbolList);

        List<StockFundamentalsWithNamesVO> stockFundamentslsList = namedParameterJdbcTemplate.query(sqlQuery,mapSqlParameterSource,(rs,rowNum)->{
            StockFundamentalsWithNamesVO stockFundamentalsWithNamesVO= new StockFundamentalsWithNamesVO();
            stockFundamentalsWithNamesVO.setTickerSymbol(rs.getString("ticker_symbol"));
            stockFundamentalsWithNamesVO.setTickerName(rs.getString("ticker_name"));
            stockFundamentalsWithNamesVO.setSectorID(rs.getInt("sector_id"));
            stockFundamentalsWithNamesVO.setSectorName(rs.getString("sector_name"));
            stockFundamentalsWithNamesVO.setSubSectorID(rs.getInt("subsector_id"));
            stockFundamentalsWithNamesVO.setSubSectorName(rs.getString("subsector_name"));
            stockFundamentalsWithNamesVO.setMarketCap(rs.getBigDecimal("market_cap"));
            stockFundamentalsWithNamesVO.setCurrentRatio(rs.getBigDecimal("current_ratio"));
            return stockFundamentalsWithNamesVO;
        });
        return  stockFundamentslsList;
    }
}
