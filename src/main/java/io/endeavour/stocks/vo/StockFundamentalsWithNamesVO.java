package io.endeavour.stocks.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "StockFundamentalsWithNamesVO", description = "Object that contains key information for a given Ticker Symbol")
public class StockFundamentalsWithNamesVO {

    @Schema(name = "tickerSymbol", description = "Ticker Symbol of the stock in question",example = "COST")
    private String tickerSymbol;
    @Schema(name = "tickerName",description = "Name of the company",example = "Costco Wholesale Corp")
    private String tickerName;
    @Schema(name = "sectorID",description = "Sector ID of the stock belongs to",example = "19")
    private int sectorID;
    @Schema(name = "sectorName",description = "Name of the sector where the stock belongs to",example = "Consumer Defensive")
    private String sectorName;
    @Schema(name = "subSectorID",description = "subSector ID of the stock belongs to",example = "193")
    private int subSectorID;
    @Schema(name = "subSectorName",description = "Name of the sub sector where the stock belongs to",example = "Discount Stores")
    private String subSectorName;
    @Schema(name = "marketCap",description = "Market cap valuation of the company",example = "304787881984")
    private BigDecimal marketCap;
    @Schema(name = "currentRatio",description = "Current Ratio of the company",example = "1.09")
    private BigDecimal currentRatio;
    @Schema(name = "cumulativeReturn",description = "cumulativeReturn of the stock in the given time frame",example = "1.58")
    private BigDecimal cumulativeReturn;

    public BigDecimal getCumulativeReturn() {
        return cumulativeReturn;
    }

    public void setCumulativeReturn(BigDecimal cumulativeReturn) {
        this.cumulativeReturn = cumulativeReturn;
    }

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
