package com.cky.bos.domain;

import java.util.HashSet;
import java.util.Set;

public class Staff {
    private String id;
    private String name;
    private String telephone;
    private String haspda = "0";
    private String deltag = "0";  //删除标识，1表示已删除，0表示未删除
    private String station;
    private String standard;
    private Set<Decidedzone> decidedzones = new HashSet<Decidedzone>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getHaspda() {
        return haspda;
    }

    public void setHaspda(String haspda) {
        this.haspda = haspda;
    }

    public String getDeltag() {
        return deltag;
    }

    public void setDeltag(String deltag) {
        this.deltag = deltag;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Staff staff = (Staff) o;

        if (id != null ? !id.equals(staff.id) : staff.id != null) return false;
        if (name != null ? !name.equals(staff.name) : staff.name != null) return false;
        if (telephone != null ? !telephone.equals(staff.telephone) : staff.telephone != null) return false;
        if (haspda != null ? !haspda.equals(staff.haspda) : staff.haspda != null) return false;
        if (deltag != null ? !deltag.equals(staff.deltag) : staff.deltag != null) return false;
        if (station != null ? !station.equals(staff.station) : staff.station != null) return false;
        if (standard != null ? !standard.equals(staff.standard) : staff.standard != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (haspda != null ? haspda.hashCode() : 0);
        result = 31 * result + (deltag != null ? deltag.hashCode() : 0);
        result = 31 * result + (station != null ? station.hashCode() : 0);
        result = 31 * result + (standard != null ? standard.hashCode() : 0);
        return result;
    }

    public Set<Decidedzone> getDecidedzones() {
        return decidedzones;
    }

    public void setDecidedzones(Set<Decidedzone> decidedzones) {
        this.decidedzones = decidedzones;
    }
}
