package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StockFundamentals;
import io.endeavour.stocks.entity.stocks.StockFundamentalsNew;
import io.endeavour.stocks.vo.TopThreeStocksVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopThreeStocksRepository extends JpaRepository<StockFundamentals,String> {

    @Query(name = "StocksFundamentalsNew.TopThreeStocks",nativeQuery = true)
    public List<TopThreeStocksVO> getTopThreeStocksForEachSector();
}
