package com.hj.pdf;

import com.hj.core.AddWaterMark;
import com.hj.core.converters.ColorConvert;
import com.hj.core.WaterMarkAttribute;
import com.hj.core.enums.WaveMarkMode;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfAddWaterMark implements AddWaterMark {

    /**
     * 水印属性
     */
    private WaterMarkAttribute waterMarkAttribute;

    /**
     * 模式
     */
    private WaveMarkMode waveMarkMode = WaveMarkMode.WORDS;

    /**
     * 图片路径
     */
    private String PicPath;

    /**
     * 颜色转换器
     */
    private ColorConvert<BaseColor> convert = new PdfColorConvert();

    public PdfAddWaterMark() {
            init();
    }

    public PdfAddWaterMark(WaveMarkMode waveMarkMode, String picPath) {
        this.waveMarkMode = waveMarkMode;
        PicPath = picPath;
    }

    public PdfAddWaterMark(WaterMarkAttribute waterMarkAttribute) {
        this.waterMarkAttribute = waterMarkAttribute;
    }

    private void transferWords(String sourcePath, String targetPath, String waterMarkContent)throws DocumentException, IOException  {
        PdfReader reader = new PdfReader(sourcePath);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(targetPath));
        PdfGState gs = new PdfGState();
        BaseFont font =  BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        // 设置透明度 默认 0.4
        gs.setFillOpacity(waterMarkAttribute.getOpacity()==null?0.4f:waterMarkAttribute.getOpacity());
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;
        for (int i = 1; i < total; i++) {
            content = stamper.getOverContent(i);
            content.beginText();
            content.setGState(gs);
            content.setCharacterSpacing(waterMarkAttribute.getCharacterSpacing()==null?12:waterMarkAttribute.getCharacterSpacing());
            //水印颜色 没给默认值 BaseColor.GRAY
            content.setColorFill(waterMarkAttribute.getColor()==null?BaseColor.GRAY:convert.convert(waterMarkAttribute.getColor()));
            content.setFontAndSize(font, waterMarkAttribute.getFontSize()==null?56:waterMarkAttribute.getFontSize()); //水印字体样式和大小
            //水印内容和水印位置
            content.showTextAligned(Element.ALIGN_CENTER,waterMarkContent,
                    waterMarkAttribute.getX()==null?300:waterMarkAttribute.getX(),
                    waterMarkAttribute.getY()==null?300:waterMarkAttribute.getY(),
                    waterMarkAttribute.getRotation()==null?30:waterMarkAttribute.getRotation());
            content.endText();
        }
        stamper.close();
    }

    private void transferPic(String sourcePath, String targetPath) throws DocumentException, IOException {
        FileOutputStream fos = new FileOutputStream(targetPath);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        PdfReader reader = new PdfReader(sourcePath);
        PdfStamper stamper = new PdfStamper(reader, bos);
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte waterMar;

        PdfGState gs = new PdfGState();
        for (int i = 1; i < total; i++) {
            waterMar = stamper.getOverContent(i);// 在内容上方加水印
            //waterMar = stamper.getUnderContent(1);//在内容下方加水印
            // 设置图片透明度为0.8f
//            gs.setFillOpacity(0.8f);
            // 设置透明度
            waterMar.setGState(gs);
            // 创建水印图片
            Image image = Image.getInstance(PicPath);
            // 水印图片位置
            image.setAbsolutePosition(250, 400);
            // 边框固定
//            image.scaleToFit(200, 200);
            // 设置旋转弧度
            //image.setRotation(30);// 旋转 弧度
            // 设置旋转角度
            //image.setRotationDegrees(45);// 旋转 角度
            // 设置等比缩放
            image.scalePercent(100);
            // 自定义大小
            image.scaleAbsolute(100,100);
            // 附件加上水印图片
            waterMar.addImage(image);
            // stroke
            waterMar.stroke();
        }
        stamper.close();
        reader.close();
    }

    @Override
    public void init() {

    }

    @Override
    public void transfer(String sourcePath, String targetPath, String waterMarkContent) throws DocumentException, IOException {
        if (this.waterMarkAttribute == null){
            this.waterMarkAttribute = new WaterMarkAttribute();
        }
       if (waveMarkMode.equals(WaveMarkMode.WORDS)){
           transferWords(sourcePath,targetPath,waterMarkContent);
       }else if (waveMarkMode.equals(WaveMarkMode.PICTURE)){
           if (PicPath==null||PicPath.equals("")){
               throw new RuntimeException("请设置水印图片路径");
           }
           transferPic(sourcePath,targetPath);
       }
    }


    public WaterMarkAttribute getWaterMarkAttribute() {
        return waterMarkAttribute;
    }

    public void setWaterMarkAttribute(WaterMarkAttribute waterMarkAttribute) {
        this.waterMarkAttribute = waterMarkAttribute;
    }

    public WaveMarkMode getWaveMarkMode() {
        return waveMarkMode;
    }

    public void setWaveMarkMode(WaveMarkMode waveMarkMode) {
        this.waveMarkMode = waveMarkMode;
    }

    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String picPath) {
        PicPath = picPath;
    }





}
