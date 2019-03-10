package com.cky.bos.domain;

public class Subarea {
    private String id;
    private String addresskey;
    private String startnum;
    private String endnum;
    private String single;
    private String position;
    private Decidedzone decidedzone;
    private Region region;

    public String getSubareaid(){

        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddresskey() {
        return addresskey;
    }

    public void setAddresskey(String addresskey) {
        this.addresskey = addresskey;
    }

    public String getStartnum() {
        return startnum;
    }

    public void setStartnum(String startnum) {
        this.startnum = startnum;
    }

    public String getEndnum() {
        return endnum;
    }

    public void setEndnum(String endnum) {
        this.endnum = endnum;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public Decidedzone getDecidedzone() {
        return decidedzone;
    }

    public void setDecidedzone(Decidedzone decidedzone) {
        this.decidedzone = decidedzone;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
