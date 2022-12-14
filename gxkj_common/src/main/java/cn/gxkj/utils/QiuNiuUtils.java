package cn.gxkj.utils;

import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.util.Date;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--21 15:51:00
 */
public class QiuNiuUtils {

    String accessKey = "KIR2kWH0qzrRw3V2b8zsu8Lx5f4Z-XtfwffphWR8";
    String secretKey = "pGol8PElzsUJDN5b_qBtIRHBZqRmh-WviU5qJs5y";
    String bucket = "gxkj-rlzy";
    String prefix = "http://rfczjknrd.hn-bkt.clouddn.com";//域名
    private UploadManager manager;
    public QiuNiuUtils() {
        //初始化基本配置
        Configuration cfg = new Configuration(Region.autoRegion());
        //创建上传管理器
        manager = new UploadManager(cfg);
    }

    //文件名 = key
    //文件的byte数组
    public String upload(String imgName , byte [] bytes) {
        Auth auth = Auth.create(accessKey, secretKey);
        //构造覆盖上传token
        String upToken = auth.uploadToken(bucket,imgName);
        try {
            Response response = manager.put(bytes, imgName, upToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //返回请求地址
            return prefix+"/"+putRet.key+"?t="+System.currentTimeMillis();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
