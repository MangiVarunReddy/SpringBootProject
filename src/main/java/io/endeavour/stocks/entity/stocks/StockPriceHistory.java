package io.endeavour.stocks.entity.stocks;

import io.endeavour.stocks.vo.StockPriceHistoryWithStockFundamentals;
import io.endeavour.stocks.vo.TopThreeStocksVO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "stocks_price_history", schema = "endeavour")
@IdClass(value = StockPRiceHistoryKey.class) //Add this annotation when the table has a composite primary key
@NamedNativeQuery(name = "StocksPriceHistory.StockPriceHistoryWithStockFundamentals",query = """
        select
        	sl.ticker_name ,
        	sf.market_cap ,
        	sf.current_ratio ,
        	sph.ticker_symbol ,
        	sph.trading_date ,
        	sph.close_price ,
        	sph.open_price,
        	sph.volume
        from
        	endeavour.stock_fundamentals sf ,
        	endeavour.stocks_price_history sph ,
        	endeavour.stocks_lookup sl
        where
        	sf.current_ratio is not null
        	and sf.ticker_symbol =sph.ticker_symbol
        	and sl.ticker_symbol =sf.ticker_symbol
        	and sph.trading_date between :fromDate AND :toDate
        	and sf.ticker_symbol =:tickerSymbol
        order by sf.market_cap desc 
        """,resultSetMapping = "StocksPriceHistory.StockPriceHistoryWithStockFundamentalsMapping")

@SqlResultSetMapping(name = "StocksPriceHistory.StockPriceHistoryWithStockFundamentalsMapping",
        classes = @ConstructorResult(targetClass = StockPriceHistoryWithStockFundamentals.class,
                columns = {
                        @ColumnResult(name = "ticker_symbol",type = String.class),
                        @ColumnResult(name = "ticker_name",type = String.class),
                        @ColumnResult(name = "market_cap",type = BigDecimal.class),
                        @ColumnResult(name = "current_ratio",type = BigDecimal.class),
                        @ColumnResult(name = "trading_date",type = LocalDate.class),
                        @ColumnResult(name = "open_price",type = BigDecimal.class),
                        @ColumnResult(name = "close_price",type = BigDecimal.class),
                        @ColumnResult(name = "volume",type = Long.class)
                }
        )
)
public class StockPriceHistory {

    @Column(name = "ticker_symbol")
    @Id
    private String tickerSymbol;
    @Column(name = "trading_date")
    @Id
    private LocalDate tradingDate;
    @Column(name = "open_price")
    private BigDecimal openPrice;
    @Column(name = "close_price")
    private BigDecimal closePrice;
    @Column(name = "volume")
    private Long volume;



    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public LocalDate getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(LocalDate tradingDate) {
        this.tradingDate = tradingDate;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "StockPriceHistory{" +
                "tickerSymbol='" + tickerSymbol + '\'' +
                ", tradingDate=" + tradingDate +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", volume=" + volume +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockPriceHistory that = (StockPriceHistory) o;
        return Objects.equals(tickerSymbol, that.tickerSymbol) && Objects.equals(tradingDate, that.tradingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickerSymbol, tradingDate);
    }
}
