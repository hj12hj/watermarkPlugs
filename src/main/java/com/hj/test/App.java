package com.hj.test;

import com.hj.core.AddWaterMark;
import com.hj.core.WaterMarkAttribute;
import com.hj.pdf.PdfAddWaterMark;
import com.itextpdf.text.DocumentException;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws DocumentException, IOException {

        AddWaterMark addWaterMark = new PdfAddWaterMark(new WaterMarkAttribute());
        addWaterMark.transfer("/Users/hejie/Desktop/11.pdf","/Users/hejie/Desktop/112.pdf","11111");


    }
}
