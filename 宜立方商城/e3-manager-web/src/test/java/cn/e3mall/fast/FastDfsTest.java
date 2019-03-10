package cn.e3mall.fast;

import cn.e3mall.common.utils.FastDFSClient;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.IOException;

public class FastDfsTest {


    @Test
    public void testUpload() throws IOException, MyException {

        //创建一个配置文件，内容就是tracker服务器的地址
        //使用全局对象加载配置文件
        ClientGlobal.init("E:\\idea_workspace\\宜立方商城\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
        //创建一个TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient获得一个TrackerServer对象
        TrackerServer connection = trackerClient.getConnection();
        //创建一个StorageServer的引用，可以是null
        StorageServer storageServer = null;
        //创建一个StorageClient，参数需要Trackerserver和SorageServer
        StorageClient storageClient = new StorageClient(connection,storageServer);
        //使用StorageClient上传图片
        String[] strings = storageClient.upload_file("F:\\download\\图片\\0d60e1a575691ff2c2a58c2586e4f94c.jpg",
                "jpg", null);

        for (String str:strings){
            System.out.println(str);
        }


    }

    @Test
    public void testFastDfsClient() throws Exception {

        FastDFSClient fastDFSClient = new FastDFSClient("E:\\idea_workspace\\宜立方商城\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
        String str = fastDFSClient.uploadFile("F:\\download\\图片\\e5a72b8dbf0bb6cf47c9a899bc09829b.jpg");
        System.out.println(str);
    }
}
