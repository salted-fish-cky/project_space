package com.cky.bos.domain;

import java.util.HashSet;
import java.util.Set;

public class Decidedzone {
    private String id;
    private String name;
    private Staff staff;
    private Set<Subarea> subarea = new HashSet<Subarea>();

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




    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Set<Subarea> getSubarea() {
        return subarea;
    }

    public void setSubarea(Set<Subarea> subarea) {
        this.subarea = subarea;
    }
}
