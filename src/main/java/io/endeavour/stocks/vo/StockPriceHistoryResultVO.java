package io.endeavour.stocks.vo;

import java.math.BigDecimal;
import java.util.List;

public class StockPriceHistoryResultVO {
    private BigDecimal marketCap;
    private BigDecimal currentRatio;


    private List<StockPriceHistoryDataChildVO> stockPriceHistoryDataChildVO;

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

    public List<StockPriceHistoryDataChildVO> getStockPriceHistoryDataChildVO() {
        return stockPriceHistoryDataChildVO;
    }

    public void setStockPriceHistoryDataChildVO(List<StockPriceHistoryDataChildVO> stockPriceHistoryDataChildVO) {
        this.stockPriceHistoryDataChildVO = stockPriceHistoryDataChildVO;
    }
}
