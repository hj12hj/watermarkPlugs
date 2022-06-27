package com.hj.interceptor;

import com.hj.AddWaterMarkUtil;
import com.hj.aop.AddWaterMarkAdvisor;
import com.hj.core.AddWaterMark;
import com.hj.core.enums.WaveMarkMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * 拦截器方式实现
 */
public class AddWaterMarkInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(AddWaterMarkInterceptor.class);

    private final String TOMCAT_TEMP_FILE = "part.fileItem.dfos.outputFile";

    /**
     * 本地缓存路径
     */
    private final String TEMP_PATH = System.getProperty("user.dir");//AddWaterMarkAdvisor.class.getClassLoader().getResource("").getPath();



    private AddWaterMark addWaterMark;

    public AddWaterMarkInterceptor(AddWaterMark addWaterMark) {
        this.addWaterMark = addWaterMark;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();
            Object[] objects = fileMap.values().toArray();
            MultipartFile multipartFile = (MultipartFile) objects[0];

            String flag = request.getParameter("flag");
            String mode = request.getParameter("mode");
            String picPath = request.getParameter("picPath");
            String contentValue = request.getParameter("content");
            WaveMarkMode waveMarkMode;
            if ("pic".equals(mode)) {
                waveMarkMode = WaveMarkMode.PICTURE;
            } else {
                waveMarkMode = WaveMarkMode.WORDS;
            }
            if ("true".equals(flag)) {
                //这里遇到不支持的类型直接返回
                String originalFilename = multipartFile.getOriginalFilename();
                String suffix = originalFilename.split("\\.")[1];
                boolean containsKey = AddWaterMarkUtil.getAddWaterMarkMap().containsKey(suffix);
                if (!containsKey)
                    return true;
                //这里实现文件替换操作
                // 实现动态替换 File
                MetaObject metaObject = SystemMetaObject.forObject(multipartFile);
                //删除原来的缓存文件
                File file = (File) metaObject.getValue(TOMCAT_TEMP_FILE);
                //todo 属性设置
                addWaterMark.setWaveMarkMode(waveMarkMode);
                addWaterMark.setPicPath(picPath);
                addWaterMark.transfer(file.getPath(), TEMP_PATH + multipartFile.getOriginalFilename(), contentValue);
                file.delete();
                metaObject.setValue(TOMCAT_TEMP_FILE, new File(TEMP_PATH + multipartFile.getOriginalFilename()));
            }

        }


        return true;
    }
}
