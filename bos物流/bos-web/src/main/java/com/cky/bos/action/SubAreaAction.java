package com.cky.bos.action;

import com.cky.bos.action.base.BaseAction;
import com.cky.bos.domain.Region;
import com.cky.bos.domain.Subarea;
import com.cky.bos.service.SubAreaService;
import com.cky.bos.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@Scope("prototype")
public class SubAreaAction extends BaseAction<Subarea> {
    @Autowired
    private SubAreaService subAreaService;

    public String add(){
        subAreaService.save(model);
        return LIST;
    }

    /**
     * 分页查询
     * @return
     */
    public String pageQuery(){
        System.out.println(model);

        DetachedCriteria dc = pageBean.getDetachedCriteria();
        //动态添加过滤条件
        String addresskey = model.getAddresskey();

        if(StringUtils.isNotBlank(addresskey)){
            //添加过滤条件，根据地址关键字模糊查询
            dc.add(Restrictions.like("addresskey","%"+addresskey+"%"));
        }
        Region region = model.getRegion();
        if(region!=null){
            //添加过滤条件 -----多表关联查询  ，使用别名方式实现
            dc.createAlias("region","r");
            String city = region.getCity();
            if(StringUtils.isNotBlank(city)){
                //添加过滤条件，根据city关键字模糊查询
                dc.add(Restrictions.like("r.city","%"+city+"%"));
            }
            String district = region.getDistrict();

            if(StringUtils.isNotBlank(district)){
                //添加过滤条件，根据district关键字模糊查询
                dc.add(Restrictions.like("r.district","%"+district+"%"));
            }
            String province = region.getProvince();
            if(StringUtils.isNotBlank(province)){
                //添加过滤条件，根据province关键字模糊查询
                dc.add(Restrictions.like("r.province","%"+province+"%"));
            }


        }

        subAreaService.pageQuery(pageBean);

        this.java2Json(pageBean,new String[]{"currentPage","detachedCriteria","pageSize","decidedzone","subareas"});

        return NONE;
    }


    /**
     * 分区数据导出功能
     * @return
     */
    public String exportXls() throws IOException {
        //第一步，查询所有分区数据
        List<Subarea> subareas =  subAreaService.findAll();
        //第二布，使用POI将数据写到Excel文件中

        //在内存中创建一个Excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个标签页
        HSSFSheet sheet = workbook.createSheet("分区数据");
        //创建标题行
        HSSFRow headRow = sheet.createRow(0);
        headRow.createCell(0).setCellValue("分区编号");
        headRow.createCell(1).setCellValue("开始编号");
        headRow.createCell(2).setCellValue("结束编号");
        headRow.createCell(3).setCellValue("位置信息");
        headRow.createCell(4).setCellValue("省市区");


        for(Subarea subarea:subareas){
            HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
            dataRow.createCell(0).setCellValue(subarea.getId());
            dataRow.createCell(1).setCellValue(subarea.getStartnum());
            dataRow.createCell(2).setCellValue(subarea.getEndnum());
            dataRow.createCell(3).setCellValue(subarea.getPosition());
            dataRow.createCell(4).setCellValue(subarea.getRegion().getName());
        }
        //第三布，使用输出流进行文件下载(一个流两个头)
        ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();

        String fileName = "分区数据.xls";
        String mimeType = ServletActionContext.getServletContext().getMimeType(fileName);
        ServletActionContext.getResponse().setContentType(mimeType);
        //获取客户端浏览器类型
        String agent = ServletActionContext.getRequest().getHeader("User-Agent");

        fileName = FileUtils.encodeDownloadFilename(fileName,agent);
        ServletActionContext.getResponse().setHeader("content-disposition","attachment;fileName="+fileName);
        workbook.write(outputStream);
        return NONE;
    }

    public String listAjax(){
        List<Subarea> list = subAreaService.findListNotAssociation();
        java2Json(list,new String[] {"decidedzone","region"});
        return NONE;
    }

    private String decidedzoneId;

    public void setDecidedzoneId(String decidedzoneId) {
        this.decidedzoneId = decidedzoneId;
    }

    /**
     * 根据定区id查询关联的分区
     * @return
     */
    public String findListByDecidedzoneId(){
        List<Subarea> list = subAreaService.findListByDecidedzoneId(decidedzoneId);
        java2Json(list,new String[]{"decidedzone","subareas"});
        return NONE;
    }

    /**
     * 查询区域分区分布图数据
     * @return
     */
    public String findSubAreasGroupByProvince(){
        List<Object> list = subAreaService.findSubAreasGroupByProvince();
        java2Json(list,new String[]{});
        return NONE;
    }
}
