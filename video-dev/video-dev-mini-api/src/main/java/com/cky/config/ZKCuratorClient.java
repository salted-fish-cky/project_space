package com.cky.config;

import com.cky.enums.BGMOperatorTypeEnum;
import com.cky.utils.JsonUtils;
import org.apache.commons.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.Map;

@Component
public class ZKCuratorClient {

    @Value("${com.cky.fileSpace}")
    private String FILE_SPACE;

    private CuratorFramework client = null;

    final static Logger log = LoggerFactory.getLogger(ZKCuratorClient.class);

    @Value("${com.cky.zookeeperServer}")
     private  String ZOOKEEPER_SERVER;

    @Value("${com.cky.bgmServer}")
    private  String BGM_SERVER;

    public void init(){
        if(client != null){
            return;
        }
        //重连策略
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000,5);
        //创建zk客户端
        client = CuratorFrameworkFactory.builder().connectString(ZOOKEEPER_SERVER).sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy).namespace("admin").build();
        client.start();
        try {
//            String data = new String(client.getData().forPath("/bgm/18052674D26HH3X6"));
//            log.info("获取到的数据为：{}",data);
            addChildWatch("/bgm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addChildWatch(String nodePath) throws Exception {

        final PathChildrenCache cache = new PathChildrenCache(client,nodePath,true);
        cache.start();
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent)
                    throws Exception {
                //添加
                if(pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)){

                    //1.从数据库查询bgm对象，获取路径path
                    String path = pathChildrenCacheEvent.getData().getPath();
                    String operMap = new String(pathChildrenCacheEvent.getData().getData());
                    Map<String,String> map = JsonUtils.jsonToPojo(operMap,Map.class);
                    String operatorType = map.get("operType");
                    String songPath = map.get("path");
//                    String[] arr = path.split("/");
//                    String bgmId = arr[arr.length - 1];
//                    Bgm bgm = bgmService.queryBgmById(bgmId);
//                    if(bgm == null){
//                        return;
//                    }
//                    String songPath = bgm.getPath();
                    //2.定义保存到本地的bgm路径
                    String filePath = FILE_SPACE+songPath;

                    //3.定义下载的路径(播放的url)
//                    String[] pathArr = songPath.split("\\\\");
                    String finalPath = songPath;
                    //处理url的斜杆以及编码
//                    for (int i = 0; i < pathArr.length; i++) {
//                        if (StringUtils.isNotBlank(pathArr[i])){
//                            finalPath += "/";
//                            finalPath += URLEncoder.encode(pathArr[i],"utf-8");
//                        }
//
//                    }

                    String bgmUrl = BGM_SERVER+finalPath;

                    //添加bgm
                    if(operatorType.equals(BGMOperatorTypeEnum.ADD.type)){
                        URL url = new URL(bgmUrl);
                        File file = new File(filePath);
                        FileUtils.copyURLToFile(url,file);
                        client.delete().forPath(path);
                        //删除bgm
                    }else if(operatorType.equals(BGMOperatorTypeEnum.DELETE.type)){
                        File file = new File(filePath);
                        FileUtils.forceDelete(file);
                        client.delete().forPath(path);
                    }

                }
            }
        });

    }
}
