package com.cky.bos.test;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class POITest {

    @Test
    public void test1() throws FileNotFoundException,IOException {
        String filePath = "F:\\黑马视频\\BOS-day05\\BOS-day05\\资料\\区域导入测试数据.xls";
        //包装一个excel文件对象
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(filePath)));
        //读取文件中第一个Sheet标签页
        HSSFSheet sheet = workbook.getSheetAt(0);
        //遍历标签页所有的行
        for(Row row:sheet){
            System.out.println();
            for(Cell cell:row){
                String cellValue = cell.getStringCellValue();
                System.out.print(cellValue);
            }
        }
    }
}
