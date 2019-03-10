package com.cky.demo;

public class Authority {
    private String displayName;
    private String url;

    public Authority(String displayName, String url) {
        this.displayName = displayName;
        this.url = url;
    }
    public Authority(){

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authority authority = (Authority) o;

        return url.equals(authority.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
