import com.cky.upload.bean.FileUploadBean;
import com.cky.upload.db.FileUploadDao;
import com.cky.upload.db.FileUploadDaoImp;

import java.util.ArrayList;
import java.util.List;


public class Test {
    private FileUploadDao dao = new FileUploadDaoImp();
    List<FileUploadBean> beans = new ArrayList<>();

    public void save() throws Exception {
       FileUploadBean bean1 = new FileUploadBean("1","1","1");
       FileUploadBean bean2 = new FileUploadBean("2","2","2");
       beans.add(bean1);
       beans.add(bean2);
       dao.save(beans);
    }

    @org.junit.Test
    public void getAll() throws Exception {
        List<FileUploadBean> beans = dao.getAll();
        for (FileUploadBean bean:beans){
            System.out.println(bean.getId()+"-"+bean.getFile_name()
                    +"-"+bean.getFile_path()+"-"+bean.getFile_desc());
        }
    }
}
