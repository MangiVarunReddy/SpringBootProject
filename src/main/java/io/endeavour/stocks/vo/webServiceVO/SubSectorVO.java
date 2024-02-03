package io.endeavour.stocks.vo.webServiceVO;

import java.util.List;

public class SubSectorVO {
    private int sunSectorID;
    private String subSectorName;

    private List<StocksListVO> topStocks;

    public int getSunSectorID() {
        return sunSectorID;
    }

    public void setSunSectorID(int sunSectorID) {
        this.sunSectorID = sunSectorID;
    }

    public String getSubSectorName() {
        return subSectorName;
    }

    public void setSubSectorName(String subSectorName) {
        this.subSectorName = subSectorName;
    }

    public List<StocksListVO> getTopStocks() {
        return topStocks;
    }

    public void setTopStocks(List<StocksListVO> topStocks) {
        this.topStocks = topStocks;
    }

    @Override
    public String toString() {
        return "SubSectorVO{" +
                "sunSectorID=" + sunSectorID +
                ", subSectorName='" + subSectorName + '\'' +
                ", topStocks=" + topStocks +
                '}';
    }
}
