package com.cky.bos.action;

import com.cky.bos.action.base.BaseAction;
import com.cky.bos.domain.Region;
import com.cky.bos.service.RegionService;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("prototype")
public class RegionAction extends BaseAction<Region>{

    private File regionFile;
    private String q;

    @Autowired
    private RegionService regionService;
    private List<Region> regions = new ArrayList<Region>();

    public void setRegionFile(File regionFile) {
        this.regionFile = regionFile;
    }


    public String importXls() throws IOException, BadHanyuPinyinOutputFormatCombination {
//        使用POI解析Excel
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(regionFile));
        //获取指定标签页
        HSSFSheet sheet = workbook.getSheetAt(0);
        for(Row row:sheet){
            int rowNum = row.getRowNum();
            if(rowNum == 0){
                continue;
            }
            String id = row.getCell(0).getStringCellValue();
            String province = row.getCell(1).getStringCellValue();
            String city = row.getCell(2).getStringCellValue();
            String district = row.getCell(3).getStringCellValue();
            String postcode = row.getCell(4).getStringCellValue();

            //包装一个区域对象
            Region region = new Region(id, province, city, district, postcode, null, null, null);
            province = province.substring(0,province.length()-1);
            city = city.substring(0,city.length()-1);
            district = district.substring(0,district.length()-1);

            String info = province+city+district;
            //简码
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            //获取首字母简拼
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            String shortcode = "";
            char[] c = info.toCharArray();
            for (int i = 0; i <c.length ; i++) {
                String[] strings = PinyinHelper.toHanyuPinyinStringArray(c[i],format);
                shortcode+=strings[0].charAt(0);

            }
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            String citycode = PinyinHelper.toHanyuPinyinString(city, format, "");
            region.setShortcode(shortcode);
            region.setCitycode(citycode);
            regions.add(region);
        }
        regionService.saveBench(regions);
        return NONE;
    }

    /**
     * 查询所有数据返回json数据
     * @return
     */
    public String listajax(){
        List<Region> list = null;
        if(StringUtils.isNotBlank(q)){
            list = regionService.findByQ(q);

        }else{
            list = regionService.findAll();
            System.out.println(list);
        }
        java2Json(list,new String[]{"subareas"});
        return NONE;
    }

    public String pageQuery() throws IOException {
        regionService.pageQuery(pageBean);

        java2Json(pageBean,new String[]{"currentPage","detachedCriteria","pageSize","subareas"});
//        Map map = new HashMap();
//        map.put("rows",pageBean.getRows());
//        map.put("total",pageBean.getTotal());
//        String json = JSON.toJSONString(map);
//
//        ServletActionContext.getResponse().setContentType("text/json;charset=utf-8");
//        ServletActionContext.getResponse().getWriter().write(json);

        return NONE;
    }


    public void setQ(String q) {
        this.q = q;
    }
}
