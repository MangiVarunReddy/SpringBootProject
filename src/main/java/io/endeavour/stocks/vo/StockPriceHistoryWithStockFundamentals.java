package io.endeavour.stocks.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StockPriceHistoryWithStockFundamentals {
    private String tickerSymbol;
    private String tickerName;
    private BigDecimal marketCap;
    private BigDecimal currentRatio;
    private LocalDate tradingDate;
    private BigDecimal openPrice;
    private  BigDecimal closePrice;
    private long volume;

    public StockPriceHistoryWithStockFundamentals(
                                String tickerSymbol,
                                String tickerName,
                                BigDecimal marketCap,
                                BigDecimal currentRatio,
                                LocalDate tradingDate,
                                BigDecimal openPrice,
                                BigDecimal closePrice,
                                long volume) {
        this.tickerSymbol = tickerSymbol;
        this.tickerName = tickerName;
        this.marketCap = marketCap;
        this.currentRatio = currentRatio;
        this.tradingDate = tradingDate;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.volume = volume;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public String getTickerName() {
        return tickerName;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public BigDecimal getCurrentRatio() {
        return currentRatio;
    }

    public LocalDate getTradingDate() {
        return tradingDate;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public long getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return "StockPriceHistoryWithStockFundamentals{" +
                "tickerSymbol='" + tickerSymbol + '\'' +
                ", tickerName='" + tickerName + '\'' +
                ", marketCap=" + marketCap +
                ", currentRatio=" + currentRatio +
                ", tradingDate=" + tradingDate +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", volume=" + volume +
                '}';
    }
}
