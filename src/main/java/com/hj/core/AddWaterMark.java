package com.hj.core;

import com.hj.core.enums.WaveMarkMode;
import com.itextpdf.text.DocumentException;

import java.io.IOException;

/**
 * 转换接口
 */
public interface AddWaterMark {
    /**
     * 初始化设置默认变量
     */
    void init();

    /**
     * 转换
     *
     * @param sourcePath
     * @param targetPath
     * @param waterMarkContent
     * @throws DocumentException
     * @throws IOException
     */
    void transfer(String sourcePath, String targetPath, String waterMarkContent) throws DocumentException, IOException;

    /**
     * 水印模式
     *
     * @param waveMarkMode
     */
    void setWaveMarkMode(WaveMarkMode waveMarkMode);

    /**
     * 图片路径
     * @param picPath
     */
    void setPicPath(String picPath);

    /**
     * 水印属性
     * @param waterMarkAttribute
     */
    void setWaterMarkAttribute(WaterMarkAttribute waterMarkAttribute);
}
