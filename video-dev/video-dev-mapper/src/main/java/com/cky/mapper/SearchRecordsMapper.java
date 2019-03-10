package com.cky.mapper;

import com.cky.pojo.SearchRecords;
import com.cky.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {

    public List<String> getHotWords();
}