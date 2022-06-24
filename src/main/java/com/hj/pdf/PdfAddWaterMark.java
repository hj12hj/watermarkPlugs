package com.hj.pdf;

import com.hj.core.AddWaterMark;
import com.hj.core.ColorConvert;
import com.hj.core.WaterMarkAttribute;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class PdfAddWaterMark implements AddWaterMark {

    /**
     * 水印属性
     */
    private WaterMarkAttribute waterMarkAttribute;

    /**
     * 颜色转换器
     */
    private ColorConvert<BaseColor> convert = new PdfColorConvert();




    public PdfAddWaterMark() {

    }

    public PdfAddWaterMark(WaterMarkAttribute waterMarkAttribute) {
        this.waterMarkAttribute = waterMarkAttribute;
    }


    @Override
    public void init() {
    }

    @Override
    public void transfer(String sourcePath, String targetPath, String WaterMarkContent) throws DocumentException, IOException {
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
            content.showTextAligned(Element.ALIGN_CENTER,WaterMarkContent,
                    waterMarkAttribute.getX()==null?300:waterMarkAttribute.getX(),
                    waterMarkAttribute.getY()==null?300:waterMarkAttribute.getY(),
                    waterMarkAttribute.getRotation()==null?30:waterMarkAttribute.getRotation());
            content.endText();
        }
        stamper.close();
    }
}
