package com.hj.interceptor;

import com.hj.AddWaterMarkUtil;
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
import java.io.*;
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
    private final String TEMP_PATH = System.getProperty("user.dir");



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
                //这里遇到不支持的类型直接返回
                String originalFilename = multipartFile.getOriginalFilename();
                String suffix = originalFilename.split("\\.")[1];
                boolean containsKey = AddWaterMarkUtil.getAddWaterMarkMap().containsKey(suffix);
                if (!containsKey)
                    return true;

                //这里实现文件替换操作
                // 实现动态替换 File
                MetaObject metaObject = SystemMetaObject.forObject(multipartFile);
                //得到原来的缓存文件
                File file = (File) metaObject.getValue(TOMCAT_TEMP_FILE);
                //word 格式的容错
                if ("doc".equals(suffix)||"docx".equals(suffix)) {
                    String oldPath = file.getPath();
                    int i = oldPath.lastIndexOf(".");
                    String newPath = oldPath.substring(0, i - 1) + "." + suffix;
                    MetaObject fileMetaObject = SystemMetaObject.forObject(file);
                    fileMetaObject.setValue("path", newPath);
                    copyByInOutStream(new File(oldPath), new File(newPath));
                    new File(oldPath).delete();
                }
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

    private   void copyByInOutStream(File srcFile, File destFile) throws IOException {
        byte[] bytes = new byte[24 * 1024];
        try (InputStream in = new FileInputStream(srcFile);
             OutputStream out = new FileOutputStream(destFile)) {
            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
        }
    }
}
