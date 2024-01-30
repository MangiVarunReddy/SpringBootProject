package io.endeavour.stocks.entity.stocks;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "stock_fundamentals",schema = "endeavour")
public class StockFundamentals {
    @Column(name = "ticker_symbol")
    @Id
    private String tickerSymbol;

    @OneToOne
    @JoinColumn(name = "sector_id", referencedColumnName = "sector_id")
    private SectorLookup sectorLookup;

    @OneToOne
    @JoinColumn(name = "subsector_id", referencedColumnName = "subsector_id")
    private SubSectorLookup subSectorLookup;


    @Column(name = "market_cap")
    private BigDecimal marketCap;
    @Column(name = "current_ratio")
    private BigDecimal currentRatio;

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
