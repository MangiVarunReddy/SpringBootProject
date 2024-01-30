package io.endeavour.stocks.entity.stocks;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "subsector_lookup",schema = "endeavour")
public class SubSectorLookup {

    @Column(name="subsector_id")
    @Id
    private  Integer subSectorID;
    @Column(name="subsector_name")
    private String subSectorName;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "sector_id")
    private SectorLookup sectorLookup;

    public String getSectorName(){
        return sectorLookup.getSectorName();

    }

    public Integer getSubSectorID() {
        return subSectorID;
    }

    public void setSubSectorID(Integer subSectorID) {
        this.subSectorID = subSectorID;
    }

    public String getSubSectorName() {
        return subSectorName;
    }

    public void setSubSectorName(String subSectorName) {
        this.subSectorName = subSectorName;
    }

    public SectorLookup getSectorLookup() {
        return sectorLookup;
    }

    public void setSectorLookup(SectorLookup sectorLookup) {
        this.sectorLookup = sectorLookup;
    }

    @Override
    public String toString() {
        return "SubSectorLookup{" +
                "subSectorID=" + subSectorID +
                ", subSectorName='" + subSectorName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubSectorLookup that = (SubSectorLookup) o;
        return Objects.equals(subSectorID, that.subSectorID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subSectorID);
    }
}
