package com.hj.test;

import com.hj.AddWaterMarkUtil;
import com.hj.core.AddWaterMark;
import com.hj.core.WaterMarkAttribute;
import com.hj.pdf.PdfAddWaterMark;
import com.itextpdf.text.DocumentException;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws DocumentException, IOException {

        AddWaterMark addWaterMark = new AddWaterMarkUtil();
        addWaterMark.transfer("/Users/hejie/Desktop/12.docx","/Users/hejie/Desktop/1122.docx","11111");


    }
}
