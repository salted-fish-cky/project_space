

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@WebServlet(name = "UploadServlet",urlPatterns = "/upload")
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.得到fileItem的集合items
        // Create a factory for disk-based file items
        DiskFileItemFactory  factory = new DiskFileItemFactory();

// Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        factory.setSizeThreshold(1024*500);
        File tempDicretory = new File("G:\\tempDicretory");
        factory.setRepository(tempDicretory);

        upload.setSizeMax(1024*1024*20);

// Parse the request
        try {
            List<FileItem> /* FileItem */ items = upload.parseRequest(request);
            //遍历items
            for(FileItem item:items){
                //2.遍历items:若是一个一般的表单域，打印信息
                if(item.isFormField()){
                    String fieldName = item.getFieldName();
                    String value = item.getString();
                    System.out.println(fieldName+":"+value);
                }else{
                    //若是文件域则把文件保存到g:\\files 目录下
                    String fieldName = item.getFieldName();
                    String fileName = item.getName();
                    String contentType = item.getContentType();
                    long sizeInBytes = item.getSize();

                    System.out.println(fieldName);
                    System.out.println(fileName);
                    System.out.println(contentType);
                    System.out.println(sizeInBytes);

                    InputStream inputStream = item.getInputStream();
                    byte[] buffer = new byte[1024*4];
                    int len = 0;
                    fileName = "G:\\files\\"+fileName;
                    System.out.println(fileName);
                    OutputStream out = new FileOutputStream(fileName);
                    while((len = inputStream.read())!= -1){
                        out.write(buffer,0,len);
                    }
                    inputStream.close();
                    out.close();


                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }




    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
