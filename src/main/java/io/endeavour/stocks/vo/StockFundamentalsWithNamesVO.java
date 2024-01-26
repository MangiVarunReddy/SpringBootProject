package io.endeavour.stocks.vo;

import java.math.BigDecimal;

public class StockFundamentalsWithNamesVO {

    private String tickerSymbol;
    private String tickerName;
    private int sectorID;
    private String sectorName;
    private int subSectorID;
    private String subSectorName;
    private BigDecimal marketCap;
    private BigDecimal currentRatio;

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getTickerName() {
        return tickerName;
    }

    public void setTickerName(String tickerName) {
        this.tickerName = tickerName;
    }

    public int getSectorID() {
        return sectorID;
    }

    public void setSectorID(int sectorID) {
        this.sectorID = sectorID;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public int getSubSectorID() {
        return subSectorID;
    }

    public void setSubSectorID(int subSectorID) {
        this.subSectorID = subSectorID;
    }

    public String getSubSectorName() {
        return subSectorName;
    }

    public void setSubSectorName(String subSectorName) {
        this.subSectorName = subSectorName;
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
    public String toString() {
        return "StockFundamentalsWithNamesVO{" +
                "tickerSymbol='" + tickerSymbol + '\'' +
                ", tickerName='" + tickerName + '\'' +
                ", sectorID=" + sectorID +
                ", sectorName='" + sectorName + '\'' +
                ", sunSectorID=" + subSectorID +
                ", sunSectorName='" + subSectorName + '\'' +
                ", marketCap=" + marketCap +
                ", currentRatio=" + currentRatio +
                '}';
    }
}
