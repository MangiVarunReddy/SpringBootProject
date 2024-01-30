package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.SubSectorLookup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubSectorRepository extends JpaRepository<SubSectorLookup,Integer> {
}
