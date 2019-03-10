package com.cky.upload.servlet;

import com.cky.upload.bean.FileUploadBean;
import com.cky.upload.db.FileUploadDao;
import com.cky.upload.db.FileUploadDaoImp;
import com.cky.upload.exception.InvalidExtNameException;
import com.cky.upload.utils.FileUploadAppProperties;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@WebServlet(name = "FileUploadServlet",urlPatterns = "/upload")
public class FileUploadServlet extends HttpServlet {
    private static final String FILE_PATH = "\\WEB-INF\\files";
    private FileUploadDao dao = new FileUploadDaoImp();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = null;
        request.setCharacterEncoding("UTF-8");
        ServletFileUpload upload = getServletFileUpload();

        try {
            //把需要上传的FileItem都放入到该map中，key为路径
            Map<String,FileItem> uploadFiles = new HashMap<>();
            //解析请求得到FileItem集合
            List<FileItem> items = upload.parseRequest(request);
            //1.构建FileUploadBean集合,同时填充Map uploadFiles
            List<FileUploadBean> beans = buildFileUploadBeans(items,uploadFiles);

            //2.校验扩展名
            vaidateExtName(beans);
            //3.校验文件大小

            //4.进行文件上传
            upload(uploadFiles);
            // 5.把上传的信息保存到数据库中
            saveBeans(beans);
            path = "/app/success.jsp";
        }catch (Exception e){
            e.printStackTrace();
            path = "app/update.jsp";
            System.out.println("message"+e.getMessage());
            request.setAttribute("message",e.getMessage());
        }finally {
            request.getRequestDispatcher(path).forward(request,response);

        }
    }

    /**
     * 保存数据到数据库
     * @param beans
     */
    private void saveBeans(List<FileUploadBean> beans) {
        dao.save(beans);
    }

    /**
     * 文件上传时的准备工作，得到filePath和InputStream
     * @param uploadFiles
     * @throws IOException
     */
    private void upload(Map<String, FileItem> uploadFiles) throws IOException {
        for(Map.Entry<String,FileItem> fileUpload:uploadFiles.entrySet()){
            String filePath = fileUpload.getKey();
            FileItem item = fileUpload.getValue();

            upload(filePath,item.getInputStream());
        }
    }

    /**
     * 文件开始上传
     * @param filePath
     * @param inputStream
     * @throws IOException
     */
    private void upload(String filePath, InputStream inputStream) throws IOException {
        OutputStream out = new FileOutputStream(filePath);
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inputStream.read(buffer))!=-1){
            out.write(buffer,0,len);
        }
        inputStream.close();
        out.close();
        System.out.println(filePath);
    }

    /**
     * 校验扩展名是否合法
     * @param beans
     */
    private void vaidateExtName(List<FileUploadBean> beans) {
        String exts = FileUploadAppProperties.getInstance().getProperty("exts");
        List<String> extList = Arrays.asList(exts.split(","));
        for(FileUploadBean bean:beans){
            String fileName = bean.getFile_name();
            String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
            if(!extList.contains(extName)){

                throw new InvalidExtNameException(fileName+"文件的扩展名不合法");
            }
        }
    }

    /**
     * 利用传入的FileItem的集合，构建FileUploadBean的集合，同时填充uploadFiles
     * @param items
     * @param uploadFiles
     * @return
     */
    private List<FileUploadBean> buildFileUploadBeans(List<FileItem> items,Map<String,FileItem> uploadFiles) throws UnsupportedEncodingException {
        List<FileUploadBean> beans = new ArrayList<>();
        //得到所有的desc
        Map<String,String> descs = new HashMap<>();
        for(FileItem item:items){
            if(item.isFormField()){
                descs.put(item.getFieldName(),item.getString("UTF-8"));

            }
        }
        //得到Fileitem对象
        for(FileItem item:items){
            if(!item.isFormField()){
                //得到key 的名字
                String fieldName = item.getFieldName();
                //得到文件的名字
                String name = item.getName();
                String index = fieldName.substring(fieldName.length()-1);
                String desc = descs.get("desc" + index);
                String filePath = getFilePath(name);
                String replacePath = filePath.replace("\\","//");
                System.out.println(replacePath);
                FileUploadBean bean = new FileUploadBean(name,replacePath,desc);
                beans.add(bean);
                uploadFiles.put(filePath,item);
            }
        }
        return beans;
    }

    /**
     * 1.根据文件名构建一个随机的文件名
     * 2.利用ServletContext的getRealPath方法获取绝对路径
     *
     * @param name
     * @return
     */
    private String getFilePath(String name) {
        String extName = name.substring(name.lastIndexOf("."));
        Random random = new Random();
        int randomNum = random.nextInt(100000);
        String filePath = getServletContext().getRealPath(FILE_PATH)+"\\"+System.currentTimeMillis()+randomNum+extName;
//        System.out.println(filePath);
        return filePath;
    }

    /**
     * 构建ServletUpload对象
     * 从配置文件中读取了部分属性
     * @return
     */
    private ServletFileUpload getServletFileUpload() {
        String fileMaxSize = FileUploadAppProperties.getInstance().getProperty("file.max.size");
        String allMaxSize = FileUploadAppProperties.getInstance().getProperty("total.file.max.size");

        DiskFileItemFactory factory = new DiskFileItemFactory();

// Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        factory.setSizeThreshold(1024*500);
        File tempDicretory = new File("G:\\tempDicretory");
        factory.setRepository(tempDicretory);

        upload.setFileSizeMax(Integer.parseInt(fileMaxSize));
        upload.setSizeMax(Integer.parseInt(allMaxSize));
        return upload;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
