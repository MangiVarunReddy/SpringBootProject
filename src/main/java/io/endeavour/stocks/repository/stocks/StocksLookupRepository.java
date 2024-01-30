package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StocksLookup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StocksLookupRepository extends JpaRepository<StocksLookup,String> {
}
