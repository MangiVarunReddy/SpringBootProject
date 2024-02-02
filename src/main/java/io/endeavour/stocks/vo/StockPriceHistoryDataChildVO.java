package io.endeavour.stocks.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StockPriceHistoryDataChildVO {
    private String tickerSymbol;
    private String tickerName;
    private LocalDate tradingDate;
    private BigDecimal openPrice;
    private  BigDecimal closePrice;
    private long volume;

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

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "StockPriceHistoryDataChildVO{" +
                "tickerSymbol='" + tickerSymbol + '\'' +
                ", tickerName='" + tickerName + '\'' +
                ", tradingDate=" + tradingDate +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", volume=" + volume +
                '}';
    }
}
