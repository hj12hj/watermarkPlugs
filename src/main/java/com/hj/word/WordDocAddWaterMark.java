package com.hj.word;


import com.hj.core.AddWaterMark;
import com.hj.core.WaterMarkAttribute;
import com.hj.core.converters.ColorConvert;
import com.hj.core.enums.WaveMarkColor;
import com.hj.core.enums.WaveMarkMode;
import com.itextpdf.text.DocumentException;
import com.spire.doc.*;
import com.spire.doc.documents.WatermarkLayout;
import org.apache.poi.hwpf.HWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;


public class WordDocAddWaterMark implements AddWaterMark {
    Logger logger = LoggerFactory.getLogger(WordDocAddWaterMark.class);
    /**
     * 颜色转换器
     */
    private ColorConvert<Color> convert = new WordColorConvert();

    /**
     * 属性封装
     */
    private WaterMarkAttribute waterMarkAttribute;

    /**
     * 模式
     */
    private WaveMarkMode waveMarkMode = WaveMarkMode.WORDS;

    /**
     * 图片路径
     */
    private String picPath;


    public WordDocAddWaterMark() {
        init();
    }

    /**
     * spire.doc 首行会有警告 这里去掉
     *  个别文件这里存在问题
     */
    // TODO 部分 doc 格式去掉首行警告问题存在问题
    private void removeWarnings(String path) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(path));
        HWPFDocument hwpfDocument = new HWPFDocument(inputStream);
        //以上Spire.Doc 生成的文件会自带警告信息，这里来删除Spire.Doc 的警告
        //hwpfDocument.delete() 该方法去掉文档指定长度的内容
        hwpfDocument.delete(0,70);
        //输出word内容文件流，新输出路径位置
        OutputStream os=new FileOutputStream(path);
        try {
            hwpfDocument.write(os);
            logger.info("生成doc文档成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            hwpfDocument.close();
            os.close();
            inputStream.close();
        }
    }

    /**
     * 文字水印
     */
    private void transferWords(String sourcePath, String targetPath, String waterMarkContent) throws IOException {
        try {
            Document document = new Document();
            document.loadFromFile(sourcePath);
            document.createParagraph();
            insertTextWatermark(document.getSections().get(0), waterMarkContent);
            document.saveToFile(targetPath, FileFormat.Doc);
            removeWarnings(targetPath);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
     * todo 图片存在问题 除去首行警告后图片水印显示有问题
     * 图片水印
     */
    private void transferPic(String sourcePath, String targetPath) {
        try {
            Document document = new Document();
            document.loadFromFile(sourcePath);
            PictureWatermark picture = new PictureWatermark();
            picture.setPicture(picPath);
            picture.setScaling(200);
            picture.isWashout(false);
            document.setWatermark(picture);
            document.getSections();
            document.saveToFile(targetPath, FileFormat.Doc);
//            removeWarnings(targetPath);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    private void insertTextWatermark(Section section, String waterMarkContent) {
        WaveMarkColor color = waterMarkAttribute.getColor();
        Float fontSize = waterMarkAttribute.getFontSize();
        TextWatermark txtWatermark = new TextWatermark();
        txtWatermark.setText(waterMarkContent);
        txtWatermark.setFontName("宋体");
        txtWatermark.setFontSize(fontSize == null ? 40 : fontSize);
        txtWatermark.setColor(color == null ? Color.black : convert.convert(color));
        txtWatermark.setLayout(WatermarkLayout.Diagonal);
        section.getDocument().setWatermark(txtWatermark);
    }

    @Override
    public void init() {

    }

    @Override
    public void transfer(String sourcePath, String targetPath, String waterMarkContent) throws DocumentException, IOException {
        if (this.waterMarkAttribute == null) {
            this.waterMarkAttribute = new WaterMarkAttribute();
        }
        if (waveMarkMode.equals(WaveMarkMode.WORDS)) {
            transferWords(sourcePath, targetPath, waterMarkContent);
        } else if (waveMarkMode.equals(WaveMarkMode.PICTURE)) {
            if (picPath == null || picPath.equals("")) {
                throw new RuntimeException("请设置水印图片路径");
            }
            transferPic(sourcePath, targetPath);
        }
    }

    @Override
    public void setWaveMarkMode(WaveMarkMode waveMarkMode) {
        this.waveMarkMode = waveMarkMode;
    }

    @Override
    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    @Override
    public void setWaterMarkAttribute(WaterMarkAttribute waterMarkAttribute) {
        this.waterMarkAttribute = waterMarkAttribute;
    }
}
