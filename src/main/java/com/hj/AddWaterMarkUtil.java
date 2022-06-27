package com.hj;

import com.hj.core.AddWaterMark;
import com.hj.core.WaterMarkAttribute;
import com.hj.core.enums.WaveMarkMode;
import com.hj.pdf.PdfAddWaterMark;
import com.hj.word.WordDocAddWaterMark;
import com.hj.word.WordDocxAddWaterMark;
import com.itextpdf.text.DocumentException;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 策略模式整合不同类型文件
 */
public class AddWaterMarkUtil implements AddWaterMark {
    private static Map<String, AddWaterMark> addWaterMarkMap = new HashMap<>();

    static {
        //pdf 解析
        addWaterMarkMap.put("pdf", new PdfAddWaterMark());
        //docx 解析
        addWaterMarkMap.put("docx", new WordDocxAddWaterMark());

        addWaterMarkMap.put("doc", new WordDocAddWaterMark());
    }

    public static Map<String, AddWaterMark>  getAddWaterMarkMap(){
        return addWaterMarkMap;
    }


    //todo 此处应该留出扩展
    public static void  addNewTypes(String suffix, AddWaterMark addWaterMark) {
        addWaterMarkMap.put(suffix, addWaterMark);
    }

    public AddWaterMarkUtil() {
        init();
    }

    @Override
    public void init() {

    }

    @Override
    public void transfer(String sourcePath, String targetPath, String waterMarkContent) throws DocumentException, IOException {
        String[] split = targetPath.split(File.separator);
        String fileName = split[split.length - 1];
        String suffix = fileName.split("\\.")[1];
        AddWaterMark addWaterMark = addWaterMarkMap.get(suffix);
        if (addWaterMark == null) {
            throw new RuntimeException("暂不支持此种类型文件水印添加");
        }
        addWaterMark.transfer(sourcePath, targetPath, waterMarkContent);
    }

    public AddWaterMarkUtil(WaveMarkMode waveMarkMode, String picPath, WaterMarkAttribute waterMarkAttribute) {
        addWaterMarkMap.values().forEach(item -> {
            item.setWaveMarkMode(waveMarkMode);
            item.setPicPath(picPath);
            item.setWaterMarkAttribute(waterMarkAttribute);
        });
    }

    @Override
    public void setWaveMarkMode(WaveMarkMode waveMarkMode) {
        addWaterMarkMap.values().forEach(item -> {
            item.setWaveMarkMode(waveMarkMode);
        });
    }

    @Override
    public void setPicPath(String picPath) {
        addWaterMarkMap.values().forEach(item -> {
            item.setPicPath(picPath);
        });
    }

    @Override
    public void setWaterMarkAttribute(WaterMarkAttribute waterMarkAttribute) {
        addWaterMarkMap.values().forEach(item -> {
            item.setWaterMarkAttribute(waterMarkAttribute);
        });
    }

}
