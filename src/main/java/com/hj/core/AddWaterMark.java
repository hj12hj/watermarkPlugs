package com.hj.core;

import com.itextpdf.text.DocumentException;

import java.io.IOException;

/**
 * 转换接口
 */
public interface AddWaterMark {
    /**
     *  初始化设置默认变量
     */
    void init();

    /**
     * 转换
     * @param sourcePath
     * @param targetPath
     * @param WaterMarkContent
     * @throws DocumentException
     * @throws IOException
     */
    void transfer(String sourcePath,String targetPath,String WaterMarkContent) throws DocumentException, IOException;
}
