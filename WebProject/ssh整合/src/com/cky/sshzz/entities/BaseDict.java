package com.cky.sshzz.entities;

public class BaseDict {
    private int dictId;
    private String dictTypeCode;
    private String dictTypeName;
    private String dictItemName;
    private String dictItemCode;
    private Integer dictSort;
    private Character dictEnable;
    private String dictMemo;

    public int getDictId() {
        return dictId;
    }

    public void setDictId(int dictId) {
        this.dictId = dictId;
    }

    public String getDictTypeCode() {
        return dictTypeCode;
    }

    public void setDictTypeCode(String dictTypeCode) {
        this.dictTypeCode = dictTypeCode;
    }

    public String getDictTypeName() {
        return dictTypeName;
    }

    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName;
    }

    public String getDictItemName() {
        return dictItemName;
    }

    public void setDictItemName(String dictItemName) {
        this.dictItemName = dictItemName;
    }

    public String getDictItemCode() {
        return dictItemCode;
    }

    public void setDictItemCode(String dictItemCode) {
        this.dictItemCode = dictItemCode;
    }

    public Integer getDictSort() {
        return dictSort;
    }

    public void setDictSort(Integer dictSort) {
        this.dictSort = dictSort;
    }

    public Character getDictEnable() {
        return dictEnable;
    }

    public void setDictEnable(Character dictEnable) {
        this.dictEnable = dictEnable;
    }

    public String getDictMemo() {
        return dictMemo;
    }

    public void setDictMemo(String dictMemo) {
        this.dictMemo = dictMemo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseDict baseDict = (BaseDict) o;

        if (dictId != baseDict.dictId) return false;
        if (dictTypeCode != null ? !dictTypeCode.equals(baseDict.dictTypeCode) : baseDict.dictTypeCode != null)
            return false;
        if (dictTypeName != null ? !dictTypeName.equals(baseDict.dictTypeName) : baseDict.dictTypeName != null)
            return false;
        if (dictItemName != null ? !dictItemName.equals(baseDict.dictItemName) : baseDict.dictItemName != null)
            return false;
        if (dictItemCode != null ? !dictItemCode.equals(baseDict.dictItemCode) : baseDict.dictItemCode != null)
            return false;
        if (dictSort != null ? !dictSort.equals(baseDict.dictSort) : baseDict.dictSort != null) return false;
        if (dictEnable != null ? !dictEnable.equals(baseDict.dictEnable) : baseDict.dictEnable != null) return false;
        if (dictMemo != null ? !dictMemo.equals(baseDict.dictMemo) : baseDict.dictMemo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dictId;
        result = 31 * result + (dictTypeCode != null ? dictTypeCode.hashCode() : 0);
        result = 31 * result + (dictTypeName != null ? dictTypeName.hashCode() : 0);
        result = 31 * result + (dictItemName != null ? dictItemName.hashCode() : 0);
        result = 31 * result + (dictItemCode != null ? dictItemCode.hashCode() : 0);
        result = 31 * result + (dictSort != null ? dictSort.hashCode() : 0);
        result = 31 * result + (dictEnable != null ? dictEnable.hashCode() : 0);
        result = 31 * result + (dictMemo != null ? dictMemo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseDict{" +
                "dictId=" + dictId +
                ", dictTypeCode='" + dictTypeCode + '\'' +
                ", dictTypeName='" + dictTypeName + '\'' +
                ", dictItemName='" + dictItemName + '\'' +
                ", dictItemCode='" + dictItemCode + '\'' +
                ", dictSort=" + dictSort +
                ", dictEnable=" + dictEnable +
                ", dictMemo='" + dictMemo + '\'' +
                '}';
    }
}
