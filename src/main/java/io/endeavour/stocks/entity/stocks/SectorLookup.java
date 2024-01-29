package io.endeavour.stocks.entity.stocks;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sector_lookup",schema = "endeavour")
public class SectorLookup {

    @Column(name = "sector_id")
    @Id
    private Integer sectorID;
    @Column(name = "sector_name")
    private String sectorName;

    @OneToMany(mappedBy ="sectorLookup", fetch = FetchType.EAGER)
   List<SubSectorLookup> subSectorLookupList;

    public Integer getSectorID() {
        return sectorID;
    }

    public void setSectorID(Integer sectorID) {
        this.sectorID = sectorID;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public List<SubSectorLookup> getSubSectorLookupList() {
        return subSectorLookupList;
    }

    public void setSubSectorLookupList(List<SubSectorLookup> subSectorLookupList) {
        this.subSectorLookupList = subSectorLookupList;
    }

    @Override
    public String toString() {
        return "SectorLookup{" +
                "sectorID=" + sectorID +
                ", sectorName='" + sectorName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectorLookup that = (SectorLookup) o;
        return Objects.equals(sectorID, that.sectorID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectorID);
    }
}
