package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.SectorLookup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorLookupRepository extends JpaRepository<SectorLookup,Integer> {
}
