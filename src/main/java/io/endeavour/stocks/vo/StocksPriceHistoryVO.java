package io.endeavour.stocks.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Schema(name = "StocksPriceHistoryVO",description ="Object that contains key information for a given Ticker Symbol" )
public class StocksPriceHistoryVO {


    @Schema(name = "tickerSymbol", description = "Ticker Symbol of the stock in question",example = "COST")
    private String tickerSymbol;
    @Schema(name = "tradingDate", description = "This is the current date ",example = "2023-01-01")
    private LocalDate tradingDate;
    @Schema(name = "openPrice", description = "This is the openPrice ",example = "148.56")
    private BigDecimal openPrice;
    @Schema(name = "closePrice", description = "This is the closePrice ",example = "152.78")
    private  BigDecimal closePrice;
    @Schema(name = "volume", description = "This is the volume ",example = "30478788")
    private long volume;



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

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "StocksPriceHistoryVO{" +
                "tickerSymbol='" + tickerSymbol + '\'' +
                ", tradingDate='" + tradingDate + '\'' +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", volume=" + volume +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StocksPriceHistoryVO that = (StocksPriceHistoryVO) o;
        return volume == that.volume && Objects.equals(tickerSymbol, that.tickerSymbol) && Objects.equals(tradingDate, that.tradingDate) && Objects.equals(openPrice, that.openPrice) && Objects.equals(closePrice, that.closePrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickerSymbol, tradingDate, openPrice, closePrice, volume);
    }
}
