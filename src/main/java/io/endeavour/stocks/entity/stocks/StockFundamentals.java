package io.endeavour.stocks.entity.stocks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.endeavour.stocks.vo.TopStockBySectorVO;
import io.endeavour.stocks.vo.TopThreeStocksVO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "stock_fundamentals",schema = "endeavour")
@NamedNativeQuery(name = "StockFundamentals.TopStockBySector", query = """
        with MKTCP_RANK_BY_SECTOR as(
        	select
        	sf.sector_id ,
        	sl2.sector_name ,
        	sf.ticker_symbol ,
        	sl.ticker_name ,
        	sf.market_cap ,
        	rank () over(partition by sf.sector_id order by sf.market_cap desc) as MKTCP_RANK
        from
        	endeavour.stock_fundamentals sf ,
        	endeavour.stocks_lookup sl ,
        	endeavour.sector_lookup sl2
        where
        	sf.market_cap is  not null
        	and sf.ticker_symbol =sl.ticker_symbol
        	and sl2.sector_id =sf.sector_id
        )
        select
        	mrs.sector_id,
        	mrs.sector_name,
        	mrs.ticker_symbol,
        	mrs.market_cap,
        	mrs.ticker_name
        from
        	MKTCP_RANK_BY_SECTOR mrs
        where
        	MKTCP_RANK=1      
        """, resultSetMapping = "StockFundamentals.TopStockBySectorMapping")
@SqlResultSetMapping(name = "StockFundamentals.TopStockBySectorMapping",
        classes = @ConstructorResult(targetClass = TopStockBySectorVO.class,
        columns = {
                @ColumnResult(name = "sector_id",type = Integer.class),
                @ColumnResult(name = "sector_name",type =String.class),
                @ColumnResult(name = "ticker_symbol",type =String.class),
                @ColumnResult(name = "ticker_name",type =String.class),
                @ColumnResult(name = "market_cap",type =BigDecimal.class)
        }
        )
)
@NamedNativeQuery(name = "StocksFundamentalsNew.TopThreeStocks",query = """
        with MKTCP_RANK_BY_SECTOR as(
        	select
        	sf.sector_id,
        	sl2.sector_name ,
        	sf.ticker_symbol ,
        	sl.ticker_name ,
        	sf.market_cap ,
        	rank () over(partition by sf.sector_id order by sf.market_cap desc) as MKTCP_RANK
        from
        	endeavour.stock_fundamentals sf ,
        	endeavour.stocks_lookup sl ,
        	endeavour.sector_lookup sl2
        where
        	sf.market_cap is  not null
        	and sf.ticker_symbol =sl.ticker_symbol
        	and sl2.sector_id =sf.sector_id
        )
        select
            mrs.sector_id,
            mrs.sector_name,
        	mrs.ticker_symbol,
        	mrs.ticker_name,
        	mrs.market_cap
        from
        	MKTCP_RANK_BY_SECTOR mrs
        where
        	MKTCP_RANK<4 
        """,resultSetMapping = "StocksFundamentalsNew.TopThreeStocksMapping")

@SqlResultSetMapping(name = "StocksFundamentalsNew.TopThreeStocksMapping",
        classes = @ConstructorResult(targetClass = TopThreeStocksVO.class,
                columns = {
                        @ColumnResult(name = "sector_id",type = Integer.class),
                        @ColumnResult(name = "sector_name",type = String.class),
                        @ColumnResult(name = "ticker_symbol",type = String.class),
                        @ColumnResult(name = "ticker_name",type = String.class),
                        @ColumnResult(name = "market_cap",type = BigDecimal.class)
                }
        )
)
public class StockFundamentals {
    @Column(name = "ticker_symbol")
    @Id
    private String tickerSymbol;

    @OneToOne
    @JoinColumn(name = "sector_id", referencedColumnName = "sector_id")
    private SectorLookup sectorLookup;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "subsector_id", referencedColumnName = "subsector_id")
    private SubSectorLookup subSectorLookup;

    @OneToOne
    @JoinColumn(name = "ticker_symbol",referencedColumnName = "ticker_symbol")
    private StocksLookup stocksLookup;

    @Column(name = "market_cap")
    private BigDecimal marketCap;
    @Column(name = "current_ratio")
    private BigDecimal currentRatio;

    public String getTickerName(){
        return stocksLookup.getTickerName();
    }

    @JsonIgnore
    public StocksLookup getStocksLookup() {
        return stocksLookup;
    }

    public void setStocksLookup(StocksLookup stocksLookup) {
        this.stocksLookup = stocksLookup;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public int getSectorID(){
        return sectorLookup.getSectorID();
    }

    public String getSectorName(){
        return sectorLookup.getSectorName();
    }

    public int getSubSectorID(){
        return subSectorLookup.getSubSectorID();
    }

    public String getSubSectorName(){
        return subSectorLookup.getSubSectorName();
    }

    @JsonIgnore
    public SectorLookup getSectorLookup() {
        return sectorLookup;
    }

    public void setSectorLookup(SectorLookup sectorLookup) {
        this.sectorLookup = sectorLookup;
    }


    @JsonIgnore
    public SubSectorLookup getSubSectorLookup() {
        return subSectorLookup;
    }

    public void setSubSectorLookup(SubSectorLookup subSectorLookup) {
        this.subSectorLookup = subSectorLookup;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public BigDecimal getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(BigDecimal currentRatio) {
        this.currentRatio = currentRatio;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockFundamentals that = (StockFundamentals) o;
        return Objects.equals(tickerSymbol, that.tickerSymbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickerSymbol);
    }
}
