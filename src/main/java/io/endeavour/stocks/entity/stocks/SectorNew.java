package io.endeavour.stocks.entity.stocks;

import io.endeavour.stocks.vo.TopThreeStocksVO;

import java.util.List;

public class SectorNew {
    private int sectorID;
    private String sectorName;

    private List<TopThreeStocksVO> topThreeStocksVOList;

    public SectorNew(int sectorID, String sectorName, List<TopThreeStocksVO> topThreeStocksForSector) {
        this.sectorID = sectorID;
        this.sectorName = sectorName;
        this.topThreeStocksVOList=topThreeStocksForSector;
    }

    public List<TopThreeStocksVO> getTopThreeStocksVOList() {
        return topThreeStocksVOList;
    }

    public void setTopThreeStocksVOList(List<TopThreeStocksVO> topThreeStocksVOList) {
        this.topThreeStocksVOList = topThreeStocksVOList;
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
}
